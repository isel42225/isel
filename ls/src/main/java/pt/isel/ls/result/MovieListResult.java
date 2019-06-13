package pt.isel.ls.result;

import pt.isel.ls.model.Movie;

import java.util.List;

public class MovieListResult implements Result {

    private final List<Movie> movies;

    public MovieListResult(List<Movie> movies)
    {
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
