package pt.isel.ls.result;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Theater;
import pt.isel.ls.model.Ticket;

import java.util.List;

public class TicketListResult implements Result {
    private final Cinema cinema;
    private final Theater theater;
    private final List<Ticket> tickets;

    public TicketListResult(Cinema cinema, Theater theater, List<Ticket> tickets) {
        this.cinema = cinema;
        this.theater = theater;
        this.tickets = tickets;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public Theater getTheater() {
        return theater;
    }
}
