namespace SI2App.Concrete.Repositories
{
    using SI2App.Concrete.Mappers;
    using SI2App.Dal;
    using SI2App.Model;
    using System.Collections.Generic;

    public class AttendeeRepository : IAttendeeRepository
    {
        private IContext Context { get; set; }
        private AttendeeMapper Mapper { get; set; }

        public AttendeeRepository(IContext context)
        {
            this.Context = context;
            this.Mapper = new AttendeeMapper(context);
        }

        public IEnumerable<Attendee> FindAll() => this.Mapper.ReadAll();

        public IEnumerable<Attendee> Find(Clauses clauses) => this.Mapper.ReadWhere(clauses);

        public Attendee Delete(Attendee entity) => this.Mapper.Delete(entity);

        public Attendee Update(Attendee entity) => this.Mapper.Update(entity);

        public Attendee Create(Attendee entity) => this.Mapper.Create(entity);

        public void GiveRole(Attendee user, int role) => this.Mapper.GiveRoleToUser(user, role);
    }
}
