package pt.isel.ls.commands;

import pt.isel.ls.model.Ticket;
import pt.isel.ls.exceptions.SQLCommandException;
import pt.isel.ls.mappers.sql.TicketSQLMapper;
import pt.isel.ls.result.IntegerResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.HashMap;

public class DeleteTickets implements Command {

    private final String description = "DELETE /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets -> " +
            "delete ticket for the session identified by {sid} from theater {tid} from cinema {cid}";


    @Override
    public IntegerResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws SQLCommandException {
        Connection c = manager.get();
        TicketSQLMapper ticketSQLMapper = new TicketSQLMapper(c);
        String seat = parameters.get("tkid");
        int sid = Integer.parseInt(parameters.get("{sid}"));
        Ticket t = ticketSQLMapper.getFromSeat(sid, seat);
        return new IntegerResult(ticketSQLMapper.delete(t) ? t.getTkid() : -1);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
