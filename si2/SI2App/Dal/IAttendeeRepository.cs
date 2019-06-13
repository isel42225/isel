namespace SI2App.Dal
{
    using SI2App.Model;

    public interface IAttendeeRepository : IRepository<Attendee>
    {
        void GiveRole(Attendee user, int role);
    }
}
