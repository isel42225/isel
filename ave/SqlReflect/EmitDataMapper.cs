using System;
using System.Data;
using SqlReflect.Attributes;
using System.Reflection.Emit;
using System.Reflection;
using System.Linq;
using System.Collections.Generic;



namespace SqlReflect
{
    public class EmitDataMapper
    {
        //sort of cache for already emited types
        public static Dictionary<Type, DynamicDataMapper> log = new Dictionary<Type, DynamicDataMapper>();
        public static DynamicDataMapper Build(Type klass, string connStr, bool withCache)
        {
            //already in cache
            if (log.ContainsKey(klass)) return log[klass];

            AssemblyName aName = new AssemblyName(
                                        String.Format("{0}EmitDataMapper", klass.Name)
                                    );
            AssemblyBuilder ab =
                AppDomain.CurrentDomain.DefineDynamicAssembly(
                    aName,
                    AssemblyBuilderAccess.RunAndSave);

            // For a single-module assembly, the module name is usually
            // the assembly name plus an extension.
            ModuleBuilder mb =
                ab.DefineDynamicModule(aName.Name, aName.Name + ".dll");

            TypeBuilder tb = mb.DefineType(
                    String.Format("{0}EmitDataMapper", klass.Name),
                    TypeAttributes.Public);

            tb.SetParent(typeof(DynamicDataMapper));    //Extends DynamicDataMapper


            Type[] ctorParams = { typeof(Type), typeof(string), typeof(bool) };

            ConstructorBuilder ctorBuilder =
                 tb.DefineConstructor(
                     MethodAttributes.Public,
                     CallingConventions.Standard,
                     ctorParams);



            ILGenerator ctor = ctorBuilder.GetILGenerator();

            //Ctor code of klass.Name Data Mapper
            ctor.Emit(OpCodes.Ldarg_0); //this
            ctor.Emit(OpCodes.Ldarg_1); //type
            ctor.Emit(OpCodes.Ldarg_2); //connStr
            ctor.Emit(OpCodes.Ldarg_3); //cache
            ctor.Emit(OpCodes.Call, typeof(DynamicDataMapper).GetConstructor(ctorParams));  //call base ctor
            ctor.Emit(OpCodes.Ret);

            EmitLoad(tb, klass);
            EmitInsert(klass,tb);
            EmitUpdate(klass, tb);
            EmitDelete(klass,tb);

            Type newEmitDataMapperType = tb.CreateType();

            ab.Save(aName.Name + ".dll");

            DynamicDataMapper ret = (DynamicDataMapper)Activator
                .CreateInstance(newEmitDataMapperType, new object[] { klass, connStr, withCache });

            //add to dictionary
            log.Add(klass,ret);
            return ret;

        }

        private static void EmitLoad(TypeBuilder tb,Type klass)
        {
            MethodBuilder LoadBuilder = tb.DefineMethod(
                            "Load",
                            MethodAttributes.Family | MethodAttributes.Virtual,
                            typeof(object), //return (object)
                            new Type[] { typeof(IDataReader) });    //parameter (IDataReader)

            FieldInfo connStr = typeof(AbstractDataMapper).GetField("connStr", BindingFlags.NonPublic | BindingFlags.Instance);

            ILGenerator load = LoadBuilder.GetILGenerator();

            LocalBuilder res = load.DeclareLocal(klass);   //local target
            bool valType = klass.IsValueType; //is struct / primitive 

            if (valType)
            {  
                load.Emit(OpCodes.Ldloca_S,res);    //initObj needs an adress to initialize (Ldloca)
                load.Emit(OpCodes.Initobj, klass);   //ValueTypes uses initobj
                
            }
            else
            {   
                //isRefType
                //Load klass Ctor
                load.Emit(OpCodes.Newobj, klass.GetConstructor(Type.EmptyTypes));
                load.Emit(OpCodes.Stloc_S,res); //Local  gets ctor result
            }

            PropertyInfo[] props = klass.GetProperties();


            int idx = 0;
            foreach (PropertyInfo prop in props)
            {
                Type propType = prop.PropertyType;
                Label dbnull = load.DefineLabel();  //dbNull label
                Label setprop = load.DefineLabel(); //setprop label
                //LocalBuilder pval = load.DeclareLocal(typeof(object));

                //check if value type
                if (valType)
                {
                    load.Emit(OpCodes.Ldloca_S, res);
                }
                else
                {
                    load.Emit(OpCodes.Ldloc_S, res);
                }

                load.Emit(OpCodes.Ldarg_1); //Arg1 = IDataReader dr
                load.Emit(OpCodes.Ldc_I4, idx++);   //idx of GetValue
                load.Emit(OpCodes.Callvirt, 
                    typeof(IDataRecord).GetMethod("GetValue")); //callvirt because is interface

                load.Emit(OpCodes.Dup); //duplication to retain value from DB get
                load.Emit(OpCodes.Isinst, typeof(DBNull)); //consumes value
                load.Emit(OpCodes.Brtrue, dbnull);   //if db null goto

                //if prop is foreign key
                if (propType.IsDefined(typeof(TableAttribute)))
                {
                    //load of new type
                    LocalBuilder dbget = load.DeclareLocal(typeof(object));
                    load.Emit(OpCodes.Stloc_S, dbget); //store do valueDB
                    

                    load.Emit(OpCodes.Ldtoken, propType);  
                    load.Emit(OpCodes.Call, typeof(Type).GetMethod("GetTypeFromHandle", BindingFlags.Public | BindingFlags.Static)); //arg0 = type
                    load.Emit(OpCodes.Ldarg_0);         
                    load.Emit(OpCodes.Ldfld, connStr);  //arg1 = connstr
                    load.Emit(OpCodes.Ldc_I4, 1);       //arg2 = true
                    load.Emit(OpCodes.Call, typeof(EmitDataMapper).GetMethod("Build", BindingFlags.Public |BindingFlags.Static));
                    //top of stack is ...DynamicDataMapper
                    load.Emit(OpCodes.Ldloc_S, dbget);
                    load.Emit(OpCodes.Call, typeof(AbstractDataMapper).GetMethod("GetById"));

                }

                //stack is with valueDB
                if (propType.IsValueType)
                {
                    //value prop needs unbox 
                    load.Emit(OpCodes.Unbox_Any, propType);
                }
                else
                {
                    load.Emit(OpCodes.Castclass, propType);
                }
                load.Emit(OpCodes.Br, setprop); //jump unconditional

                //set propval to null
                load.MarkLabel(dbnull);
                load.Emit(OpCodes.Pop); //pop duplication, not needed
                load.Emit(OpCodes.Ldnull);
               
                load.MarkLabel(setprop);
                // set property
                load.Emit(OpCodes.Call, prop.GetSetMethod());
            }

            //stack empty
            load.Emit(OpCodes.Ldloc,res); //Return the created object

            if (valType)
            {   
                load.Emit(OpCodes.Box, klass);  //box to object
                
            }
            
             load.Emit(OpCodes.Ret);
            
        }


        private static void EmitInsert(Type klass, TypeBuilder tb)
        {
            MethodBuilder insertBuilder = tb.DefineMethod(
                            "SqlInsert",
                            MethodAttributes.Family | MethodAttributes.Virtual,
                            typeof(string), //Retorno (string)
                            new Type[] { typeof(object) });    //parametro (object)

            ILGenerator insert = insertBuilder.GetILGenerator();

            MethodInfo concat = typeof(string).GetMethod("Concat",
                new Type[] { typeof(string), typeof(string) });

            FieldInfo stmt = typeof(DynamicDataMapper).GetField("insertStmt", BindingFlags.NonPublic | BindingFlags.Instance);

            bool valType = klass.IsValueType;

            insert.DeclareLocal(typeof(string));    //loc_0
            insert.DeclareLocal(klass);    //loc_1

            insert.Emit(OpCodes.Ldarg_1);   //TARGET

            //cast or Unbox
            if (valType)
            {
                insert.Emit(OpCodes.Unbox_Any, klass);
            }
            else
            {
                insert.Emit(OpCodes.Castclass, klass);
            }

            insert.Emit(OpCodes.Stloc_1);   //target casted/unboxed

            insert.Emit(OpCodes.Ldarg_0);       //this DDM
            insert.Emit(OpCodes.Ldfld, stmt);   //load insert statement
            insert.Emit(OpCodes.Stloc_0);       //base.insertStmt

            insert.Emit(OpCodes.Ldloc_0);   //stmt
            insert.Emit(OpCodes.Ldstr, "(");
            insert.Emit(OpCodes.Call, concat);

            insert.Emit(OpCodes.Stloc_0);   //store statement with "("

            PropertyInfo[] props = klass.GetProperties();

            int idx = 0;
            foreach (PropertyInfo p in props)
            {
                //jump auto-increment pk
                if (p.IsDefined(typeof(PKAttribute)) && p.GetCustomAttribute<PKAttribute>().AutoIncrement)
                {
                    ++idx;
                    continue;
                }
                Type pType = p.PropertyType;
                MethodInfo concatProp = typeof(string).GetMethod("Concat",
                        new Type[] { typeof(string), pType, typeof(string) });


                //future prop val
                LocalBuilder pval = insert.DeclareLocal(pType);

                if (valType)
                {
                    insert.Emit(OpCodes.Ldloca_S, 1);    //loadAdrress
                }
                else
                {
                    insert.Emit(OpCodes.Ldloc_1);
                }

                insert.Emit(OpCodes.Call, p.GetGetMethod()); //

                

                insert.Emit(OpCodes.Stloc_S, pval);


                if (pType == typeof(string))
                {
                    insert.Emit(OpCodes.Ldstr, "'");
                    insert.Emit(OpCodes.Ldloc_S, pval);
                    insert.Emit(OpCodes.Call, concat);
                    insert.Emit(OpCodes.Ldstr, "'");
                    insert.Emit(OpCodes.Call, concat);
                    insert.Emit(OpCodes.Stloc_S, pval);

                }

                //load insert stmt
                insert.Emit(OpCodes.Ldloc_0);
                
                //check foreign type key
                if (pType.IsDefined(typeof(TableAttribute)))
                {
                    if (pType.IsValueType)
                    {
                        insert.Emit(OpCodes.Ldloca_S, pval);
                    }
                    else
                    {
                        insert.Emit(OpCodes.Ldloc_S, pval);
                    }
                    PropertyInfo pi = pType.GetProperties().First(x => x.IsDefined(typeof(PKAttribute)));
                    insert.Emit(OpCodes.Call, pi.GetGetMethod());   //foreign entity id 
                    pType = pi.PropertyType;    //change current pval type to id type
                }
                else
                {
                    insert.Emit(OpCodes.Ldloc_S, pval);
                }

                if (pType.IsValueType)
                { 
                        insert.Emit(OpCodes.Box, pType);  
                }

                //is final collumn
                if (idx++ == props.Length - 1)
                {
                    insert.Emit(OpCodes.Ldstr, "");

                }

                else
                {
                    insert.Emit(OpCodes.Ldstr, ",");
                }

                insert.Emit(OpCodes.Call, concatProp);
                insert.Emit(OpCodes.Stloc_0); 

            }

            insert.Emit(OpCodes.Ldloc_0);
            insert.Emit(OpCodes.Ldstr, ")");
            insert.Emit(OpCodes.Call, concat); //insert stmt final

            insert.Emit(OpCodes.Ret);
        }
        private static void EmitDelete(Type klass, TypeBuilder tb)
        {
            MethodBuilder deleteBuilder = tb.DefineMethod(
                           "SqlDelete",
                           MethodAttributes.Family | MethodAttributes.Virtual,
                           typeof(string), //Retorno (object)
                           new Type[] { typeof(object) });    //parametro (IDataReader)

            ILGenerator delete = deleteBuilder.GetILGenerator();

            FieldInfo stmt = typeof(DynamicDataMapper).GetField("deleteStmt", BindingFlags.NonPublic | BindingFlags.Instance);

            MethodInfo concatProp = typeof(string).GetMethod("Concat",
                       new Type[] { typeof(object), typeof(object) });

            MethodInfo concatStr = typeof(string).GetMethod("Concat",
                       new Type[] { typeof(string), typeof(string) });

            PropertyInfo pk = klass
                .GetProperties()
                .First(p => p.IsDefined(typeof(PKAttribute)));

            delete.DeclareLocal(typeof(string));    //loc_0
            delete.DeclareLocal(klass);    //loc_1
            delete.DeclareLocal(pk.PropertyType);   //loc_2

            //Unbox/Cast target
            delete.Emit(OpCodes.Ldarg_1);
            if (klass.IsValueType)
            {
                delete.Emit(OpCodes.Unbox_Any, klass);
            }
            else
            {
                delete.Emit(OpCodes.Castclass, klass);
            }

            delete.Emit(OpCodes.Stloc_1);   //target casted/unboxed

            delete.Emit(OpCodes.Ldarg_0);       //this
            delete.Emit(OpCodes.Ldfld, stmt);   //load delete statement
            delete.Emit(OpCodes.Stloc_0);       //base.deleteStmt


            delete.Emit(OpCodes.Ldloc_0);
            if (klass.IsValueType)
            {
                delete.Emit(OpCodes.Ldloca_S, 1);
            }
            else
            {
                delete.Emit(OpCodes.Ldloc_1);
            }

           
            delete.Emit(OpCodes.Call, pk.GetGetMethod());   //get id val
            delete.Emit(OpCodes.Stloc_2);   //store in loc_2 id val
            if (typeof(string).IsAssignableFrom(pk.PropertyType))
            {
                delete.Emit(OpCodes.Ldstr, "'");        //
                delete.Emit(OpCodes.Ldloc_2);           //
                delete.Emit(OpCodes.Call, concatStr);   //if string add "'"
                delete.Emit(OpCodes.Ldstr, "'");        //
                delete.Emit(OpCodes.Call, concatStr);   //
            }
            else if (pk.PropertyType.IsValueType)
            {
                delete.Emit(OpCodes.Ldloc_2);               //value types need
                delete.Emit(OpCodes.Box, pk.PropertyType);  //boxing for concat
            }
            
          
            delete.Emit(OpCodes.Call, concatProp);  //final Concat => "Delete from table.Name Where table.ID = ..."
         
            delete.Emit(OpCodes.Ret);
        }

        private static void EmitUpdate(Type klass, TypeBuilder tb)
        {
            MethodBuilder updateBuilder = tb.DefineMethod(
                           "SqlUpdate",
                           MethodAttributes.Family | MethodAttributes.Virtual,
                           typeof(string), //Retorno (object)
                           new Type[] { typeof(object) });    //parametro (IDataReader)

            ILGenerator update = updateBuilder.GetILGenerator();

            FieldInfo stmt = typeof(DynamicDataMapper).GetField("updateStmt", BindingFlags.NonPublic | BindingFlags.Instance);

            MethodInfo concat = typeof(string).GetMethod("Concat",
                       new Type[] { typeof(object), typeof(object) });

            MethodInfo format = typeof(string).GetMethod("Format",
                new Type[] { typeof(string), typeof(object), typeof(object) });


            update.DeclareLocal(typeof(string));    //loc_0
            LocalBuilder target = update.DeclareLocal(klass);             //loc_1

            update.Emit(OpCodes.Ldarg_0);       //this
            update.Emit(OpCodes.Ldfld, stmt);   //load update statement
            update.Emit(OpCodes.Stloc_0);       //base.updateStmt

            update.DeclareLocal(typeof(string));    //loc_2 (idx 0 of format)
            update.DeclareLocal(typeof(object));    //loc_3 (idx 1)

            int idx = 0;
            PropertyInfo[] props = klass.GetProperties();

            //Get primary key
            PropertyInfo pk = props.First(p => p.IsDefined(typeof(PKAttribute)));

            update.Emit(OpCodes.Ldarg_1);

            if (klass.IsValueType)
            {
                update.Emit(OpCodes.Unbox_Any, klass);
                update.Emit(OpCodes.Stloc_S, target);
                update.Emit(OpCodes.Ldloca_S, target);


            }
            else
            {
                update.Emit(OpCodes.Castclass, klass);
                update.Emit(OpCodes.Stloc_S, target);
                update.Emit(OpCodes.Ldloc_S, target);
            }
            
            update.Emit(OpCodes.Call, pk.GetGetMethod());
 
            if (pk.PropertyType.IsValueType)
            {
                update.Emit(OpCodes.Box, pk.PropertyType);
            }

            //store pk val
            update.Emit(OpCodes.Stloc_3);

            if (typeof(string).IsAssignableFrom(pk.PropertyType))
            {
                update.Emit(OpCodes.Ldstr, "'");
                update.Emit(OpCodes.Ldloc_3);
                update.Emit(OpCodes.Call, concat);
                update.Emit(OpCodes.Ldstr, "'");
                update.Emit(OpCodes.Call, concat);
                update.Emit(OpCodes.Stloc_3);
            }
            

            foreach (PropertyInfo p in props)
            {
                //jump key
                if (p.IsDefined(typeof(PKAttribute)))
                {
                    ++idx;
                    continue;
                }
                LocalBuilder pval = update.DeclareLocal(p.PropertyType); ;
                
                //load val/ref type
                if (klass.IsValueType)
                {
                    update.Emit(OpCodes.Ldloca_S,target);
                }
                else
                {
                    update.Emit(OpCodes.Ldloc_S,target);
                }

                //get prop val and store it
                update.Emit(OpCodes.Call, p.GetGetMethod());
                update.Emit(OpCodes.Stloc_S, pval);

                //load {0} of update statement to build
                update.Emit(OpCodes.Ldloc_2);
                update.Emit(OpCodes.Ldstr, p.Name); 
                update.Emit(OpCodes.Call, concat);  //concat with prop name
                update.Emit(OpCodes.Ldstr, "=");
                update.Emit(OpCodes.Call, concat);

                if (typeof(string).IsAssignableFrom(p.PropertyType))
                {
                    //wrap in "'"
                    update.Emit(OpCodes.Ldstr, "'");
                    update.Emit(OpCodes.Call, concat);

                    update.Emit(OpCodes.Ldloc_S, pval);
                    update.Emit(OpCodes.Call, concat);

                    update.Emit(OpCodes.Ldstr, "'");
                    update.Emit(OpCodes.Call, concat);

                }

                if(idx++ == props.Length - 1 )
                {
                    update.Emit(OpCodes.Ldstr, "");

                }
                else
                {
                    update.Emit(OpCodes.Ldstr, ",");
                }

                update.Emit(OpCodes.Call, concat);  
                update.Emit(OpCodes.Stloc_2);

            }

            //final loads for String.format return
            update.Emit(OpCodes.Ldloc_0);   //load insertStmt
            update.Emit(OpCodes.Ldloc_2);   //load {0}
            update.Emit(OpCodes.Ldloc_3);   //load {1}
            update.Emit(OpCodes.Call, format);

            
            update.Emit(OpCodes.Ret);
        }

        
    }
}
