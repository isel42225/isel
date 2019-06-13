namespace SI2App.Dal
{
    using System;
    using System.Data.SqlClient;

    public interface IContext : IDisposable
    {

        void Open();
        SqlCommand CreateCommand();
        void EnlistTransaction();

    }
}
