
namespace SI2App.Concrete.Repositories
{
    using SI2App.Concrete.Mappers;
    using System.Collections.Generic;
    using SI2App.Dal;
    using SI2App.Model;

    public class ConferenceRepository : IConferenceRepository
    {
        private IContext Context { get; set; }
        private ConferenceMapper Mapper { get; set; }

        public ConferenceRepository(IContext context)
        {
            this.Context = context;
            this.Mapper = new ConferenceMapper(context);
        }

        public IEnumerable<Conference> Find(Clauses clauses) => this.Mapper.ReadWhere(clauses); 

        public IEnumerable<Conference> FindAll() => this.Mapper.ReadAll();


        public Conference Delete(Conference entity) => this.Mapper.Delete(entity);

        public Conference Update(Conference entity) => this.Mapper.Update(entity);

        public Conference Create(Conference entity) => this.Mapper.Create(entity);



        public float PercentageOfAcceptedArticles(Conference c) => this.Mapper.PercentageOfAcceptedArticles(c.Id.Value);

    }
}
