package pt.isel.ls.result;

import pt.isel.ls.model.Cinema;

import java.util.List;

public class CinemaListResult implements Result {
    private final List<Cinema> cinemas;

    public CinemaListResult(List<Cinema> cinemas) {
        this.cinemas = cinemas;
    }

    public List<Cinema> getCinemas() {
        return cinemas;
    }

    @Override
    public String toString() {
        return cinemas.toString();
    }
}
