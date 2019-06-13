using SI2App.Concrete.Mappers;
using SI2App.Dal;
using SI2App.Model;
using System.Collections.Generic;

namespace SI2App.Mapper.Proxy
{
    class ConferenceProxy : Conference
    {
        private readonly IContext context;
        public ConferenceProxy(Conference c, IContext context) :base()
        {
            this.Acronym = c.Acronym;
            this.Grade = c.Grade;
            this.Id = c.Id;
            this.Name = c.Name;
            this.SubmissionDate = c.SubmissionDate;
            this.Year = c.Year;
            base.Attendees = null;
            base.Articles = null;
            this.context = context;
        }

        public override List<Attendee> Attendees {
            get
            {
                if(base.Attendees == null)
                {
                    var cm = new ConferenceMapper(this.context);
                    base.Attendees = cm.LoadAttendees(this);
                }
                return base.Attendees;
            }

            set => base.Attendees = value;
        }

        public override List<Article> Articles {
            get
            {
                if(base.Articles == null)
                {
                    var cm = new ConferenceMapper(this.context);
                    base.Articles = cm.LoadArticles(this);
                }
                return base.Articles;
            }
            set => base.Articles = value;
        }
    }
}
