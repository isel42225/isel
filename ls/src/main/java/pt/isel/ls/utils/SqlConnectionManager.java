package pt.isel.ls.utils;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import pt.isel.ls.exceptions.SQLConnectionException;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlConnectionManager {

    public static final String DB_SERVER ="localhost";
    public static  String DB_NAME = System.getenv("LS_DATABASE");
    public static  String USER = System.getenv("LS_USER");
    public static  String PASS = System.getenv("LS_PASSWORD");


    private Connection c;

    public SqlConnectionManager() {

    }

    public SqlConnectionManager (String db )
    {
        DB_NAME = db;

    }

    private void open() throws SQLConnectionException {
        try {
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setDatabaseName(DB_NAME);
            ds.setServerName(DB_SERVER);
            ds.setUser(USER);
            ds.setPassword(PASS);
            c = ds.getConnection();
        }catch (SQLServerException e){
            throw new SQLConnectionException(e.getMessage());
        }
    }

    public Connection get() throws SQLConnectionException {
        if(c == null)
            open(); //first time
        return c;
    }

    public void close() throws SQLConnectionException {
        try {
            c.close();
        } catch (SQLException e) {
            throw new SQLConnectionException(e.getMessage());
        }
    }

    public void rollback() throws SQLConnectionException {
        try {
            c.rollback();
        } catch (SQLException e) {
            throw new SQLConnectionException(e.getMessage());
        }
    }

    public void beginTransaction() throws SQLConnectionException {
        if(c != null) {
            try {
                c.setAutoCommit(false);
            } catch (SQLException e) {
                throw new SQLConnectionException(e.getMessage());
            }
        }
    }

    public void endTransaction() throws SQLConnectionException {
        if(c != null) {
            try {
                c.setAutoCommit(true);
            } catch (SQLException e) {
                throw new SQLConnectionException(e.getMessage());
            }
        }
    }
}
