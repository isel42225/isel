namespace SI2App.Concrete.Mappers
{
    using System;
    using System.Collections.Generic;
    using System.Data;
    using System.Data.SqlClient;
    using System.Transactions;
    using SI2App.Dal;
    using SI2App.Mapper;
    using SI2App.Mapper.Proxy;
    using SI2App.Model;

    public class ConferenceMapper : AbstractMapper<Conference, int?, List<Conference>>, IConferenceMapper
    {

        public ConferenceMapper(IContext context) : base(context)
        {
        }

        #region LOADER METHODS
        internal List<Attendee> LoadAttendees(Conference conference)
        {
            var attendees = new List<Attendee>();
            var mapper = new AttendeeMapper(this.context);

            var parameters = new List<IDataParameter>
            {
#pragma warning disable IDE0009 // Member access should be qualified.
                new SqlParameter("@id", conference.Id)
#pragma warning restore IDE0009 // Member access should be qualified.
            };

            using (var reader = this.ExecuteReader("select userId from ConferenceUser where conferenceId = @id", parameters))
            {
                while (reader.Read()) attendees.Add(mapper.Read(reader.GetInt32(0)));
            }
            return attendees;
        }

        public List<Article> LoadArticles(Conference c) => new ArticleMapper(this.context).ReadWhere(new Clauses().Equals(c.Id.Value, "conferenceId"));
        #endregion

        public override Conference Delete(Conference conf)
        {
            this.CheckEntityForNull(conf, typeof(Conference));

            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                this.EnsureContext();
                this.context.EnlistTransaction();
                var attendees = conf.Attendees;
                if (attendees != null && attendees.Count > 0)
                {
                    var p = new SqlParameter("@confId", conf.Id);
                    var parameters = new List<IDataParameter>
                    {
#pragma warning disable IDE0009 // Member access should be qualified.
                        p
#pragma warning restore IDE0009 // Member access should be qualified.
                    };
                    this.ExecuteNonQuery("delete from dbo.ConferenceUser where conferenceId=@confId", parameters);
                }
                var articleMapper = new ArticleMapper(this.context);
                foreach (var a in this.LoadArticles(conf))
                {
                    articleMapper.Delete(a);
                }
                var del = base.Delete(conf);
                ts.Complete();
                return del;
            }
        }

        public float PercentageOfAcceptedArticles(int conference)
        {
            using (IDbCommand command = this.context.CreateCommand())
            {
                command.CommandType = CommandType.StoredProcedure;
                command.CommandText = "GetPercentageOfAcceptedArticles";
                var percentage = new SqlParameter("@percentage", -1);
                var id = new SqlParameter("@conferenceId", conference);
                percentage.Direction = ParameterDirection.InputOutput;
                command.Parameters.Add(percentage);
                command.Parameters.Add(id);
                command.ExecuteNonQuery();
                var res = float.Parse(percentage.Value.ToString());
                return res;
            }
        }
        protected override string Table => "Conference";

        protected override string SelectCommandText => $"{this.SelectAllCommandText} where id=@id";

        protected override string UpdateCommandText => "UpdateConference";

        protected override CommandType UpdateCommandType => CommandType.StoredProcedure;

        protected override string DeleteCommandText => $"delete from {this.Table} where id = @id";

        protected override string InsertCommandText =>
            $@"insert into {this.Table}(name, year, acronym) values (@name, @year, @acronym); 
            select @id = scope_identity()";

        protected override string SelectAllCommandText => $"select id, name, year, acronym, grade, submissionDate from {this.Table}";

        protected override void DeleteParameters(IDbCommand command, Conference entity)
        {
            var parameter = new SqlParameter("@id", entity.Id);
            command.Parameters.Add(parameter);
        }

        protected override void InsertParameters(IDbCommand command, Conference entity)
        {
            var id = new SqlParameter("@conferenceId", SqlDbType.Int)
            {
                Direction = ParameterDirection.InputOutput
            };
            var name = new SqlParameter("@name", entity.Name);
            var year = new SqlParameter("@year", entity.Year);
            var acronym = new SqlParameter("@acronym", entity.Acronym);
            SqlParameter grade;
            if (entity.Grade.HasValue)
                grade = new SqlParameter("@grade", entity.Grade);
            else
                grade = new SqlParameter("@grade", DBNull.Value);

            SqlParameter submissionDate;
            if (entity.SubmissionDate.HasValue)
                submissionDate = new SqlParameter("@submissionDate", entity.SubmissionDate);
            else
                submissionDate = new SqlParameter("@submissionDate", DBNull.Value);
            var parameters = new List<SqlParameter>()
            {
#pragma warning disable IDE0009 // Member access should be qualified.
                id, name, year, acronym, grade,
                submissionDate
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

        protected override Conference Map(IDataRecord record) =>
            new ConferenceProxy(
                new Conference { 
                Id = record.GetInt32(0),
                Name = record.GetString(1),
                Year = record.GetInt32(2),
                Acronym = record.GetString(3),
                Grade = record.IsDBNull(4) ? null : (float?)record.GetValue(4),
                SubmissionDate = record.IsDBNull(4) ? null : (DateTime?)record.GetValue(5)
                },
                this.context
        );



        protected override void SelectParameters(IDbCommand command, int? id)
        {
            var parameter = new SqlParameter("@id", id);
            command.Parameters.Add(parameter);
        }

        protected override Conference UpdateEntityId(IDbCommand command, Conference entity)
        {
            var parameter = command.Parameters["@id"] as SqlParameter;
            entity.Id = int.Parse(parameter.Value.ToString());
            return entity;
        }

        protected override void UpdateParameters(IDbCommand command, Conference entity) => this.InsertParameters(command, entity);
    }
}
