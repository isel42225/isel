package pt.isel.ls.mappers.sql;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Theater;
import pt.isel.ls.exceptions.SQLCommandException;
import pt.isel.ls.result.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TheaterSQLMapper implements SQLMapper<Theater, Integer> {




    static final String cid = "cinemas_cid";
    static final String tid = "tid";
    static final String name = "name";
    static final String availableSeats = "available_seats";
    static final String rows = "rows";
    static final String seatsPerRow = "seats_per_row";

    static final String SQL_SELECT_ALL = "Select * from Theaters";
    static final String SQL_SELECT_BY_ID = "Select * from Theaters where tid = ?";
    static final String SQL_DELETE = "Delete from Theaters where tid = ?";
    static final String SQL_UPDATE = "UPDATE Theaters SET cinemas_cid = ? , name = ?, available_seats = ? ," +
            " rows = ? , seats_per_row = ? where tid = ?";
    static final String SQL_UPDATE_SEATS = String.format("UPDATE Theaters SET %s = ?" +
            " where %s = ?",  availableSeats, tid);
    static final String SQL_INSERT = "Insert into Theaters(cinemas_cid , name , available_seats , rows ,seats_per_row)" +
            " values (? , ? , ?, ? ,?)";
    private static final String SQL_COUNT =
            "Select COUNT (*) from Theaters where cinemas_cid = ?";

    private Connection c;


    public TheaterSQLMapper(Connection c )
    {
        this.c = c;
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
    public int insert(Theater entity) throws SQLCommandException {
        try {
            PreparedStatement st =
                    c.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);

            st.setInt(1, entity.cid);
            st.setString(2, entity.name);
            st.setInt(3, entity.availableSeats);
            st.setInt(4, entity.rows);
            st.setInt(5, entity.seatsPerRow);
            st.execute();

            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            int tid = rs.getInt(1);
            entity.tid = tid;
            return tid;
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }

    @Override
    public List<Theater> selectAll() throws SQLCommandException {
        try{
            PreparedStatement st = c.prepareStatement(SQL_SELECT_ALL);

            ResultSet rs = st.executeQuery();
            List<Theater> res = new ArrayList<>();
            while (rs.next())
            {
                Theater t = new Theater(
                        rs.getInt(tid),
                        rs.getInt(cid),
                        rs.getString(name),
                        rs.getInt(availableSeats),
                        rs.getInt(rows),
                        rs.getInt(seatsPerRow)
                );

                res.add(t);
            }
            return res;
        }catch (SQLException e) {
            throw new SQLCommandException(e.getMessage());
        }
    }

    @Override
    public Theater selectById(Integer id) throws SQLCommandException {
        try {

            PreparedStatement st = c.prepareStatement(SQL_SELECT_BY_ID);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            rs.next();


            Theater t = new Theater(id,
                            rs.getInt(cid),
                            rs.getString(name),
                            rs.getInt(availableSeats),
                            rs.getInt(rows),
                            rs.getInt(seatsPerRow)
                    );


            return t;
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }

    @Override
    public boolean delete(Theater entity) throws SQLCommandException {
        try{
            PreparedStatement st = c.prepareStatement(SQL_DELETE);
            st.setInt(1 , entity.tid);
            return st.execute();
        }catch (SQLException e)
        {
            throw new SQLCommandException(e.getMessage());
        }
    }

    @Override
    public boolean update(Theater entity) throws SQLCommandException {
        try {
            PreparedStatement st =
                    c.prepareStatement(SQL_UPDATE);

            st.setInt(1, entity.cid);
            st.setString(2,entity.name);
            st.setInt(3, entity.availableSeats);
            st.setInt(4, entity.rows);
            st.setInt(5, entity.seatsPerRow);
            st.setInt(6, entity.tid);

            return st.execute();
        }catch (SQLException e){
            throw new SQLCommandException(e.getMessage());
        }
    }

    public boolean updateSeats(Theater t , int nseats) throws SQLCommandException
    {
        try {
            PreparedStatement st =
                    c.prepareStatement(SQL_UPDATE_SEATS);

            st.setInt(1,nseats);
            st.setInt(2, t.tid);

            return st.execute();
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }



    public List<Theater> theathersOfCinema(int cid) throws SQLCommandException {
        try {

            String query = "Select * from Theaters where cinemas_cid = ?";
            PreparedStatement st = c.prepareStatement(query);
            st.setInt(1, cid);
            ResultSet rs = st.executeQuery();
            List<Theater> res = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt(tid);
                Theater t = new Theater(
                        id,
                        cid,
                        rs.getString(name),
                        rs.getInt(availableSeats),
                        rs.getInt(rows),
                        rs.getInt(seatsPerRow)
                );

                res.add(t);
            }

            return res;
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }
}
