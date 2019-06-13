namespace SI2App.Dal
{
    using SI2App.Model;
    using System.Collections.Generic;

    public interface IArticleRepository : IRepository<Article>
    {
        IEnumerable<Reviewer> GetCompatibleReviewers(int article);
        void AttributeRevision(int article, int reviewer);

       void RegisterRevision(int article, int reviewerId, string text, int grade);
    }
}
