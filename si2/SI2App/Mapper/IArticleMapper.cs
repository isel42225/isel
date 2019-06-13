namespace SI2App.Mapper
{
    using SI2App.Model;
    using System.Collections.Generic;

    public interface IArticleMapper : IMapper<Article, int?, List<Article>>
    {
        IEnumerable<Reviewer> GetCompatibleReviewers(int article);
        void AttributeRevision(int article, int reviewer);

        void RegisterRevision(int article, int reviewerId, string text, int grade);
    }
}
