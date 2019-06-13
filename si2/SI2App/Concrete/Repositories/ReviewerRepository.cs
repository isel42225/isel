namespace SI2App.Concrete.Repositories
{
    using SI2App.Concrete.Mappers;
    using SI2App.Dal;
    using SI2App.Model;
    using System.Collections.Generic;

    public class ReviewerRepository : IReviewerRepository
    {
        private IContext Context { get; set; }
        private ReviewerMapper Mapper { get; set; }

        public ReviewerRepository(IContext context)
        {
            this.Context = context;
            this.Mapper = new ReviewerMapper(context);
        }

        public IEnumerable<Reviewer> Find(Clauses clauses) => this.Mapper.ReadWhere(clauses);

        public IEnumerable<Reviewer> FindAll() => this.Mapper.ReadAll();

        public Reviewer Delete(Reviewer entity) => this.Mapper.Delete(entity);

        public Reviewer Update(Reviewer entity) => this.Mapper.Update(entity);

        public Reviewer Create(Reviewer entity) => this.Mapper.Create(entity);
    }
}
