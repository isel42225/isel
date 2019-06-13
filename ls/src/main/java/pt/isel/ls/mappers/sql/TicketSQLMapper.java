package pt.isel.ls.mappers.sql;

import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.exceptions.SQLCommandException;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketSQLMapper implements SQLMapper<Ticket,  Integer> {

    //conforme DB
    static final String tkid = "tkid";
    static final String Sid = "session_id";
    static final String row = "row";
    static final String seatNumber = "seat_number";



    static final String SQL_SELECT_ALL = "Select * from Tickets";
    static final String SQL_SELECT_BY_ID = SQL_SELECT_ALL + " where  tkid = ?";
    static final String SQL_DELETE = "Delete from Tickets where tkid = ?";
    static final String SQL_UPDATE =
            "UPDATE Tickets SET session_id = ? ,row = ? , seat_number = ? where tkid = ?";
    static final String SQL_INSERT =
            "Insert into Tickets (session_id, row, seat_number) values (?, ?, ?)";
    private static final String SQL_COUNT = "Select COUNT (*) from Tickets";



    //IDEA => Map de tkid , seat (number + row)

    Map<Integer, String> seats = new HashMap<>();

    public Connection c;

    public TicketSQLMapper(Connection c) {

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
    public int insert(Ticket entity) throws SQLCommandException {
        try{
            TheaterSQLMapper tMap = new TheaterSQLMapper(c);
            SessionSQLMapper sMap = new SessionSQLMapper(c);
            Session session = sMap.selectById(entity.getSessionId());
            Theater th = tMap.selectById(session.tid);
            if(th.availableSeats == 0)
                throw new SQLCommandException("SESSION FULL");

            PreparedStatement st = c.prepareStatement(SQL_INSERT,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            st.setInt(1,entity.getSessionId());
            st.setString(2,entity.rowLetter);
            st.setInt(3,entity.seatNumber);
            st.execute();

            ResultSet rs = st.getGeneratedKeys();
            rs.next();

            int tkid = rs.getInt(1);
            entity.setTkid(tkid);


            tMap.updateSeats(th, --th.availableSeats);
            return tkid;

        }catch (SQLException e){
            throw new SQLCommandException("Error inserting ticket");
        }
    }


    @Override
    public List<Ticket> selectAll() throws SQLCommandException {

        try {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(SQL_SELECT_ALL);
            List<Ticket> tickets = new ArrayList<>();
            while (rs.next()) {
                int tkidTable = rs.getInt(tkid);
                int sidTable  = rs.getInt(Sid);
                int seatNumberTable  = rs.getInt(seatNumber);
                String rowTable  = rs.getString(row);

                tickets.add(new Ticket(tkidTable, sidTable, seatNumberTable,rowTable ));
            }
            return tickets;
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }

    @Override
    public Ticket selectById(Integer id) throws SQLCommandException {
        try{

            PreparedStatement st = c.prepareStatement(SQL_SELECT_BY_ID);
            st.setInt(1,id);

            ResultSet rs =  st.executeQuery();
            rs.next();

            int tkid = rs.getInt("tkid");
            int sid = rs.getInt(Sid);
            int seatNumber = rs.getInt("seat_number");
            String row = rs.getString("row");


            Ticket t = new Ticket(tkid, sid ,seatNumber,row );
            return t;

        }catch (SQLException e){
            throw new SQLCommandException("Error getting ticket");
        }
    }

    @Override
    public boolean delete(Ticket entity) throws SQLCommandException {
        try{

            PreparedStatement st = c.prepareStatement(SQL_DELETE);
            st.setInt(1,entity.getTkid());
            boolean ret =  st.execute();
            SessionSQLMapper sMap = new SessionSQLMapper(c);
            Session session = sMap.selectById(entity.getSessionId());
            TheaterSQLMapper tMap = new TheaterSQLMapper(c);
            Theater th = tMap.selectById(session.tid);
            tMap.updateSeats(
                    th,
                    ++th.availableSeats
                );

            return ret;
        }catch (SQLException e){
            throw new SQLCommandException(e.getMessage());
        }
    }

    @Override
    public boolean update(Ticket entity) throws SQLCommandException {
        try{
            PreparedStatement st = c.prepareStatement(SQL_UPDATE);
            st.setInt(1, entity.getSessionId());
            st.setString(2, entity.getRow());
            st.setInt(3, entity.getSeat());
            st.setInt(4, entity.getTkid());
            return st.execute();
        }catch (SQLException e)
        {
            throw new SQLCommandException(e.getMessage());
        }
    }


    public List<Ticket> getFromSession(int sid) throws SQLCommandException{
        try{

            PreparedStatement st = c.prepareStatement(SQL_SELECT_ALL +" where " + Sid +" = ?");
            st.setInt(1,sid);

            List<Ticket> res = new ArrayList<>();

            ResultSet rs =  st.executeQuery();
            while(rs.next()) {

                int tkid = rs.getInt("tkid");
                int seatNumber = rs.getInt("seat_number");
                String row = rs.getString("row");


                Ticket t = new Ticket(tkid, sid,seatNumber ,row );
                res.add(t);
            }
            return res;
        }catch (SQLException e){
            throw new SQLCommandException("Error getting ticket");
        }

    }

    public Ticket getFromSeat(int sid, String seat) throws SQLCommandException{

        String row = seat.substring(0,1);
        int seatNumber = Integer.parseInt(seat.substring(1));
        List<Ticket> tickets = getFromSession(sid);

        for(Ticket t : tickets){
            if(t.rowLetter.equalsIgnoreCase(row) && t.seatNumber == seatNumber)
                return t;
        }
        return null;


    }

}
