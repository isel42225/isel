using SI2App.Concrete.Mappers;
using SI2App.Dal;
using SI2App.Model;
using System.Collections.Generic;

namespace SI2App.Mapper.Proxy
{
    class ReviewerProxy : Reviewer
    {
        private readonly IContext context;
        public ReviewerProxy(Reviewer r, IContext context): base()
        {
            base.Articles = r.Articles;
            this.Email = r.Email;
            this.Id = r.Id;
            base.Institution = r.Institution;
            this.Name = r.Name;
            this.context = context;
        }

        public override List<Article> Articles {
            get
            {
                if(base.Articles == null)
                {
                    var rm = new ReviewerMapper(this.context);
                    base.Articles = rm.LoadArticles(this);
                }
                return base.Articles;
            }

            set => base.Articles = value;
        }
    }
}
