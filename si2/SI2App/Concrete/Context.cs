namespace SI2App.Concrete
{
    using SI2App.Concrete.Repositories;
    using SI2App.Dal;
    using System.Data;
    using System.Data.SqlClient;
    using System.Transactions;

    public class Context : IContext
    {
        private readonly string connectionString;
        private SqlConnection con = null;

        public Context(string cs)
        {
            this.connectionString = cs;
            this.Conferences = new ConferenceRepository(this);
            this.Users = new AttendeeRepository(this);
            this.Articles = new ArticleRepository(this);
            this.Reviewers = new ReviewerRepository(this);
        }

        public SqlCommand CreateCommand()
        {
            this.Open();
            return this.con.CreateCommand();
        }

        public void Dispose()
        {
            if (this.con != null)
            {
                this.con.Dispose();
                this.con = null;
            }
        }

        public void EnlistTransaction()
        {
            if (this.con != null)
            {
                this.con.EnlistTransaction(Transaction.Current);
            }
        }

        public void Open()
        {
            if (this.con == null)
            {
                this.con = new SqlConnection(this.connectionString);
            }
            if (this.con.State != ConnectionState.Open)
            {
                this.con.Open();
            }
        }

        public IConferenceRepository Conferences { get; }

        public IAttendeeRepository Users { get; }

        public IArticleRepository Articles { get; }

        public IReviewerRepository Reviewers { get; }
    }
}
