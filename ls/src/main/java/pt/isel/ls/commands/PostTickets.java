package pt.isel.ls.commands;

import pt.isel.ls.model.Session;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.exceptions.SQLCommandException;
import pt.isel.ls.mappers.sql.SessionSQLMapper;
import pt.isel.ls.mappers.sql.TicketSQLMapper;
import pt.isel.ls.result.IntegerResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.HashMap;

public class PostTickets implements Command {

    private final String description = "POST /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets" +
            "-> creates a new ticket given the following parameters";

    @Override
    public IntegerResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws SQLCommandException {
        Connection c = manager.get();

        TicketSQLMapper mapper = new TicketSQLMapper(c);

        int sid = Integer.parseInt(parameters.get("{sid}"));
        int seat = Integer.parseInt(parameters.get("seat"));
        String row = parameters.get("row");

        Ticket tk = new Ticket(sid,seat ,row );
        int tkid = mapper.insert(tk);
        System.out.println(tk);
        return new IntegerResult(tkid);

    }

    @Override
    public String getDescription() {
        return description;
    }
}
