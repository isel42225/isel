package pt.isel.ls.mappers.sql;

import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.exceptions.SQLCommandException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieSQLMapper implements SQLMapper<Movie, Integer> {

    static final String title = "title";
    static final String releaseYear = "releaseYear";
    static final String duration = "duration";
    static final String mid = "mid";

    static final String SQL_SELECT_ALL = "Select * from Movies";
    static final String SQL_SELECT_BY_ID = SQL_SELECT_ALL + " where mid = ";
    static final String SQL_DELETE = "Delete from Movies where mid = ";
    static final String SQL_UPDATE = String.format("UPDATE Movies SET %s = ? , %s = ? , " +
            "%s = ? where %s = ?", title , releaseYear ,duration , mid);
    static final String SQL_INSERT = "Insert into Movies (title , releaseYear , duration)" +
            " values (? , ? , ?)";
    private static final String SQL_COUNT = "Select COUNT(*) from Movies ";

    public Connection c;
    public MovieSQLMapper(Connection c) {
        this.c = c;
    }


    @Override
    public List<Movie> selectAll() throws SQLCommandException {
        try {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(SQL_SELECT_ALL);
            List<Movie> movies = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt(mid);
                String t = rs.getString(title);
                int y = rs.getInt(releaseYear);
                int d = rs.getInt(duration);
                movies.add(new Movie(id, t, y, d));
            }
            return movies;
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }

    private List<Session> numberOfSessions(int size)
    {
        ArrayList<Session> res = new ArrayList<>();
        while(size-- > 0)
        {
            res.add(new Session());
        }
        return res;
    }
    @Override
    public Movie selectById(Integer id) throws SQLCommandException {
        try {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(SQL_SELECT_BY_ID + id);
            rs.next();

            return new
                    Movie(
                            id,
                            rs.getString(title),
                            rs.getInt(releaseYear),
                            rs.getInt(duration));
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }

    @Override
    public int count() throws SQLCommandException {
        try {
            PreparedStatement st = c.prepareStatement(SQL_COUNT);
            ResultSet rs = st.executeQuery();
            rs.next();
            return  rs.getInt(1);
        } catch (SQLException e) {
            throw new SQLCommandException(e);

        }
    }

    @Override
    public int insert(Movie entity) throws SQLCommandException {
        try {
            PreparedStatement st = c.prepareStatement(SQL_INSERT,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, entity.title);
            st.setInt(2, entity.releaseYear);
            st.setInt(3, entity.duration);
            st.execute();

            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }


    @Override
    public boolean delete(Movie entity) throws SQLCommandException {
        try {
            Statement st = c.createStatement();
            st.executeUpdate(SQL_DELETE + entity.mid);
            return false;
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }

    @Override
    public boolean update(Movie entity) throws SQLCommandException {
        try {
            PreparedStatement st = c.prepareStatement(SQL_UPDATE);
            st.setString(1, entity.title);
            st.setInt(2, entity.releaseYear);
            st.setInt(3, entity.duration);
            st.setInt(4, entity.mid);
            return st.execute();
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }
}
