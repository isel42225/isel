using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EF_Dal
{
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Infrastructure;
    using System.Data.Entity.Core.Objects;
    using System.Linq;
    public struct Authors
    {
        int authorId;
        bool isResponsible;
        int articleId;
    }
    public partial class cs : DbContext
    {
        public virtual ObjectResult<DBNull> InsertArticleAuthor(Nullable<int> articleId, Nullable<int> authorId, Nullable<bool> isResponsible)
        {
            var articleIdParameter = articleId.HasValue ?
                new ObjectParameter("articleId", articleId) :
                new ObjectParameter("articleId", typeof(int));

            var authorIdParameter = authorId.HasValue ?
                new ObjectParameter("authorId", authorId) :
                new ObjectParameter("authorId", typeof(int));

            var isResponsibleParameter = isResponsible.HasValue ?
                new ObjectParameter("isResponsible", isResponsible) :
                new ObjectParameter("isResponsible", typeof(bool));

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<DBNull>(
                        "InsertArticleAuthor",
                         articleIdParameter,
                         authorIdParameter,
                         isResponsibleParameter);

        }
        public virtual ObjectResult<DBNull> AttributeReviewerToRevision(Nullable<int> articleId, string reviewerId)
        {
            var articleIdParameter = articleId.HasValue ?
                new ObjectParameter("articleId", articleId) :
                new ObjectParameter("articleId", typeof(int));

            var reviewerIdParameter = new ObjectParameter("reviewerId", reviewerId);

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<DBNull>(
                        "AttributeReviewerToRevision",
                         articleIdParameter,
                         reviewerIdParameter);
        }
        public virtual ObjectResult<int> ArticleAverageGrade(Nullable<int> id)
        {
            var idParameter = id.HasValue ?
                new ObjectParameter("id", id) :
                new ObjectParameter("id", typeof(int));

            return ((IObjectContextAdapter)this)
                       .ObjectContext
                       .ExecuteFunction<int>(
                            "ArticleAverageGrade",
                            idParameter);
        }

        public virtual ObjectResult<DBNull> ChangeArticleStatus(Nullable<int> conferenceId, Nullable<int> grade)
        {
            var conferenceIdParameter = conferenceId.HasValue ?
                new ObjectParameter("conferenceId", conferenceId) :
                new ObjectParameter("conferenceId", typeof(int));

            var gradeParameter = grade.HasValue ?
                new ObjectParameter("grade", grade) :
                new ObjectParameter("grade", typeof(int));

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<DBNull>(
                        "ChangeArticleStatus",
                        conferenceIdParameter,
                        gradeParameter);
        }
        public virtual ObjectResult<DBNull> ChangeSubmissionStatus(Nullable<int> conferenceId, Nullable<int> grade)
        {
            var conferenceIdParameter = conferenceId.HasValue ?
               new ObjectParameter("conferenceId", conferenceId) :
               new ObjectParameter("conferenceId", typeof(int));

            var gradeParameter = grade.HasValue ?
                new ObjectParameter("grade", grade) :
                new ObjectParameter("grade", typeof(int));

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<DBNull>(
                        "ChangeSubmissionStatus",
                        conferenceIdParameter,
                        gradeParameter);
        }
        public virtual ObjectResult<DBNull> GiveRoleToUser(Nullable<int> userId, Nullable<int> role)
        {
            var userIdParameter = userId.HasValue ?
                new ObjectParameter("userId", userId) :
                new ObjectParameter("userId", typeof(int));

            var roleParameter = role.HasValue ?
                new ObjectParameter("role", role) :
                new ObjectParameter("role", typeof(int));

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<DBNull>(
                        "GiveRoleToUser",
                        userIdParameter,
                        roleParameter);
        }

        public virtual ObjectResult<float> GetPercentageOfAcceptedArticles(Nullable<int> conferenceId, Nullable<float> percentage)
        {
            var conferenceIdParameter = conferenceId.HasValue ?
               new ObjectParameter("conferenceId", conferenceId) :
               new ObjectParameter("conferenceId", typeof(int));

            var percentageParameter = percentage.HasValue ?
                new ObjectParameter("percentage", percentage) :
                new ObjectParameter("percentage", typeof(float));

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<float>(
                        "GetPercentageOfAcceptedArticles",
                        conferenceIdParameter,
                        percentageParameter);
        }
        public virtual ObjectResult<DBNull> RegisterRevision(Nullable<int> articleId, string revisionText, Nullable<int> grade)
        {
            var articleIdParameter = articleId.HasValue ?
                new ObjectParameter("articleId", articleId) :
                new ObjectParameter("articleId", typeof(int));

            var revisionTextParameter = new ObjectParameter("revisionText", revisionText);

            var gradeParameter = grade.HasValue ?
                new ObjectParameter("grade", grade) :
                new ObjectParameter("grade", typeof(int));

            return ((IObjectContextAdapter)this)
                .ObjectContext
                .ExecuteFunction<DBNull>(
                    "RegisterRevision",
                    articleIdParameter,
                    revisionTextParameter,
                    gradeParameter);
        }
        public virtual ObjectResult<int> InsertFile(Nullable<int> articleId, byte[] file, Nullable<int> id)
        {
            var articleIdParameter = articleId.HasValue ?
                new ObjectParameter("articleId", articleId) :
                new ObjectParameter("articleId", typeof(int));

            var fileParameter = new ObjectParameter("file", file);

            var idParameter = id.HasValue ?
                new ObjectParameter("id", id) :
                new ObjectParameter("id", typeof(int));

            return ((IObjectContextAdapter)this)
                .ObjectContext
                .ExecuteFunction<int>(
                    "InsertFile",
                    articleIdParameter,
                    fileParameter,
                    idParameter);
        }
        public virtual ObjectResult<int> InsertSubmission(Nullable<Authors> authors, Nullable<int> conferenceId, string summary, byte[] file, Nullable<int> articleId)
        {
            var authorsParameter = authors.HasValue ?
                new ObjectParameter("authors", authors) :
                new ObjectParameter("authors", typeof(Authors));

            var conferenceIdParameter = conferenceId.HasValue ?
               new ObjectParameter("conferenceId", conferenceId) :
               new ObjectParameter("conferenceId", typeof(int));

            var summaryParameter = new ObjectParameter("summary", summary);

            var fileParameter = new ObjectParameter("file", file);

            var articleIdParameter = articleId.HasValue ?
                new ObjectParameter("articleId", articleId) :
                new ObjectParameter("articleId", typeof(int));

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<int>(
                        "InsertSubmission",
                        conferenceIdParameter,
                        summaryParameter,
                        fileParameter,
                        articleIdParameter);
        }
        public virtual ObjectResult<DBNull> DeleteSubmission(Nullable<int> articleId)
        {
            var articleIdParameter = articleId.HasValue ?
                new ObjectParameter("articleId", articleId) :
                new ObjectParameter("articleId", typeof(int));

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<DBNull>(
                        "DeleteSubmission",
                        articleIdParameter);
        }
        public virtual ObjectResult<DBNull> UpdateSubmission(Nullable<int> articleId, string summary, Nullable<int> stateId, Nullable<bool> accepted)
        {
            var articleIdParameter = articleId.HasValue ?
                new ObjectParameter("articleId", articleId) :
                new ObjectParameter("articleId", typeof(int));

            var summaryParameter = new ObjectParameter("summary", summary);

            var stateIdParameter = stateId.HasValue ?
                new ObjectParameter("stateId", stateId) :
                new ObjectParameter("stateId", typeof(int));

            var acceptedParameter = accepted.HasValue ?
                new ObjectParameter("accepted", accepted) :
                new ObjectParameter("accepted", typeof(bool));

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<DBNull>(
                        "UpdateSubmission",
                        articleIdParameter,
                        summaryParameter,
                        stateIdParameter,
                        acceptedParameter);
        }
        public virtual ObjectResult<DBNull> UpdateConference(Nullable<int> conferenceId, string name, Nullable<int> year, string acronym, Nullable<int> grade, Nullable<DateTime> submissionDate)
        {
            var conferenceIdParameter = conferenceId.HasValue ?
               new ObjectParameter("conferenceId", conferenceId) :
               new ObjectParameter("conferenceId", typeof(int));

            var nameParameter = new ObjectParameter("name", name);

            var yearParameter = year.HasValue ?
                new ObjectParameter("year", year) :
                new ObjectParameter("year", typeof(int));

            var acronymParameter = new ObjectParameter("acronym", acronym);

            var gradeParameter = grade.HasValue ?
                new ObjectParameter("grade", grade) :
                new ObjectParameter("grade", typeof(int));

            var submissionDateParameter = submissionDate.HasValue ?
                new ObjectParameter("submissionDate", submissionDate) :
                new ObjectParameter("submissionDate", typeof(DateTime));

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<DBNull>(
                        "UpdateConference",
                        conferenceIdParameter,
                        nameParameter,
                        yearParameter,
                        acronymParameter,
                        gradeParameter,
                        submissionDateParameter);
        }

        public virtual ObjectResult<int> InsertUser(string email, Nullable<int> institutionId, string name, Nullable<int> conferenceId, Nullable<int> id)
        {
            var emailParameter = new ObjectParameter("email", email);

            var institutionIdParameter = institutionId.HasValue ?
                new ObjectParameter("institutionId", institutionId) :
                new ObjectParameter("institutionId", typeof(int));

            var nameParameter = new ObjectParameter("name", name);

            var conferenceIdParameter = conferenceId.HasValue ?
               new ObjectParameter("conferenceId", conferenceId) :
               new ObjectParameter("conferenceId", typeof(int));

            var idParameter = id.HasValue ?
                new ObjectParameter("id", id) :
                new ObjectParameter("id", typeof(int));

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<int>(
                        "InsertUser",
                        emailParameter,
                        institutionIdParameter,
                        nameParameter,
                        conferenceIdParameter,
                        idParameter);
        }
        public virtual ObjectResult<DBNull> DeleteUser(Nullable<int> id)
        {
            var idParameter = id.HasValue ?
                new ObjectParameter("id", id) :
                new ObjectParameter("id", typeof(int));

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<DBNull>(
                        "DeleteUser",
                        idParameter);
        }
        public virtual ObjectResult<DBNull> UpdateUser(Nullable<int> id, string email, Nullable<int> institutionId, string name)
        {
            var idParameter = id.HasValue ?
               new ObjectParameter("id", id) :
               new ObjectParameter("id", typeof(int));

            var emailParameter = new ObjectParameter("email", email);

            var institutionIdParameter = institutionId.HasValue ?
                new ObjectParameter("institutionId", institutionId) :
                new ObjectParameter("institutionId", typeof(int));

            var nameParameter = new ObjectParameter("name", name);

            return ((IObjectContextAdapter)this)
                    .ObjectContext
                    .ExecuteFunction<DBNull>(
                        "UpdateUser",
                        idParameter,
                        emailParameter,
                        institutionIdParameter,
                        nameParameter);
        }

    } 
}
