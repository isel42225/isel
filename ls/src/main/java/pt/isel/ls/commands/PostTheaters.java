package pt.isel.ls.commands;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Theater;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.mappers.sql.CinemaSQLMapper;
import pt.isel.ls.mappers.sql.TheaterSQLMapper;
import pt.isel.ls.result.IntegerResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.HashMap;

public class PostTheaters implements Command{

    private final String description = "POST /cinemas/{cid}/theaters -> add a new theater to a cinema," +
            " given teh parameters: name= & rows= & seats= ";

    @Override
    public IntegerResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException {
        Connection c = manager.get();

        TheaterSQLMapper mapper = new TheaterSQLMapper(c);

        String name = parameters.get("name");
        int cid = Integer.parseInt(parameters.get("{cid}"));
        int rows = Integer.parseInt(parameters.get("rows"));
        int seats_per_row = Integer.parseInt(parameters.get("seats"));
        int seats = rows * seats_per_row;

        Theater theater = new Theater(cid , name , seats , rows ,seats_per_row);

        int tid = mapper.insert(theater);
        System.out.println("Tid = " + tid);
        return new IntegerResult(tid);
    }

    public String getDescription(){
        return description;
    }
}
