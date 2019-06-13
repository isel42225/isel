package pt.isel.ls.result;

import pt.isel.ls.model.Session;
import pt.isel.ls.model.Ticket;

public class TicketResult implements Result {
    private final Ticket tk;
    private final Session session;

    public TicketResult(Ticket tk, Session session) {
        this.tk = tk;
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public Ticket getTicket() {
        return tk;
    }
}
