package pt.isel.ls.commands.get;

import pt.isel.ls.mappers.sql.MovieSQLMapper;
import pt.isel.ls.result.CinemaResult;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.mappers.sql.CinemaSQLMapper;
import pt.isel.ls.mappers.sql.SessionSQLMapper;
import pt.isel.ls.mappers.sql.TheaterSQLMapper;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetCinemasById extends Get{

    private final String description = "GET /cinemas/{cid} -> return information for the Cinema identified " +
            "by {cid}";


    @Override
    public CinemaResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException {
        Connection c = manager.get();
        CinemaSQLMapper mapper = new CinemaSQLMapper(c);
        TheaterSQLMapper tMap = new TheaterSQLMapper(c);
        SessionSQLMapper sMap = new SessionSQLMapper(c);
        MovieSQLMapper movMap = new MovieSQLMapper(c);

        int cid = Integer.parseInt(parameters.get("{cid}"));

        Cinema cinema = mapper.selectById(cid);
        List<Theater> theaters = tMap.theathersOfCinema(cid);
        List<Movie> movs = new ArrayList<>();

        List<Session> sessions = sMap.getSessionsFromCinema(cid);

        List<Integer> ids = new ArrayList<>();
        for(Session s : sessions)
        {
            Movie m = movMap.selectById(s.mid);
            if(!ids.contains(m.mid)) {
                ids.add(m.mid);
                movs.add(m);
            }
        }

        return new CinemaResult(cinema , theaters , movs);
    }



    public String getDescription(){
        return description;
    }
}
