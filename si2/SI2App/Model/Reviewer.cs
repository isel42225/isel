namespace SI2App.Model
{
    using System.Collections.Generic;

    public class Reviewer : Attendee
    {
        public virtual List<Article> Articles { get; set; }

        public Reviewer() : base()
        {
            this.Articles = new List<Article>();
        }
    }
}
