package pt.isel.ls.commands.get;

import pt.isel.ls.mappers.sql.CinemaSQLMapper;
import pt.isel.ls.mappers.sql.TheaterSQLMapper;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.exceptions.SQLCommandException;
import pt.isel.ls.mappers.sql.TicketSQLMapper;
import pt.isel.ls.result.TicketListResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class GetTicketsFromSessionsByIdFromTheatersByIdFromCinemasById extends Get{


    private final String description = "GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets " +
            "-> returns a list with all tickets for a session";
    @Override
    public TicketListResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws SQLCommandException {
        Connection c = manager.get();
        TicketSQLMapper ticketSQLMapper = new TicketSQLMapper(c);
        CinemaSQLMapper cinMapper = new CinemaSQLMapper(c);
        TheaterSQLMapper thMapper = new TheaterSQLMapper(c);

        int sid = Integer.parseInt(parameters.get("{sid}"));
        int tid = Integer.parseInt(parameters.get("{tid}"));
        int cid = Integer.parseInt(parameters.get("{cid}"));
        List<Ticket> tk =
                ticketSQLMapper.getFromSession(sid );

        Cinema cin = cinMapper.selectById(cid);
        Theater th = thMapper.selectById(tid);

        return new TicketListResult(cin, th,tk);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
