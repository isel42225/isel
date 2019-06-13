namespace SI2App.Mapper
{
    using SI2App.Model;
    using System.Collections.Generic;

    public interface IUserMapper : IMapper<Attendee, int?, List<Attendee>>
    {
    }
}
