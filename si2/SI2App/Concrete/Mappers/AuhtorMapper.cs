namespace SI2App.Concrete.Mappers
{
    using SI2App.Dal;
    using SI2App.Mapper;
    using SI2App.Mapper.Proxy;
    using SI2App.Model;
    using System.Collections.Generic;
    using System.Data;
    using System.Data.SqlClient;

    public class AuthorMapper : AbstractMapper<Author, int?, List<Author>>, IAuthorMapper
    {   

        public AuthorMapper(IContext context) : base(context)
        {
        }

        #region LOADER METHODS
        internal List<Article> LoadArticles(Author author)
        {
            var articles = new List<Article>();
            var mapper = new ArticleMapper(this.context);

            var parameters = new List<IDataParameter>
            {
#pragma warning disable IDE0009 // Member access should be qualified.
                new SqlParameter("@id", author.Id)
#pragma warning restore IDE0009 // Member access should be qualified.
            };

            using (var reader = this.ExecuteReader("select articleId from ArticleAuthor where authorId = @id", parameters))
            {
                while (reader.Read()) articles.Add(mapper.Read(reader.GetInt32(0)));
            }
            return articles;
        }
        #endregion

        protected override string Table => "Author";

        protected override string SelectAllCommandText =>
            $"select [User].id as id, [User].name as name, [User].email as email, [User].institutionId as institutionId from {this.Table} inner join [User] on [User].id = Author.authorId";

        protected override string SelectCommandText => $"{this.SelectAllCommandText} where authorId = @id";

        protected override string UpdateCommandText => "UpdateUser";

        protected override CommandType UpdateCommandType => CommandType.StoredProcedure;

        protected override string DeleteCommandText => "DeleteUser";

        protected override CommandType DeleteCommandType => CommandType.StoredProcedure;

        protected override string InsertCommandText => "InsertUser";

        protected override CommandType InsertCommandType => CommandType.StoredProcedure;

        protected override void DeleteParameters(IDbCommand command, Author entity) => this.SelectParameters(command, entity.Id);

        protected override void InsertParameters(IDbCommand command, Author entity) => this.SelectParameters(command, entity.Id);

        protected override Author Map(IDataRecord record)
        {
            var a = new Author
            {
                Id = record.GetInt32(0),
                Name = record.GetString(1),
                Email = record.GetString(2),
                Institution = new InstitutionMapper(this.context).Read(record.GetInt32(3))
            };
            return new AuthorProxy(a, this.context);
        }

        protected override void SelectParameters(IDbCommand command, int? id) =>
            command.Parameters.Add(new SqlParameter("@id", id));

        protected override Author UpdateEntityId(IDbCommand command, Author entity)
        {
            var parameter = command.Parameters["@id"] as SqlParameter;
            entity.Id = int.Parse(parameter.Value.ToString());
            return entity;
        }

        protected override void UpdateParameters(IDbCommand command, Author entity)
        {
            var id = new SqlParameter("@id", entity.Id);
            var name = new SqlParameter("@name", entity.Name);
            var email = new SqlParameter("@email", entity.Email);
            var institutionId = new SqlParameter("@institutionId", entity.Institution == null ? null : entity.Institution.Id);
            var parameters = new List<SqlParameter>
            {
#pragma warning disable IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
#pragma warning disable IDE0009 // Member access should be qualified.
                id, name, email, institutionId
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning restore IDE0009 // Member access should be qualified.
#pragma warning restore IDE0009 // Member access should be qualified.
            };

            command.Parameters.AddRange(parameters);
        }
    }
}
