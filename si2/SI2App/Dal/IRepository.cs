namespace SI2App.Dal
{
    using System.Collections.Generic;

    public interface IRepository<T>
    {

        IEnumerable<T> FindAll();
        IEnumerable<T> Find(Clauses clauses);
        T Delete(T entity);
        T Update(T entity);
        T Create(T entity);
        
    }
}
