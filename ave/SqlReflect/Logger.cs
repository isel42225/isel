using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;
using SqlReflect.Attributes;

namespace SqlReflect
{

  


    public class Logger
    {
        public static Logger Build(Type t)
        {
            return new Logger(t);
        }

       
        private PropertyInfo[] properties;
        private Type type;



        private Logger(Type t)
        {
            this.type = t;      
            /* build relevant properties */
            properties = t.GetProperties();
        }

        public PropertyInfo[] GetProperties()
        {
            return properties;
        }

        public String GetTableName()
        {

            TableAttribute ta =
                (TableAttribute)type.GetCustomAttribute(typeof(TableAttribute));
            return ta.Name;
           
        }

        public String GetTableIdName()
        {
            String res = null;
            
            foreach (PropertyInfo p in properties){

                if (p.IsDefined(typeof(PKAttribute))){                
                        res = p.Name;
                        return res;
                }
            }
                
            
            return res;
        }
   

        public String BuildTableCollumns()
        {
            String collumns = "";

            Type pk = typeof(PKAttribute);

            for (int i = 0; i < properties.Length; ++i)
            {
                PropertyInfo p = properties[i];
                 if (p.IsDefined(pk))
                {

                    //PKAttribute pk = (PKAttribute) p.PropertyType
                    continue;
                }
                

                Type t = p.PropertyType;
                bool b = t.IsDefined(typeof(TableAttribute), true);
                collumns += p.Name + (b ? "ID" : "");
                if (i != properties.Length - 1) collumns += ", ";
            }

            return collumns;
        }

        //return ID val from table object
        public object LogID(object obj)
        {
            if (obj == null) return null;
            object res = null;
            Type objType = obj.GetType();
            foreach (PropertyInfo p in objType.GetProperties() )
            {
                if (p.IsDefined(typeof(PKAttribute)))
                {
                    res = p.GetValue(obj);
                    break;
                }
                
               
            }

            return res;

        }


    }
}