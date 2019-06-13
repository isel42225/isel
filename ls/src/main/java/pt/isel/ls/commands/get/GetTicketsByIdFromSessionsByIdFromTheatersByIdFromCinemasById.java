package pt.isel.ls.commands.get;

import pt.isel.ls.mappers.sql.SessionSQLMapper;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.exceptions.SQLCommandException;
import pt.isel.ls.mappers.sql.TicketSQLMapper;
import pt.isel.ls.result.TicketResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.HashMap;

public class GetTicketsByIdFromSessionsByIdFromTheatersByIdFromCinemasById
        extends Get {

    private final String description = "GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/{tkid}" +
            "-> returns the detailed information of the ticket, including session information";
    @Override
    public TicketResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws SQLCommandException {
        Connection c = manager.get();
        TicketSQLMapper ticketSQLMapper = new TicketSQLMapper(c);
        SessionSQLMapper sMap = new SessionSQLMapper(c);

        int tkid = Integer.parseInt(parameters.get("{tkid}"));

        Ticket tk = ticketSQLMapper.selectById(tkid);
        Session session = sMap.selectById(tk.getSessionId());


        return new TicketResult(tk , session);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
