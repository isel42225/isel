namespace SI2App.Model
{
    using System.Collections.Generic;

    public class Attendee
    {
        public int? Id { get; set; }

        public string Name { get; set; }

        public string Email { get; set; }

        public virtual Institution Institution { get; set; }

        public virtual List<Conference> Conferences { get; set; }

        public Attendee()
        {
            this.Conferences = new List<Conference>();
        }
    }
}
