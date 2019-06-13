package pt.isel.ls.result;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;

import java.util.List;

public class TheaterResult implements Result {

    private final Theater theater;
    private final Cinema cinema;
    private final List<Session> sessions;

    public TheaterResult(Theater theater, Cinema cinema, List<Session> sessions) {
        this.theater = theater;
        this.cinema = cinema;
        this.sessions = sessions;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public Theater getTheater() {
        return theater;
    }

    public List<Session> getSessions() {
        return sessions;
    }
}
