package pt.isel.ls.result;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Movie;
import pt.isel.ls.model.Theater;

import java.util.List;
import java.util.Set;

public class CinemaResult implements Result {

    private final Cinema cinema;
    private final List<Theater> theaters;
    private final List<Movie> movies;    //TODO mudar para List mas de maneira
                                        // TODO a que sejam sempre únicos

    public CinemaResult(Cinema cinema, List<Theater> theaters, List<Movie> movies)
    {
        this.cinema = cinema;
        this.theaters = theaters;
        this.movies = movies;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public List<Theater> getTheaters() {
        return theaters;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
