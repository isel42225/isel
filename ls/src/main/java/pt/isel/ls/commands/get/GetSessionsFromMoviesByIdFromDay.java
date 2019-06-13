package pt.isel.ls.commands.get;

import pt.isel.ls.mappers.sql.CinemaSQLMapper;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.model.Session;
import pt.isel.ls.exceptions.SQLCommandException;
import pt.isel.ls.mappers.sql.SessionSQLMapper;
import pt.isel.ls.result.SessionListResult;
import pt.isel.ls.utils.SqlConnectionManager;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

public class GetSessionsFromMoviesByIdFromDay extends Get {

    private final String description = "GET /movies/{mid}/sessions/date/{dmy} -> return list of sessions for the movie" +
            "mid in the date dmy with option parameters";

    @Override
    public SessionListResult execute(HashMap<String, String> parameters, SqlConnectionManager manager) throws SQLCommandException {
        Connection c = manager.get();
        SessionSQLMapper mapper = new SessionSQLMapper(c);
        CinemaSQLMapper cinMap = new CinemaSQLMapper(c);

        int mid = Integer.parseInt(parameters.get("{mid}"));

        LocalDate date = LocalDate.parse(parameters.get("{dmy}"));

        List<Session> ret = mapper.getFromMovieDate(mid, date);

        if(parameters.containsKey("city")) {

            final String query = parameters.get("city") ;

            if (query != null) {

                ret = filter(ret, s -> {
                    Cinema cin = cinMap.selectById(s.cid);
                    return cin.city.equalsIgnoreCase(query);
                });
            }
        }
        return new SessionListResult(ret);
    }

    private List<Session> filter(List<Session> src, Predicate<Session> pred)
    {
        List<Session> ret = new ArrayList<>();
        for(Session s : src )
        {
            if(pred.test(s))
                ret.add(s);
        }
        return ret;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
