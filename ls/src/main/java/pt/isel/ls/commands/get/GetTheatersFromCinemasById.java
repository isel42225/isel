package pt.isel.ls.commands.get;

import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Theater;
import pt.isel.ls.exceptions.CommandException;
import pt.isel.ls.mappers.sql.CinemaSQLMapper;
import pt.isel.ls.mappers.sql.TheaterSQLMapper;
import pt.isel.ls.result.TheaterListResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class GetTheatersFromCinemasById extends Get {

    private final String description = "GET /cinemas/{cid}/theaters -> returns the list of all theaters" +
            " of the cinema identified by {cid}";

    @Override
    public TheaterListResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws CommandException {
        Connection c = manager.get();
        CinemaSQLMapper cinMap = new CinemaSQLMapper(c);
        TheaterSQLMapper mapper = new TheaterSQLMapper(c);

        int cid = Integer.parseInt(parameters.get("{cid}"));

        Cinema cin = cinMap.selectById(cid);
        List<Theater> theaters = mapper.theathersOfCinema(cid);

       /* Consumer<Theaters> consumer = t -> System.out.println(t.tid);
        theaters.forEach(consumer);*/
        return new TheaterListResult(cin , theaters);

    }

    public String getDescription(){
        return description;
    }


}
