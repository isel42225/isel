package pt.isel.ls.commands;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.mappers.sql.CinemaSQLMapper;
import pt.isel.ls.mappers.sql.MovieSQLMapper;
import pt.isel.ls.mappers.sql.SessionSQLMapper;
import pt.isel.ls.mappers.sql.TheaterSQLMapper;
import pt.isel.ls.result.IntegerResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

public class PostSessions implements Command{

    private final String description = "POST /cinemas/{cid}/theaters/{tid}/sessions -> creates a new session," +
            " given the parameters: date= & {mid}= ";

    @Override
    public IntegerResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException {
        Connection c = manager.get();

        SessionSQLMapper mapper = new SessionSQLMapper(c);

        int cid = Integer.parseInt(parameters.get("{cid}"));
        int tid = Integer.parseInt(parameters.get("{tid}"));
        int mid = Integer.parseInt(parameters.get("mid"));
        String date = parameters.get("date").replaceFirst(" ","T");
        LocalDateTime localDateTime = LocalDateTime.parse(date);
        LocalDate localDate = localDateTime.toLocalDate();
        LocalTime localTime = localDateTime.toLocalTime();


        Session session = new Session(cid , tid , mid ,localDate , localTime );
        int sid = mapper.insert(session);
        System.out.println("Sid = " + sid);
        return new IntegerResult(sid);

    }

    public String getDescription(){
        return description;
    }
}
