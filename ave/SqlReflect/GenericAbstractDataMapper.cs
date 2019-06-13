using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Common;
using System.Data.SqlClient;


namespace SqlReflect
{
    static class EnumerableUtils
    {
      
    }

    public abstract class AbstractDataMapper<K, V> : AbstractDataMapper , IDataMapper<K, V>
    {
        public AbstractDataMapper(string connStr, bool withCache) : base(connStr, withCache)
        {
        }

        public AbstractDataMapper(string connStr) : base(connStr)
        {
        }

        private new IEnumerable<V> Get(string sql)
        {
            if (listener != null) listener();
            string tableName = GetTableNameFromSql(sql, "FROM ");
            if (cache == null)
                return GetFromDb(sql, tableName);

            DataTable table = cache.Tables[tableName];
            return table != null
                ? sql.ToUpper().Contains("WHERE") ? DataReaderToLazy(sql, table.CreateDataReader()) : DataReaderToLazy(table.CreateDataReader())
                : GetFromDb(sql, tableName);
        }

        private IEnumerable<V> DataReaderToLazy(string sql, IDataReader dr)
        {
            string[] clause = sql
                .ToUpper()
                .Split(new[] { " WHERE " }, StringSplitOptions.None)
                [1]  // Last part
                .Split('=');

            return LazyGetById(dr, clause);
            
        }

        private IEnumerable<V> DataReaderToLazy(IDataReader dr)
        {
            return LazyGet(dr);
        }

        private IEnumerable<V> GetFromDb(string sql, string tableName)
        {
            SqlConnection con;
            SqlCommand cmd;
            DbDataReader dr;

            using (con = new SqlConnection(connStr))
            {
                using (cmd = con.CreateCommand())
                {
                    cmd.CommandText = sql;
                    con.Open();
                    dr = cmd.ExecuteReader();
                    dr = AddToCache(dr, tableName);
                    return LazyGet(dr);
                }
            }
            
        }

        IEnumerable<V> LazyGet (IDataReader dr)
        {
            using (dr)
            {
                while (dr.Read())
                {
                    yield return (V)Load(dr);   //safe cast
                }

            }
        }

        IEnumerable<V> LazyGetById(IDataReader dr , string [] clause)
        {
            using (dr)
            {
                if (clause != null)
                {
                    char[] c = {};
                    string col = clause[0].Trim();
                    string val = clause[1].Trim().Replace("'" , "");    //when id is string remove extra "'"
                
                while (dr.Read())
                {
                  if (dr[col].ToString().Equals(val)) break ;
                }
                    yield return (V)Load(dr);   //lazy return
                }
            }
        }

        public V GetById(K id)
        {
            string sql = SqlGetById(id);
            if (cache != null)
            {
                string tableName = GetTableNameFromSql(sql, "FROM ");
                DataTable table = cache.Tables[tableName];
                if (table == null) GetAll();
            }
            IEnumerator<V> iter = Get(sql).GetEnumerator();
            return iter.MoveNext() ? iter.Current : default(V) ;    //default because V can be value type
        }

        //new to signal that is different from previous implementation (?)
        public new IEnumerable<V> GetAll()
        {
            return Get(SqlGetAll());
        }

        private object Execute(string sql, string tableName)
        {
            if (listener != null) listener();  //signal listener (?)
            RemoveFromCache(tableName); //remove because cache will be outdated
            
            using (SqlConnection con  = new SqlConnection(connStr))
            {
                using (SqlCommand cmd = con.CreateCommand())
                {
                    cmd.CommandText = sql;
                    con.Open();
                   
                    return cmd.ExecuteScalar();
                }
            }
        }
        private void RemoveFromCache(string tableName)
        {
            if (cache != null && cache.Tables.Contains(tableName))
                cache.Tables.Remove(tableName);
        }

        public K Insert(V target)
        {
            string sql = SqlInsert(target);
            string tableName = GetTableNameFromSql(sql, "INTO ");
            return (K) Execute(sql, tableName);
        }

        public void Update(V target)
        {
            string sql = SqlUpdate(target);
            string tableName = GetTableNameFromSql(sql, "UPDATE ");
            Execute(sql, tableName);
        }

       
        public void Delete(V target)
        {
            string sql = SqlDelete(target);
            string tableName = GetTableNameFromSql(sql, "FROM ");
            Execute(sql, tableName);
        }
    }
}