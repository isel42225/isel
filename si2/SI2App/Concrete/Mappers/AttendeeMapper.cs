namespace SI2App.Concrete.Mappers
{
    using SI2App.Dal;
    using SI2App.Mapper;
    using SI2App.Mapper.Proxy;
    using SI2App.Model;
    using System;
    using System.Collections.Generic;
    using System.Data;
    using System.Data.SqlClient;

    public class AttendeeMapper : AbstractMapper<Attendee, int?, List<Attendee>>, IUserMapper
    {
        public AttendeeMapper(IContext context) : base(context)
        {
        }

        #region LOADER METHODS  
        internal List<Conference> LoadConferences(Attendee attendee)
        {
            var conferences = new List<Conference>();
            var mapper = new ConferenceMapper(this.context);
            var parameters = new List<IDataParameter>
            {
#pragma warning disable IDE0009 // Member access should be qualified.
                new SqlParameter("@id", attendee.Id)
#pragma warning restore IDE0009 // Member access should be qualified.
            };

            using (var reader = this.ExecuteReader("select userId from ConferenceUser where userId = @id", parameters))
            {
                while (reader.Read()) conferences.Add(mapper.Read(reader.GetInt32(0)));
            }

            return conferences;
        }



        internal Institution LoadInstitution(Attendee a)
        {
            Institution institution = null;
            var im = new InstitutionMapper(this.context);
            var parameters = new List<IDataParameter>
            {
#pragma warning disable IDE0009 // Member access should be qualified.
                new SqlParameter("@id", a.Id)
#pragma warning restore IDE0009 // Member access should be qualified.
            };
            var query = "Select institutionId from [User] where id=@id";
            using (var rd = this.ExecuteReader(query, parameters))
            {
                while (rd.Read())
                {
                    var key = rd.GetInt32(0);
                    institution = im.Read(key);
                }
            }

            return institution;
        }

        #endregion

        public void GiveRoleToUser(Attendee user, int role)
        {
            this.CheckEntityForNull(user, typeof(Attendee));
            using (IDbCommand command = this.context.CreateCommand())
            {
                command.CommandType = CommandType.StoredProcedure;
                command.CommandText = "GiveRoleToUser";
                var parameters = new List<SqlParameter>
                {
#pragma warning disable IDE0009 // Member access should be qualified.
                    new SqlParameter("@userId", user.Id),
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
                    new SqlParameter("@role", role)
#pragma warning restore IDE0009 // Member access should be qualified.
                };
                command.Parameters.AddRange(parameters);
                command.ExecuteNonQuery();
            }
            
        }
        protected override string Table => "[User]";

        protected override string SelectAllCommandText => $"select id, name, email, institutionId from {this.Table}";

        protected override string SelectCommandText => $"{this.SelectAllCommandText} where id = @id";

        protected override string UpdateCommandText => "UpdateUser";

        protected override CommandType UpdateCommandType => CommandType.StoredProcedure;

        protected override string DeleteCommandText => "DeleteUser";

        protected override CommandType DeleteCommandType => CommandType.StoredProcedure;

        protected override string InsertCommandText => $"InsertUser";

        protected override CommandType InsertCommandType => CommandType.StoredProcedure;

        protected override void DeleteParameters(IDbCommand command, Attendee entity) => command.Parameters.Add(new SqlParameter("@id", entity.Id));

        protected override void InsertParameters(IDbCommand command, Attendee entity)
        {
            var email = new SqlParameter("@email", entity.Email);
            var institutionId = new SqlParameter("@institutionId", entity.Institution == null ? null : entity.Institution.Id);
            var name = new SqlParameter("@name", entity.Name);
            var conferenceId = new SqlParameter("@conferenceId", entity.Conferences[0]);
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
                email, institutionId, name, id
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

        protected override Attendee Map(IDataRecord record)
        {
            var a = new Attendee
            {
                Id = record.GetInt32(0),
                Email = record.GetString(2),
                Name = record.GetString(1),
                Institution = new InstitutionMapper(this.context).Read(record.GetInt32(3))
            };
            return new AttendeeProxy(a, this.context);
        }

        protected override void SelectParameters(IDbCommand command, int? id) => command.Parameters.Add(new SqlParameter("@id", id));

        protected override Attendee UpdateEntityId(IDbCommand command, Attendee entity)
        {
            var parameter = command.Parameters["@id"] as SqlParameter;
            entity.Id = int.Parse(parameter.Value.ToString());
            return entity;
        }

        protected override void UpdateParameters(IDbCommand command, Attendee entity) => this.InsertParameters(command, entity);
    }
}
