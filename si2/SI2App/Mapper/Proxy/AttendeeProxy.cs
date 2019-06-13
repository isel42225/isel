using SI2App.Concrete.Mappers;
using SI2App.Dal;
using SI2App.Model;
using System.Collections.Generic;

namespace SI2App.Mapper.Proxy
{
    public class AttendeeProxy : Attendee
    {
        private readonly IContext context;

        public AttendeeProxy(Attendee attendee, IContext context) : base()
        {
            this.Id = attendee.Id;
            this.Name = attendee.Name;
            this.Email = attendee.Email;
            base.Conferences = null;
            base.Institution = attendee.Institution;
            this.context = context;
        }
        
        public override List<Conference> Conferences
        {
            get
            {
                if (base.Institution == null)
                {
                    var mapper = new AttendeeMapper(this.context);
                    base.Conferences = mapper.LoadConferences(this);
                }
                return base.Conferences;
            }
            set => base.Conferences = value;
        }


    }
}
