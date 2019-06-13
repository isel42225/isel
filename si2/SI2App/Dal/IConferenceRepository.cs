namespace SI2App.Dal
{
    using SI2App.Model;

    public interface IConferenceRepository : IRepository<Conference>
    {
        float PercentageOfAcceptedArticles(Conference c);
    }
}
