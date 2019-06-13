package pt.isel.ls.commands.get;

import pt.isel.ls.result.CinemaListResult;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.mappers.sql.CinemaSQLMapper;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class GetCinemas extends Get{

    private final String description = "GET /cinemas -> return a list of all Cinemas";

    @Override
    public CinemaListResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException {
        Connection c = manager.get();
        CinemaSQLMapper mapper = new CinemaSQLMapper(c);
        List<Cinema> cinemas = mapper.selectAll();

        return new CinemaListResult(cinemas);
    }

    public String getDescription(){
        return description;
    }
}
