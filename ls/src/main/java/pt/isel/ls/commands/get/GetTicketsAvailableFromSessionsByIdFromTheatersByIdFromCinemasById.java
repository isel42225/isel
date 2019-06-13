package pt.isel.ls.commands.get;

import pt.isel.ls.exceptions.SQLCommandException;
import pt.isel.ls.mappers.sql.SessionSQLMapper;
import pt.isel.ls.mappers.sql.TheaterSQLMapper;
import pt.isel.ls.model.Theater;
import pt.isel.ls.result.IntegerResult;
import pt.isel.ls.utils.SqlConnectionManager;


import java.sql.Connection;
import java.util.HashMap;

public class GetTicketsAvailableFromSessionsByIdFromTheatersByIdFromCinemasById
        extends Get {


    private final String description = "GET /cinemas/{cid}/theaters/{tid}/sessions/{sid}/tickets/available" +
            "-> returns the number of available tickets for a session";

    @Override
    public IntegerResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws SQLCommandException {
        Connection c = manager.get();
        TheaterSQLMapper mapper = new TheaterSQLMapper(c);
        int tid = Integer.parseInt(parameters.get("{tid}"));
        Theater th = mapper.selectById(tid);
        return new IntegerResult(th.availableSeats);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
