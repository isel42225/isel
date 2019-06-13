namespace SI2App.Concrete.Repositories
{
    using SI2App.Concrete.Mappers;
    using SI2App.Dal;
    using SI2App.Model;
    using System.Collections.Generic;

    public class ArticleRepository : IArticleRepository
    {
        private IContext Context { get; set; }
        private ArticleMapper Mapper { get; set; }

        public ArticleRepository(IContext context)
        {
            this.Context = context;
            this.Mapper = new ArticleMapper(context);
        }

        public IEnumerable<Article> FindAll() => this.Mapper.ReadAll();

        public IEnumerable<Article> Find(Clauses clauses) => this.Mapper.ReadWhere(clauses);

        public Article Delete(Article entity) => this.Mapper.Delete(entity);

        public Article Update(Article entity) => this.Mapper.Update(entity);

        public Article Create(Article entity) => this.Mapper.Create(entity);

        public IEnumerable<Reviewer> GetCompatibleReviewers(int article) => this.Mapper.GetCompatibleReviewers(article);

        public void AttributeRevision(int article, int reviewer) => this.Mapper.AttributeRevision(article, reviewer);

        public void RegisterRevision(int article, int reviewerId, string text, int grade) => this.Mapper.RegisterRevision(article, reviewerId, text, grade);
    }
}
