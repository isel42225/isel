package pt.isel.ls.commands.get;

import pt.isel.ls.mappers.sql.CinemaSQLMapper;
import pt.isel.ls.mappers.sql.SessionSQLMapper;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.mappers.sql.MovieSQLMapper;
import pt.isel.ls.model.Session;
import pt.isel.ls.result.MovieResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetMoviesById extends Get {

    private final String description = "GET /movies/{mid} -> return information for the Movie identified by {mid} ";

    @Override
    public MovieResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException {
        Connection c = manager.get();
        MovieSQLMapper mapper = new MovieSQLMapper(c);
        SessionSQLMapper sMap = new SessionSQLMapper(c);
        CinemaSQLMapper cinMap = new CinemaSQLMapper(c);

        int mid = Integer.parseInt(parameters.get("{mid}"));
        Movie movie = mapper.selectById(mid);
        List<Session> sessions = sMap.fromMovie(mid);

        List<Cinema> cinemas = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for(Session s :sessions)
        {
            Cinema cin = cinMap.selectById(s.cid);
            if(!ids.contains(cin.cid)) {
                ids.add(cin.cid);
                cinemas.add(cin);
            }
        }

        return new MovieResult(movie , cinemas);
    }

    public String getDescription(){
        return description;
    }
}

