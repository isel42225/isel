namespace SI2App.Mapper
{
    using SI2App.Dal;
    using System;
    using System.Collections.Generic;
    using System.Data;

    public static class CollectionExtensions
    {
        public static void AddRange(this IDataParameterCollection collection, IEnumerable<IDataParameter> parameters)
        {
            foreach(var parameter in parameters)
            {
                collection.Add(parameter);
            }
        }
    }

    public abstract class AbstractMapper<T, TId, TCol> : IMapper<T, TId, TCol> where T : class, new() where TCol : IList<T>, IEnumerable<T>, new()
    {
        protected IContext context;

        protected abstract string Table { get; }
        protected abstract T Map(IDataRecord record);
        protected abstract T UpdateEntityId(IDbCommand command, T entity);
        protected abstract string SelectAllCommandText { get; }
        protected virtual CommandType SelectAllCommandType => CommandType.Text;
        protected virtual void SelectAllParameters(IDbCommand command) { }

        protected abstract string SelectCommandText { get; }
        protected virtual CommandType SelectCommandType => CommandType.Text;
        protected abstract void SelectParameters(IDbCommand command, TId id);

        protected abstract string UpdateCommandText { get; }
        protected virtual CommandType UpdateCommandType => CommandType.Text;
        protected abstract void UpdateParameters(IDbCommand command, T entity);

        protected abstract string DeleteCommandText { get; }
        protected virtual CommandType DeleteCommandType => CommandType.Text;
        protected abstract void DeleteParameters(IDbCommand command, T entity);

        protected abstract string InsertCommandText { get; }
        protected virtual CommandType InsertCommandType => CommandType.Text;
        protected abstract void InsertParameters(IDbCommand command, T entity);

        public AbstractMapper(IContext context)
        {
            this.context = context;
        }

        protected void EnsureContext()
        {
            if (this.context == null)
                throw new InvalidOperationException("Data Context not set.");
        }

        public virtual T Create(T entity)
        {
            this.EnsureContext();
            using (IDbCommand cmd = this.context.CreateCommand())
            {
                cmd.CommandText = this.InsertCommandText;
                cmd.CommandType = this.InsertCommandType;
                this.InsertParameters(cmd, entity);
                cmd.ExecuteNonQuery();
                var e = this.UpdateEntityId(cmd, entity);
                cmd.Parameters.Clear();
                return e;
            }
        }

        public virtual T Delete(T entity)
        {
            if (entity == null)
                throw new ArgumentException($"The {typeof(T)} to delete cannot be null");

            this.EnsureContext();
            using (IDbCommand cmd = this.context.CreateCommand())
            {
                cmd.CommandText = this.DeleteCommandText;
                cmd.CommandType = this.DeleteCommandType;
                this.DeleteParameters(cmd, entity);
                var result = cmd.ExecuteNonQuery();
                return (result == 0) ? null : entity;
            }
        }

        public virtual T Read(TId id)
        {
            this.EnsureContext();
            using (IDbCommand cmd = this.context.CreateCommand())
            {
                cmd.CommandText = this.SelectCommandText;
                cmd.CommandType = this.SelectCommandType;
                this.SelectParameters(cmd, id);
                using (var reader = cmd.ExecuteReader())
                {
                    return reader.Read() ? this.Map(reader) : null;
                }
            }
        }

        public virtual TCol ReadAll()
        {
            this.EnsureContext();
            using (IDbCommand cmd = this.context.CreateCommand())
            {
                cmd.CommandText = this.SelectAllCommandText;
                cmd.CommandType = this.SelectAllCommandType;
                this.SelectAllParameters(cmd);
                using (var reader = cmd.ExecuteReader())
                {
                    return this.MapAll(reader);
                }
            }

        }
        public virtual T Update(T entity)
        {
            if (entity == null)
                throw new ArgumentException($"The {typeof(T)} to update cannot be null");

            this.EnsureContext();

            using (IDbCommand cmd = this.context.CreateCommand())
            {
                cmd.CommandText = this.UpdateCommandText;
                cmd.CommandType = this.UpdateCommandType;
                this.UpdateParameters(cmd, entity);
                var result = cmd.ExecuteNonQuery();
                return (result == 0) ? null : entity;
            }
        }

        private TCol MapAll(IDataReader reader)
        {
            var collection = new TCol();
            while(reader.Read())
            {
                try
                {
                    collection.Add(this.Map(reader));
                }
                catch
                {
                    throw;
                }
            }
            return collection;
        }

        protected IDataReader ExecuteReader(string commandText, List<IDataParameter> parameters)
        {
            using (IDbCommand cmd = this.context.CreateCommand())
            {
                if (parameters != null)
                    cmd.Parameters.AddRange(parameters);

                cmd.CommandText = commandText;
                return cmd.ExecuteReader(CommandBehavior.Default);
            }
        }

        public virtual TCol ReadWhere(Clauses clauses)
        {
            this.EnsureContext();
            using (IDbCommand cmd = this.context.CreateCommand())
            {
                cmd.CommandText = $"{this.SelectAllCommandText} {clauses.GetWhereClause()}";
                cmd.CommandType = this.SelectAllCommandType;
                this.SelectAllParameters(cmd);
                using (var reader = cmd.ExecuteReader())
                {
                    return this.MapAll(reader);
                }
            }
        }
        protected void ExecuteNonQuery(string commandText, List<IDataParameter> parameters)
        {
            using (IDbCommand cmd = this.context.CreateCommand())
            {
                if (parameters != null)
                    cmd.Parameters.AddRange(parameters);

                cmd.CommandText = commandText;
                cmd.ExecuteNonQuery();
                cmd.Parameters.Clear();
            }
        }

        protected void CheckEntityForNull(object entity, Type type)
        {
            if (entity == null)
                throw new ArgumentException($"The {type} to delete cannot be null");
        }
    }
}
