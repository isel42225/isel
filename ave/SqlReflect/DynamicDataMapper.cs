using SqlReflect.Attributes;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Reflection;

namespace SqlReflect
{
    public abstract class DynamicDataMapper : AbstractDataMapper
    {
        readonly string getAllStmt;
        readonly string getByIdStmt;
        protected readonly string insertStmt;
        protected readonly string deleteStmt;
        protected readonly string updateStmt;
        private readonly string getCountStmt;


        public DynamicDataMapper(Type klass, string connStr, bool withCache) : base(connStr, withCache)
        {
            TableAttribute table = klass.GetCustomAttribute<TableAttribute>();
            if (table == null) throw new InvalidOperationException(klass.Name + " should be annotated with Table custom attribute !!!!");

            PropertyInfo pk = klass
                .GetProperties()
                .First(p => p.IsDefined(typeof(PKAttribute)));

           

            string columns = String
                .Join(",", klass.GetProperties().Where(p => p != pk)
                .Select(p => p.PropertyType.IsDefined(typeof(TableAttribute)) ? p.Name +"ID" : p.Name ));

            bool autoIncrement = pk.GetCustomAttribute<PKAttribute>().AutoIncrement;

            getAllStmt = "SELECT " + pk.Name + "," + columns + " FROM " + table.Name;
            getByIdStmt = getAllStmt + " WHERE " + pk.Name + "=";
            insertStmt = autoIncrement ? "INSERT INTO " + table.Name + "(" + columns + ") OUTPUT INSERTED." + pk.Name + " VALUES " 
                : "INSERT INTO " + table.Name + "(" + pk.Name + "," + columns + ") OUTPUT INSERTED." + pk.Name + " VALUES ";
            deleteStmt = "DELETE FROM " + table.Name + " WHERE " + pk.Name + "=";
            updateStmt = "UPDATE " + table.Name + " SET {0} WHERE " + pk.Name + "={1}";
            getCountStmt = "SELECT COUNT (*) from " + table.Name; 
        }

        protected override string SqlGetAll()
        {
            return getAllStmt;
        }

        protected override string SqlGetById(object id)
        {
            string ret = getByIdStmt + id;
            if (typeof(string).IsAssignableFrom(id.GetType()))
            {
                ret = getByIdStmt + "'" + id + "'";
            }
            return ret;
        }
        protected override string SqlCount()
        {
            return getCountStmt;
        }

    }
}
