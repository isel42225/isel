package pt.isel.ls.commands.get;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.mappers.sql.CinemaSQLMapper;
import pt.isel.ls.mappers.sql.SessionSQLMapper;
import pt.isel.ls.mappers.sql.TheaterSQLMapper;
import pt.isel.ls.result.TheaterResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class GetTheatersByIdFromCinemasById extends Get {

    private final String description = "GET /cinemas/{cid}/theaters/{tid} -> returns information for the" +
            " theaters identified by {tid} from the cinema identified by {cid} ";


    @Override
    public TheaterResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException {
        Connection c = manager.get();
        CinemaSQLMapper cinMap  = new CinemaSQLMapper(c);
        TheaterSQLMapper mapper = new TheaterSQLMapper(c);
        SessionSQLMapper sMap   = new SessionSQLMapper(c);

        int cid = Integer.parseInt(parameters.get("{cid}"));
        int tid = Integer.parseInt(parameters.get("{tid}"));

        Cinema cin = cinMap.selectById(cid);
        Theater theater = mapper.selectById(tid);

        List<Session> sessions = sMap.getSessionsOfTheaterFromCinema(cid, tid);

        return new TheaterResult(theater, cin, sessions);

    }

    public String getDescription(){
        return description;
    }
}
