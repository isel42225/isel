namespace SI2App.Concrete.Repositories
{
    using SI2App.Concrete.Mappers;
    using SI2App.Dal;
    using SI2App.Model;
    using System.Collections.Generic;

    public class AuthorRepository : IAuthorRepository
    {
        private IContext Context { get; set; }
        private AuthorMapper Mapper { get; set; }

        public AuthorRepository(IContext context)
        {
            this.Context = context;
            this.Mapper = new AuthorMapper(context);
        }

        public IEnumerable<Author> Find(Clauses clauses) => this.Mapper.ReadWhere(clauses);

        public IEnumerable<Author> FindAll() => this.Mapper.ReadAll();

        public Author Delete(Author entity) => this.Mapper.Delete(entity);

        public Author Update(Author entity) => this.Mapper.Update(entity);

        public Author Create(Author entity) => this.Mapper.Create(entity);
    }
}
