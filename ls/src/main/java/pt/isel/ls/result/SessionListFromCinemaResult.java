package pt.isel.ls.result;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Session;

import java.util.List;

public class SessionListFromCinemaResult implements Result {
    private final Cinema cinema;
    private final List<Session> sessions;

    public SessionListFromCinemaResult(Cinema cinema, List<Session> sessions) {
        this.cinema = cinema;
        this.sessions = sessions;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public List<Session> getSessions() {
        return sessions;
    }
}
