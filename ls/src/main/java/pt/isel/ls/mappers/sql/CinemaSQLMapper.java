package pt.isel.ls.mappers.sql;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Theater;
import pt.isel.ls.exceptions.SQLCommandException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CinemaSQLMapper implements SQLMapper<Cinema, Integer> {

    static final String cid = "cid";
    static final String name = "name";
    static final String city = "city";


    static final String SQL_SELECT_ALL = "Select * from Cinemas";
    static final String SQL_SELECT_BY_ID = SQL_SELECT_ALL + " where cid = ?";
    static final String SQL_UPDATE = "UPDATE Cinemas SET name = ? , city = ? where cid = ?";
    static final String SQL_INSERT = "Insert into Cinemas (name , city) values (? , ?)";
    static final String SQL_DELETE = "Delete from Cinemas where cid = ?";
    private static final String SQL_COUNT = "Select COUNT (*) from Cinemas";


    private Connection c;

    public CinemaSQLMapper(Connection c) {
        this.c = c;
    }


    @Override
    public List<Cinema> selectAll() throws SQLCommandException {
        try {
            PreparedStatement st = c.prepareStatement(SQL_SELECT_ALL);
            ResultSet rs = st.executeQuery();
            List<Cinema> cins = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("cid");
                String name = rs.getString("name");
                String city = rs.getString("city");
                Cinema c = new Cinema(id, name, city);

                cins.add(c);
            }
            return cins;
        }catch (SQLException e){
            throw new SQLCommandException(e.getMessage());
        }
    }


    @Override
    public Cinema selectById(Integer id) throws SQLCommandException {
        try {
            PreparedStatement st = c.prepareStatement(SQL_SELECT_BY_ID);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            rs.next() ;


            String name = rs.getString("name");
            String city = rs.getString("city");


            Cinema cin = new Cinema(id, name, city);

            return cin;
        }catch (SQLException e ){
            throw new SQLCommandException(id.toString(),e );
        }
    }

    @Override
    public int insert(Cinema entity) throws SQLCommandException {
        try {
            PreparedStatement st = c.prepareStatement(SQL_INSERT,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, entity.name);
            st.setString(2, entity.city);
            st.execute();

            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            int cid = rs.getInt("GENERATED_KEYS");
            entity.cid = cid;
            return cid;

        }catch (SQLException e){
            throw  new SQLCommandException(e);
        }
    }


    @Override
    public boolean delete(Cinema entity) throws SQLCommandException {
        try {
            PreparedStatement st = c.prepareStatement(SQL_DELETE);
            st.setInt(1, entity.cid);
            return st.execute();

        }catch (SQLException e){
            throw  new SQLCommandException(e);
        }
    }

    @Override
    public boolean update(Cinema entity) throws SQLCommandException {
        try {
            PreparedStatement st = c.prepareStatement(SQL_UPDATE);
            st.setString(1, entity.name);
            st.setString(2, entity.city);
            st.setInt(3, entity.cid);
            return st.execute();

        }catch (SQLException e){
            throw  new SQLCommandException(e);
        }
    }


    @Override
    public int count () throws SQLCommandException {
        try {
            PreparedStatement st = c.prepareStatement(SQL_COUNT);
            ResultSet rs = st.executeQuery();
            rs.next();
            return  rs.getInt(1);
        } catch (SQLException e) {
            throw new SQLCommandException(e);

        }
    }

}
