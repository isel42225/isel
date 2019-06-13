package pt.isel.ls.mappers.sql;

import pt.isel.ls.exceptions.SQLCommandException;

import java.sql.SQLException;
import java.util.List;

public interface SQLMapper<T , R> {
    int count() throws SQLCommandException;
    int insert(T entity) throws SQLCommandException;
    List<T> selectAll() throws SQLCommandException;
    T selectById(R id) throws SQLCommandException;
    boolean delete(T entity) throws SQLCommandException;
    boolean update(T entity) throws SQLCommandException;

}
