package pt.isel.ls.commands;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.mappers.sql.CinemaSQLMapper;
import pt.isel.ls.result.IntegerResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.HashMap;

public class PostCinemas implements Command {

    private final String description = "POST /cinemas -> creates a new cinema, given the parameters :" +
            " name= & city= ";

    @Override
    public IntegerResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException {
        Connection c = manager.get();
        CinemaSQLMapper mapper = new CinemaSQLMapper(c);
        String name = parameters.get("name");
        String city = parameters.get("city");

        Cinema cinema = new Cinema(name, city);
        int cid = mapper.insert(cinema);
        System.out.println("Cid = " + cid);
        return new IntegerResult(cid);

    }

    public String getDescription(){
        return description;
    }
}
