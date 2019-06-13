namespace SI2App.Mapper
{
    public interface IMapper<T, TId, TCol>
    {

        T Create(T entity);
        T Read(TId id);
        TCol ReadAll();
        T Update(T entity);
        T Delete(T entity);
        TCol ReadWhere(Clauses clauses);

    }
}