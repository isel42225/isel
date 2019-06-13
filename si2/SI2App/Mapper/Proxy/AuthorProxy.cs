using SI2App.Concrete.Mappers;
using SI2App.Dal;
using SI2App.Model;
using System.Collections.Generic;

namespace SI2App.Mapper.Proxy
{
    class AuthorProxy : Author
    {
        private readonly IContext context;
        public AuthorProxy(Author a, IContext context ) : base()
        {
            this.Id = a.Id;
            this.Name = a.Name;
            this.Email = a.Email;
            this.Institution = a.Institution;
            this.context = context;
        }

        public override List<Article> Articles {
            get
            {
                if(base.Articles == null)
                {
                    var am = new AuthorMapper(this.context);
                    base.Articles = am.LoadArticles(this);
                }
                return base.Articles;
            }

            set => base.Articles = value;
        }
    }
}
