package pt.isel.ls.commands.get;

import pt.isel.ls.model.Movie;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.mappers.sql.MovieSQLMapper;
import pt.isel.ls.result.MovieListResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class GetMovies extends Get {

    private final String description = "GET /movies -> return a list of all movies ";


    @Override
    public MovieListResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException {
        Connection c = manager.get();
        MovieSQLMapper mapper = new MovieSQLMapper(c);
        List<Movie> movies = mapper.selectAll();
        return new MovieListResult(movies);
    }

    public String getDescription(){
        return description;
    }

}
