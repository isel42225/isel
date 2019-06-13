using SqlReflect.Attributes;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Reflection;

namespace SqlReflect
{
    public class ReflectDataMapper<K,V> : AbstractDataMapper<K, V>
    {
        private static Dictionary<Type, Logger> typeLogger = new Dictionary<Type, Logger>();

        string COLUMNS;
        string SQL_GET_ALL = @"SELECT ";

        //TODO Alterar PK para permitir identity e não identity
    
        Type klass;
        PropertyInfo[] pi;
        Logger logger;
        string tableName;
        string tableIdName;
        
        


    public ReflectDataMapper(Type klass, string connStr) : base(connStr)
        {
            
            this.klass = klass;
            
            if (!typeLogger.ContainsKey(klass)){
                logger = Logger.Build(klass);
                typeLogger.Add(klass , logger);
             }
            else
            {
                logger = typeLogger[klass];
            }

            pi = logger.GetProperties();
            tableName = logger.GetTableName();
            tableIdName = logger.GetTableIdName();
            COLUMNS = logger.BuildTableCollumns();
        }

        public ReflectDataMapper(Type klass, string connStr, bool withCache) : base(connStr, withCache)
        {
            this.klass = klass;

            if (!typeLogger.ContainsKey(klass))
            {
                logger = Logger.Build(klass);
                typeLogger.Add(klass, logger);
            }
            else
            {
                logger = typeLogger[klass];
            }

            pi = logger.GetProperties();
            tableName = logger.GetTableName();
            tableIdName = logger.GetTableIdName();
            COLUMNS = logger.BuildTableCollumns();
        }


        protected override object Load(IDataReader dr)
        {
            object tableInstance = Activator.CreateInstance(klass);

            foreach (PropertyInfo p in pi)
            {
                Type t = p.PropertyType;
                bool isForeignObjectKey = t.IsDefined(typeof(TableAttribute), true);
                object foreign = null;
                string propName = p.Name;
                if (isForeignObjectKey)
                {
                    //type of foreign key
                    PropertyInfo foreignKey = t.GetProperties().First(prop => prop.IsDefined(typeof(PKAttribute)));
                    propName = foreignKey.Name;
                    Type keyType = foreignKey.GetType();
                    Type[] instTypes = { keyType, t };   //parameter types of new DataMapper
                    Type reflect = typeof(ReflectDataMapper<,>);
                    Type constructed = reflect.MakeGenericType(instTypes); //Make datamapper with parameter types 
                    AbstractDataMapper dm = 
                        (AbstractDataMapper)Activator.CreateInstance(constructed, new object[] { t, connStr }); //Down Cast is safe
                    foreign = dm.GetById(dr[propName]);    //get Foreign object

                }

                //if has 1 to N relation property
                else if (typeof(IEnumerable<>).IsAssignableFrom(t))
                {
                    Type argument = t.GetGenericArguments()[0];  //get type of IEnumerable generic argument
                    String table = t.GetCustomAttribute<TableAttribute>().Name; //table Name
                    //primary key
                    Type pk = argument.GetProperties().First(prop => prop.IsDefined(typeof(PKAttribute))).GetType();
                    Type[] generics = { pk, argument };
                    Type reflect = typeof(ReflectDataMapper<,>);
                    Type constructed = reflect.MakeGenericType(generics); //Make dataMapper with parameter types 
                    AbstractDataMapper dm =
                        (AbstractDataMapper)Activator.CreateInstance(constructed, new object[] { argument, connStr }); //Down Cast is safe

                    String clause = 
                        String.Format("Select from {0} where {1} = {2}", table, tableIdName, logger.LogID(tableInstance));  //logger used to get ID val
                    foreign = dm.Get(clause);

                }

                object propValue = dr[propName] != DBNull.Value ? dr[propName] : null;  //if DBNull , null is returned
                p.SetValue(tableInstance, foreign == null ? propValue : foreign);
            }

            return tableInstance;

        }


        private Logger GetLogger()
        {
            return logger;
        }

        protected override string SqlGetAll()
        {
            return SQL_GET_ALL + tableIdName + ", " +  COLUMNS + " from " + tableName;
        }

        protected override string SqlGetById(object id)
        {
            if (typeof(string).IsAssignableFrom(id.GetType())) id = "'" + id + "'";
            return  String.Format(
                SQL_GET_ALL + tableIdName + ", " + COLUMNS + " from {2} " + "where {0} = {1}", tableIdName , id , tableName);
        }

        protected override string SqlInsert(object target)
        {
            string res = String.Format("Insert into {0} ({1}) OUTPUT INSERTED.{2} values (", 
                tableName, COLUMNS , tableIdName);
            
            
            for(int i = 0 ; i < pi.Length; ++i)
            {
                PropertyInfo p = pi[i];
                if (p.IsDefined(typeof(PKAttribute))) continue;

                object propValue = p.GetValue(target);

                if (propValue.GetType() == typeof(string))
                {
                    res += "'" + propValue + "'";
                }
                else if (p.PropertyType.IsDefined(typeof(TableAttribute)))
                {
                    res += logger.LogID(propValue) ;
                }

                else
                {
                    res += propValue;
                }

                if (i != pi.Length - 1) res += ", ";

            }
            
            res +=")";
            return res;
        }

        protected override string SqlDelete(object target)
        {
            string res = String.Format("DELETE FROM {0} WHERE {1} =", 
                tableName, tableIdName);


            foreach(PropertyInfo p in pi)
            {
                if (p.IsDefined(typeof(PKAttribute)))
                {
                    res += p.GetValue(target);
                    break;
                }
            }

            return res;
        }

        protected override string SqlUpdate(object target)
        {
            string res = "UPDATE " + tableName +" SET ";

            for (int i = 0; i < pi.Length; ++i)
            {
                PropertyInfo p = pi[i];
                if (p.IsDefined(typeof(PKAttribute))) continue;
                object propValue = p.GetValue(target);
                if (p.PropertyType.IsDefined(typeof(TableAttribute)))
                {
                    res += p.Name + "ID";
                    propValue = logger.LogID(propValue);
                }

                else
                {
                    res +=p.Name;
                    propValue = "'" + propValue + "'";
                }
                res += "=" + propValue;
                if (i != pi.Length - 1) res += ", ";

            }

            res += " WHERE " + tableIdName + "=" + logger.LogID(target);

            return res;
        }

        protected override string SqlCount()
        {
            return "Select Count (*) from " + tableName;
        }
    }
}
