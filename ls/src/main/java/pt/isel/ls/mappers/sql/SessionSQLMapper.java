package pt.isel.ls.mappers.sql;

import pt.isel.ls.model.*;
import pt.isel.ls.exceptions.SQLCommandException;

import java.sql.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SessionSQLMapper implements SQLMapper<Session, Integer> {


    private static final String sid = "sid";
    private static final String cid = "cinema_id";
    private static final String tid = "theater_id";
    private static final String Mid = "movie_id";
    private static final String date = "date_time";

    static final String SQL_SELECT_ALL = "Select * from Sessions";
    static final String SQL_SELECT_BY_ID = SQL_SELECT_ALL + " where sid = ?";
    static final String SQL_DELETE = "Delete from Sessions where sid = ?";
    static final String SQL_UPDATE = "UPDATE Sessions SET cinema_id = ? , " +
            "theater_id = ? , movie_id = ? , date_time = ? where sid = ?";
    static final String SQL_INSERT = "Insert into Sessions (cinema_id , theater_id , movie_id , date_time)" +
            " values (? , ? , ?, ?)";
    private static final String SQL_COUNT = "Select COUNT (*) from Sessions where " + Mid + "= ?";

    private static final String SQL_SELECT_FROM_CINEMA = SQL_SELECT_ALL + " where cinema_id = ?";
    private static final String SELECT_SESSIONS_OF_DATE = "Select cinema_id from Sessions where CAST(Sessions.date_time as Date) = ?";


    public Connection c;

    public SessionSQLMapper(Connection c)
    {
        this.c = c;

    }


    @Override
    public int count() throws SQLCommandException {
        return 0;
    }

    @Override
    public int insert(Session entity) throws SQLCommandException {
        try {
            if (!verifyOverlap(entity))
                throw new SQLCommandException("Overlapping Sessions!");

            PreparedStatement st = c.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setInt(1, entity.cid);
            st.setInt(2, entity.tid);
            st.setInt(3, entity.mid);
            Timestamp t = Timestamp.
                    valueOf(LocalDateTime.of(entity.localDate, entity.localTime));
            st.setTimestamp(4, t);

            st.execute();

            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            int sid = rs.getInt(1);
            entity.sid = sid;

            return sid;
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }

    private boolean verifyOverlap(Session entity) throws SQLCommandException {

        MovieSQLMapper movMap = new MovieSQLMapper(c);
        Movie mov = movMap.selectById(entity.mid);
        int movieDuration = mov.duration;

        LocalDate entityDate = entity.localDate;
        LocalTime entityTime = entity.localTime;
        int entityCinema = entity.cid;

        List<Session> ofDateSessions =
                getSessionsOfDate(entityCinema ,entityDate );

        for(Session s : ofDateSessions){
            Movie m = movMap.selectById(s.mid);
            int sessionDuration = mov.duration;

            LocalTime sessionTime = s.localTime;

            if(sessionTime.equals(entityTime)) return false;
            if(sessionTime.isBefore(entityTime)){
                return !sessionTime.plusMinutes(sessionDuration).isAfter(entityTime);
            }

            else{
                if(!entityTime.plusMinutes(movieDuration).isBefore(sessionTime))
                    return false;
            }

        }
        return true;
    }

    @Override
    public List<Session> selectAll() throws SQLCommandException {
        try {

            PreparedStatement st = c.prepareStatement(SQL_SELECT_ALL);
            ResultSet rs = st.executeQuery();
            List<Session> sessions = new ArrayList<>();


            while (rs.next()) {
                int theater_id = rs.getInt(tid);
                int movie_id = rs.getInt(Mid);
                Timestamp dateTime = rs.getTimestamp(date);
                LocalDate localDate = dateTime.toLocalDateTime().toLocalDate();
                LocalTime localTime = dateTime.toLocalDateTime().toLocalTime();


                Session s = new Session(rs.getInt(sid), rs.getInt(cid),
                        theater_id, movie_id, localDate, localTime);
                sessions.add(s);
            }
            return sessions;
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }

    @Override
    public Session selectById(Integer id) throws SQLCommandException {
        try{

            PreparedStatement st = c.prepareStatement(SQL_SELECT_BY_ID);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            rs.next();

            Timestamp dateTime = rs.getTimestamp(date);
            LocalDate localDate = dateTime.toLocalDateTime().toLocalDate();
            LocalTime localTime = dateTime.toLocalDateTime().toLocalTime();

            Session s = new Session(id, rs.getInt(cid), rs.getInt(tid) ,
                    rs.getInt(Mid), localDate , localTime);

            return s;
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }
    }


    @Override
    public boolean delete(Session entity) throws SQLCommandException {
       try{
           PreparedStatement st = c.prepareStatement(SQL_DELETE);
           st.setInt(1, entity.sid);
           return st.execute();
       }catch (SQLException e){
           throw new SQLCommandException(e.getMessage());
       }
    }

    @Override
    public boolean update(Session entity) throws SQLCommandException {
        try{
            LocalDateTime dateTime = LocalDateTime.of(entity.localDate, entity.localTime);

            PreparedStatement st = c.prepareStatement(SQL_UPDATE);
            st.setInt(1, entity.cid);
            st.setInt(2, entity.tid);
            st.setInt(3, entity.mid);
            st.setTimestamp(4, Timestamp.valueOf(dateTime));
            st.setInt(5, entity.sid);
            return st.execute();

        }catch (SQLException e){
            throw new SQLCommandException(e.getMessage());
        }
    }


    public List<Session> getSessionsOfTheaterFromCinema(int cid , int tid) throws SQLCommandException{
        List<Session> cinemaSessions = getSessionsFromCinema(cid);
        List<Session> res = new ArrayList<>();
        for(Session s : cinemaSessions){
            if(s.tid == tid)
                res.add(s);
        }
        return res;
    }

    public List<Session> getSessionsFromCinema(int cid) throws SQLCommandException {
        try{
            TheaterSQLMapper tMap = new TheaterSQLMapper(c);

            List<Theater> theaters =  tMap.theathersOfCinema(cid);

            String query = SQL_SELECT_ALL + " where theater_id = ?";
            List<Session> res = new ArrayList<>();
            for(Theater t : theaters) {
               PreparedStatement st = c.prepareStatement(query);
               st.setInt(1,t.tid);
               ResultSet rs = st.executeQuery();

               while (rs.next()) {
                   int id = rs.getInt(sid);
                   Session s = selectById(id);
                   res.add(s);
               }
           }
            return res;
        }catch (SQLException e){
            throw new SQLCommandException(e);
        }

    }

    public List<Session> getTodaySessionsOfCinema(int cid) throws SQLCommandException {
        List<Session> cinemaSessions = getSessionsFromCinema(cid);
        List<Session> res = new ArrayList<>();
        LocalDate today = LocalDate.now();


        for (Session s : cinemaSessions) {
            LocalDate sessionDate = s.localDate;

            if (today.isEqual(sessionDate)) {
                res.add(s);
            }
        }
        return res;
    }

    public List<Session> getTodaySessionsOfCinemaOfTheater(int cid, int tid) throws SQLCommandException {
        List<Session> cinemaSessions = getTodaySessionsOfCinema(cid);
        List<Session> res = new ArrayList<>();
        for (Session s : cinemaSessions) {
            if (s.tid == tid)
                res.add(s);
        }
        return res;
    }

    public List<Session> getSessionsOfDate(int cid , LocalDate date) throws SQLCommandException{

            List<Session> sessions = getSessionsFromCinema(cid);
            List<Session> res = new ArrayList<>();
            for(Session s : sessions){
                if(s.localDate.isEqual(date))
                    res.add(s);
            }

            return res;
    }


    public List<Session> fromMovie(int mid) throws SQLCommandException
    {
        try {

            PreparedStatement st = c.prepareStatement("Select DISTINCT  * from Sessions where " + Mid + "= ?");
            st.setInt(1, mid);

            ResultSet rs = st.executeQuery();
            List<Session> res = new ArrayList<>();
            while (rs.next()) {

                Timestamp dateTime = rs.getTimestamp("date_time");
                LocalDate localDate = dateTime.toLocalDateTime().toLocalDate();
                LocalTime localTime = dateTime.toLocalDateTime().toLocalTime();


                Session s = new Session(rs.getInt(sid), rs.getInt(cid), rs.getInt(tid),
                        mid, localDate, localTime);


                res.add(s);
            }

            return res;
        }catch (SQLException e){
            throw new SQLCommandException(e.getCause());
        }
    }

    public List<Session> getFromMovieDate(int mid, LocalDate date) throws SQLCommandException {
        List<Session> all = fromMovie(mid);
        List<Session> res = new ArrayList<>();

        for(Session s : all)
        {
            if (s.localDate.isEqual(date))
                res.add(s);
        }


        return res;
    }

}


