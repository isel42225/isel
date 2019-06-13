namespace SI2App.Model
{
    using System.Collections.Generic;

    public class Author : Attendee
    {
        public virtual List<Article> Articles { get; set; }
        public bool IsResponsible { get; set; }

        public Author() : base()
        {
            this.Articles = new List<Article>();
        }

    }
}
