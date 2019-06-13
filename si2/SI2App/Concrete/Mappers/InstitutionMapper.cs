namespace SI2App.Concrete.Mappers
{
    using SI2App.Dal;
    using SI2App.Mapper;
    using SI2App.Model;
    using System;
    using System.Collections.Generic;
    using System.Data;
    using System.Data.SqlClient;

    public class InstitutionMapper : AbstractMapper<Institution, int?, List<Institution>>, IInstitutionMapper
    {
        public InstitutionMapper(IContext context) : base(context)
        {
        }

        protected override string Table => "Institution";

        protected override string SelectAllCommandText => $"select id, name, address, country, acronym from {this.Table}";

        protected override string SelectCommandText => $"{this.SelectAllCommandText} where id = @id";

        protected override string UpdateCommandText =>
            $@"update {this.Table} 
                set 
                name = isnull(@name, name), 
                address = isnull(@address, address), 
                country = isnull(@country, country), 
                acronym = isnull(@acronym, acronym) 
                where id = @id";

        protected override string DeleteCommandText => $"delete from {this.Table} where id = @id";

        protected override string InsertCommandText =>
            $"insert into {this.Table} (name, address, country, acronym) values (@name, @address, @country, @acronym); select @id = scope_identity()";

        protected override void DeleteParameters(IDbCommand command, Institution entity) => command.Parameters.Add(new SqlParameter("@id", entity.Id));

        protected override void InsertParameters(IDbCommand command, Institution entity)
        {
            var name = new SqlParameter("@name", entity.Name);
            var address = new SqlParameter("@address", entity.Address);
            var country = new SqlParameter("@country", entity.Country);
            var acronym = new SqlParameter("@acronym", entity.Acronym);

            var id = new SqlParameter("@id", DbType.Int32)
            {
                Direction = ParameterDirection.InputOutput
            };
            var parameters = new List<SqlParameter>
            {
#pragma warning disable IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
                name, address, country, acronym, id
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning restore IDE0009 // Member access should be qualified.
            };

            if (entity.Id != null)
            {
                id.Value = entity.Id;
            }
            else
            {
                id.Value = DBNull.Value;
            }
            command.Parameters.AddRange(parameters);
        }

        protected override Institution Map(IDataRecord record) =>
            new Institution
            {
                Id = record.GetInt32(0),
                Name = record.GetString(1),
                Address = record.GetString(2),
                Country = record.GetString(3),
                Acronym = record.GetString(4)
            };

        protected override void SelectParameters(IDbCommand command, int? id) => command.Parameters.Add(new SqlParameter("@id", id));

        protected override Institution UpdateEntityId(IDbCommand command, Institution entity)
        {
            var parameter = command.Parameters["@id"] as SqlParameter;
            entity.Id = int.Parse(parameter.Value.ToString());
            return entity;
        }

        protected override void UpdateParameters(IDbCommand command, Institution entity) => this.InsertParameters(command, entity);
    }
}
