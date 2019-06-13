package pt.isel.ls.result;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;

import java.util.List;

public class MovieResult implements Result {

    private final Movie mov;
    private final List<Cinema> cinemas;

    public MovieResult(Movie mov, List<Cinema> cinemas) {
        this.mov = mov;
        this.cinemas = cinemas;
    }

    public Movie getMov()
    {
        return mov;
    }

    public List<Cinema> getCinemas() {
        return cinemas;
    }
}

