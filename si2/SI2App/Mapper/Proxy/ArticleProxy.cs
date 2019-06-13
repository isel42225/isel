using SI2App.Concrete.Mappers;
using SI2App.Dal;
using SI2App.Model;
using System.Collections.Generic;

namespace SI2App.Mapper.Proxy
{
    public class ArticleProxy : Article
    {
        private readonly IContext context;

        public ArticleProxy(Article a , IContext context) : base()
        {
            this.Id = a.Id;
            this.State = a.State;
            this.Summary = a.Summary;
            this.SubmissionDate = a.SubmissionDate;
            this.Accepted = a.Accepted;
            this.Conference = a.Conference;
            base.Authors = null;
            base.Reviewers = null;
            this.context = context;
        }

        public override List<Author> Authors {
            get
            {
                if(base.Authors == null)
                {
                    var am = new ArticleMapper(this.context);
                    base.Authors = am.LoadAuthors(this);
                }
                return base.Authors;
            }

            set => base.Authors = value;
        }

        public override List<Reviewer> Reviewers
        {
            get
            {
                if(base.Reviewers == null)
                {
                    var am = new ArticleMapper(this.context);
                    base.Reviewers = am.LoadReviewers(this);
                }
                return base.Reviewers;
            }

            set => base.Reviewers = value;
        }

        public override List<File> Files
        {
            get
            {
                if(base.Files == null)
                {
                    var am = new ArticleMapper(this.context);
                    base.Files = am.LoadFiles(this);
                }
                return base.Files;
            }

            set => base.Files = value;
        }
    }
}
