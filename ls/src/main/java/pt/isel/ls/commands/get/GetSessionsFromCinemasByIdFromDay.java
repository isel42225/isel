package pt.isel.ls.commands.get;

import pt.isel.ls.mappers.sql.CinemaSQLMapper;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Session;
import pt.isel.ls.exceptions.SQLCommandException;
import pt.isel.ls.mappers.sql.SessionSQLMapper;
import pt.isel.ls.result.SessionListFromCinemaResult;
import pt.isel.ls.result.SessionListResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class GetSessionsFromCinemasByIdFromDay extends Get
{
    private final String description = "GET /cinema/{cid}/sessions/date/{dmy} -> return list of sessions in cinema" +
            " cid in the date dmy with option parameters";


    @Override
    public SessionListFromCinemaResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws SQLCommandException {
        Connection c = manager.get();
        SessionSQLMapper mapper = new SessionSQLMapper(c);
        CinemaSQLMapper cinMap = new CinemaSQLMapper(c);

        int cid = Integer.parseInt(parameters.get("{cid}"));

        Cinema cin = cinMap.selectById(cid);
        LocalDate date = LocalDate.parse(parameters.get("{dmy}"));
        List<Session> sessions = mapper.getSessionsOfDate(cid, date);

        return new SessionListFromCinemaResult(cin, sessions);
    }

    @Override
    public String getDescription() {
        return description;
    }


}
