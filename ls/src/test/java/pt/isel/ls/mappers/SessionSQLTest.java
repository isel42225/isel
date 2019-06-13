package pt.isel.ls.mappers;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.exceptions.SQLCommandException;
import pt.isel.ls.mappers.sql.SessionSQLMapper;
import pt.isel.ls.model.Session;
import pt.isel.ls.model.Theater;
import pt.isel.ls.utils.SqlConnectionManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SessionSQLTest {
    private static SqlConnectionManager manager;
    private static final String DB = System.getenv("LS_TEST_DATABASE");

    @BeforeClass
    public static void initManager()
    {
        manager = new SqlConnectionManager(DB);
    }

    @AfterClass
    public static void closeCon()
    {
        manager.close();
    }

    @Test
    public void testSessionSelectAll()
    {
        SessionSQLMapper mapper = new SessionSQLMapper(manager.get());

        List<Session> res = mapper.selectAll();

        int count = 0;

        for(Session s : res)
        {
            System.out.println(s);
            ++count;
        }

        assertEquals(res.size(), count);
    }

    @Test
    public void testSessionSelectById()
    {
        SessionSQLMapper mapper = new SessionSQLMapper(manager.get());

        Session s = mapper.selectById(1);

        assertEquals(1 , s.cid);
        assertEquals(1 , s.tid);
        assertEquals(1, s.mid);
        assertEquals(LocalDate.parse("2018-12-03"),s.localDate);
        assertEquals(LocalTime.parse("12:43"), s.localTime);
    }

    @Test
    public void testInsertAndDelete()
    {
        SessionSQLMapper mapper = new SessionSQLMapper(manager.get());

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime time = LocalTime.of(now.getHour(), now.getMinute());


        Session ins = new Session(2, 3, 2 ,today, time);

        manager.beginTransaction();

        int sid = mapper.insert(ins);

        Session actual = mapper.selectById(sid);

        assertEquals(ins.cid ,actual.cid);
        assertEquals(ins.tid , actual.tid);
        assertEquals(ins.mid, actual.mid);
        assertEquals(ins.localDate , actual.localDate);
        assertEquals(ins.localTime, actual.localTime);

        assertEquals(false , mapper.delete(actual)); //false because delete is an update to table

        manager.endTransaction();

    }

    @Test
    public void testSessionUpdate()
    {
        SessionSQLMapper mapper = new SessionSQLMapper(manager.get());

        Session original = mapper.selectById(2);

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime time = LocalTime.of(now.getHour(), now.getMinute());

        Session modified = new Session(original.sid, 2 , 3, 1 ,today, time);

        manager.beginTransaction();

        mapper.update(modified);

        Session actual = mapper.selectById(2);

        assertEquals(modified.cid, actual.cid);
        assertEquals(modified.tid, actual.tid);
        assertEquals(modified.mid, actual.mid);
        assertEquals(modified.localDate, actual.localDate);
        assertEquals(modified.localTime , actual.localTime);

        mapper.update(original);

        actual = mapper.selectById(2);

        assertEquals(1, actual.cid);
        assertEquals(2, actual.tid);
        assertEquals(2, actual.mid);
        assertEquals(original.localDate, actual.localDate);
        assertEquals(original.localTime ,actual.localTime);

        manager.endTransaction();
    }

    @Test(expected = SQLCommandException.class)
    public void shouldOverLap()
    {
        SessionSQLMapper mapper = new SessionSQLMapper(manager.get());

        Session s = mapper.selectById(1);

        Session overlapping = new Session(1, 1, 2 , s.localDate, s.localTime.plusMinutes(60));

        manager.beginTransaction();

        mapper.insert(overlapping);

        manager.endTransaction();
    }

    @Test
    public void shouldNotOverlap()
    {
        SessionSQLMapper mapper = new SessionSQLMapper(manager.get());

        Session s = mapper.selectById(1);

        Session overlapping = new Session(1, 1, 2 , s.localDate, s.localTime.plusMinutes(180));

        manager.beginTransaction();

        int tid = mapper.insert(overlapping);

        Session actual = mapper.selectById(tid);
        assertEquals(false,mapper.delete(actual));

        manager.endTransaction();
    }

    @Test
    public void testTodaySessions()
    {
        SessionSQLMapper mapper = new SessionSQLMapper(manager.get());

        List<Session> res = mapper.getTodaySessionsOfCinema(1);

        assertEquals(0, res.size());

        manager.beginTransaction();

        Session ins = new Session(1, 1, 2, LocalDate.now(), LocalTime.now());

        int sid = mapper.insert(ins);
        res = mapper.getTodaySessionsOfCinema(1);

        assertEquals(1, res.size());

        Session actual = mapper.selectById(sid);
        mapper.delete(actual);

        res = mapper.getTodaySessionsOfCinema(1);

        assertEquals(0 , res.size());

        manager.endTransaction();
    }

    @Test
    public void testSessionsOfDate()
    {
        SessionSQLMapper mapper = new SessionSQLMapper(manager.get());

        LocalDate date = LocalDate.parse("2018-11-23");

        List<Session> res = mapper.getSessionsOfDate(1, date);

        assertEquals(1, res.size());

        res = mapper.getSessionsOfDate(2 , LocalDate.now());

        assertEquals(0 , res.size());

    }
}
