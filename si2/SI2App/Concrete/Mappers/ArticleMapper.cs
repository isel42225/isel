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

    public class ArticleMapper : AbstractMapper<Article, int?, List<Article>>, IArticleMapper
    {

        public ArticleMapper(IContext context) : base(context)
        {
        }


        internal List<File> LoadFiles(ArticleProxy articleProxy) => new FileMapper(this.context).ReadWhere(new Clauses().Equals(articleProxy.Id.Value, "articleId"));

        internal List<Reviewer> LoadReviewers(ArticleProxy articleProxy)
        {
            var list = new List<Reviewer>();
            var mapper = new ReviewerMapper(this.context);
            using (IDbCommand command = this.context.CreateCommand())
            {
                command.CommandText = "select reviewerId from ArticleReviewer where articleId = @id";
                command.CommandType = CommandType.TableDirect;
                command.Parameters.Add(new SqlParameter("@id", articleProxy.Id.Value));
                var reader = command.ExecuteReader();
                while(reader.Read())
                {
                    list.Add(mapper.Read(reader.GetInt32(0)));
                }
            }
            return list;
        }

        internal List<Author> LoadAuthors(ArticleProxy articleProxy)
        {
            var list = new List<Author>();
            var mapper = new AuthorMapper(this.context);
            using (IDbCommand command = this.context.CreateCommand())
            {
                command.CommandText = "select authorId from ArticleAuthor where articleId = @id";
                command.CommandType = CommandType.TableDirect;
                command.Parameters.Add(new SqlParameter("@id", articleProxy.Id.Value));
                var reader = command.ExecuteReader();
                while (reader.Read())
                {
                    list.Add(mapper.Read(reader.GetInt32(0)));
                }
            }
            return list;
        }
        

        protected override string Table => "Article";

        protected override string SelectAllCommandText => $"select id, conferenceId, submissionDate, summary, stateId, accepted from {this.Table}";

        protected override string SelectCommandText => $"{this.SelectAllCommandText} where id = @articleId";

        protected override string UpdateCommandText => "UpdateSubmission";

        protected override CommandType UpdateCommandType => CommandType.StoredProcedure;

        

        protected override string DeleteCommandText => "DeleteSubmission";

        protected override CommandType DeleteCommandType => CommandType.StoredProcedure;

        protected override string InsertCommandText => "InsertSubmission";

        protected override CommandType InsertCommandType => CommandType.StoredProcedure;

        protected override void DeleteParameters(IDbCommand command, Article entity) => this.SelectParameters(command, entity.Id);

        protected override void InsertParameters(IDbCommand command, Article entity)
        {
            var parameters = new List<SqlParameter>
            {
#pragma warning disable IDE0009 // Member access should be qualified.
                new SqlParameter("@authors", SqlDbType.Structured)
                {
                    SqlValue = entity.Authors
                },
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
                new SqlParameter("@articleId", SqlDbType.Int)
                {
                    Direction = ParameterDirection.Output
                },
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
                new SqlParameter("@summary", entity.Summary),
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
                new SqlParameter("@conferenceId", entity.Conference.Id.Value),
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
                new SqlParameter("@file", entity.Files[0].SubmittedFile),
#pragma warning restore IDE0009 // Member access should be qualified.

            };
            
            command.Parameters.AddRange(parameters);
        }

        protected override Article Map(IDataRecord record) => 
            new ArticleProxy(
                new Article
                {
                    Id = record.GetInt32(0),
                    Conference = new ConferenceMapper(this.context).Read(record.GetInt32(1)),
                    SubmissionDate = record.GetDateTime(2),
                    Summary = record.GetString(3),
                    State = (ArticleState)record.GetInt32(4),
                    Accepted = record.IsDBNull(5)? null : (bool?)record.GetBoolean(5)
                },
                this.context
            );

        protected override void SelectParameters(IDbCommand command, int? id) => command.Parameters.Add(new SqlParameter("@articleId", id.Value));

        protected override Article UpdateEntityId(IDbCommand command, Article entity)
        {
            entity.Id = int.Parse(command.Parameters["@articleId"].ToString());
            return entity;
        }

        protected override void UpdateParameters(IDbCommand command, Article entity)
        {
            var parameters = new List<SqlParameter>
            {
#pragma warning disable IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
                new SqlParameter("@articleId", entity.Id), new SqlParameter("@summary", entity.Summary), new SqlParameter("@stateId", (int)entity.State),
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
                new SqlParameter("@accepted", entity.Accepted)
#pragma warning restore IDE0009 // Member access should be qualified.
            };

            command.Parameters.AddRange(parameters);
        }

        public IEnumerable<Reviewer> GetCompatibleReviewers(int article)
        {
            var list = new List<Reviewer>();
            var mapper = new InstitutionMapper(this.context);
            using (IDbCommand command = this.context.CreateCommand())
            {
                command.CommandText = "GetCompatibleReviewersForArticle";
                command.CommandType = CommandType.StoredProcedure;
                command.Parameters.Add(new SqlParameter("@articleId", article));
                var reader = command.ExecuteReader();
                while (reader.Read()) list.Add(new Reviewer
                {
                    Id = reader.GetInt32(0),
                    Name = reader.GetString(1),
                    Email = reader.GetString(2),
                    Institution = mapper.Read(reader.GetInt32(3))
                });
            }
            return list;
        }

        public void AttributeRevision(int article, int reviewer)
        {
            using (IDbCommand command = this.context.CreateCommand())
            {
                command.CommandText = "AttributeReviewerToRevision";
                command.CommandType = CommandType.StoredProcedure;
                command.Parameters.Add(new SqlParameter("@articleId", article));
                command.Parameters.Add(new SqlParameter("@reviewerId", reviewer));
                command.ExecuteNonQuery();
            }
        }

        public void RegisterRevision(int article, int reviewerId, string text, int grade)
        {
            using (IDbCommand command = this.context.CreateCommand())
            {
                command.CommandText = "RegisterRevision";
                command.CommandType = CommandType.StoredProcedure;
                command.Parameters.Add(new SqlParameter("@articleId", article));
                command.Parameters.Add(new SqlParameter("@reviewerId", reviewerId));
                command.Parameters.Add(new SqlParameter("@revisionText", text));
                command.Parameters.Add(new SqlParameter("@grade", grade));
                command.ExecuteNonQuery();
            }
        }
    }
}
