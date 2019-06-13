namespace SI2App.Concrete
{
    using SI2App.Dal;
    using System;

    public static class ContextFactory
    {

        private static IContext Context { get; set; }

        public static IContext CreateContext(string connectionString)
        {
            if (Context == null)
            {
                Context = new Context(connectionString);
            }
            return Context;
        }

        public static object CreateContext(object configurationManager) => throw new NotImplementedException();

        public static IContext GetContext()
        {
            if (Context == null)
            {
                throw new ArgumentNullException("Context wan't created");
            }
            return Context;
        }

    }
}
