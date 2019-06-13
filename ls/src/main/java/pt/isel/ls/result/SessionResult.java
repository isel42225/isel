package pt.isel.ls.result;

import pt.isel.ls.model.*;

import java.util.List;

public class SessionResult implements Result {
    private final Session session;
    private final Theater theater;
    private final Cinema cin;
    private final Movie mov;
    private final List<Ticket> tickets;

    public SessionResult(Session session, Theater theater, Cinema cin, Movie mov, List<Ticket> tickets) {
        this.session = session;
        this.theater = theater;
        this.cin = cin;
        this.mov = mov;
        this.tickets = tickets;
    }

    public Cinema getCin()
    {
        return cin;
    }

    public Theater getTheater() {
        return theater;
    }

    public Movie getMov() {
        return mov;
    }

    public Session getSession()
    {
        return session;
    }

    public List<Ticket> getTickets()
    {
        return tickets;
    }
}
