package pt.isel.ls.commands;

import pt.isel.ls.model.Movie;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.mappers.sql.MovieSQLMapper;
import pt.isel.ls.result.IntegerResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.HashMap;

public class PostMovies implements Command {

    private final String description = "POST /movies -> creates a new movie, given the parameters :" +
            " title= & releaseYear= & duration= ";

    @Override
    public IntegerResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException {
        Connection c = manager.get();
        MovieSQLMapper mapper = new MovieSQLMapper(c);
        String title = parameters.get("title");
        int releaseYear = Integer.parseInt(parameters.get("releaseYear"));
        int duration = Integer.parseInt(parameters.get("duration"));
        Movie movie = new Movie(title , releaseYear , duration);
        int mid = mapper.insert(movie);
        System.out.println("Mid = " + mid);
        return new IntegerResult(mid);
    }

    public String getDescription(){
        return description;
    }
}
