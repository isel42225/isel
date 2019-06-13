package pt.isel.ls.commands.get;

import pt.isel.ls.mappers.sql.*;
import pt.isel.ls.model.*;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.result.SessionResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class GetSessionsByIdFromCinemasById extends Get  {

    private final String description = "GET /cinemas/{cid}/sessions/{sid} -> return information for the session identified by {sid} from cinema {cid}";

        @Override
        public SessionResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException {
            Connection c = manager.get();
            SessionSQLMapper mapper = new SessionSQLMapper(c);
            TicketSQLMapper tkMap = new TicketSQLMapper(c);
            CinemaSQLMapper cinMap = new CinemaSQLMapper(c);
            TheaterSQLMapper tMap = new TheaterSQLMapper(c);
            MovieSQLMapper movMap = new MovieSQLMapper(c);

            int sid = Integer.parseInt(parameters.get("{sid}"));
            int cid = Integer.parseInt(parameters.get("{cid}"));

            Cinema cin = cinMap.selectById(cid);
            Session session = mapper.selectById(sid);
            Theater th = tMap.selectById(session.tid);
            Movie mov = movMap.selectById(session.mid);
            List<Ticket> tickets = tkMap.getFromSession(sid);

            return new SessionResult(session ,th , cin , mov, tickets);
        }

    @Override
    public String getDescription() {
        return description;
    }
}

