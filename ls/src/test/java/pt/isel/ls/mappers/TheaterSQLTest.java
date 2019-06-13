package pt.isel.ls.mappers;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.mappers.sql.TheaterSQLMapper;
import pt.isel.ls.model.Theater;
import pt.isel.ls.utils.SqlConnectionManager;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TheaterSQLTest {

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
    public void testTheaterSelectAll()
    {
        TheaterSQLMapper mapper = new TheaterSQLMapper(manager.get());

        List<Theater> res = mapper.selectAll();
        int count = 0;
        for(Theater t : res)
        {
            System.out.println(t);
            ++count;
        }

        assertEquals(res.size(), count);
    }

    @Test
    public void testTheaterSelectById()
    {
        TheaterSQLMapper mapper = new TheaterSQLMapper(manager.get());

        Theater t = mapper.selectById(1);

        assertEquals(1, t.tid);
        assertEquals("T1C1" ,t.name);
        assertEquals(1 , t.cid);
        assertEquals(500 , t.availableSeats);
        assertEquals(50 ,t.rows);
        assertEquals(10 , t.seatsPerRow);
    }

    @Test
    public void testTheaterInsertAndDelete()
    {
        TheaterSQLMapper mapper = new TheaterSQLMapper(manager.get());

        Theater ins = new Theater(2 , "Sul", 20 , 10 , 2);

        manager.beginTransaction();

        int tid = mapper.insert(ins);

        Theater actual = mapper.selectById(tid);

        assertEquals(tid, actual.tid);
        assertEquals(ins.cid , actual.cid);
        assertEquals(ins.name , actual.name);
        assertEquals(ins.availableSeats, actual.availableSeats);
        assertEquals(ins.rows , actual.rows);
        assertEquals(ins.seatsPerRow, actual.seatsPerRow);

        assertEquals(false , mapper.delete(actual)); //false because delete is an update to table

        manager.endTransaction();
    }

    @Test
    public void testTheaterUpdate()
    {
        TheaterSQLMapper mapper = new TheaterSQLMapper(manager.get());

        Theater original = mapper.selectById(3);

        Theater modified = new Theater(original.tid ,1, "Norte", 200, 20 ,10);

        manager.beginTransaction();

        mapper.update(modified);

        Theater actual = mapper.selectById(3);

        assertEquals(modified.cid, actual.cid);
        assertEquals(modified.name , actual.name);
        assertEquals(modified.availableSeats, actual.availableSeats);
        assertEquals(modified.rows , actual.rows);
        assertEquals(modified.seatsPerRow, actual.seatsPerRow);

        mapper.update(original);

        actual = mapper.selectById(3);

        assertEquals(2 , actual.cid);
        assertEquals("T1C2", actual.name);
        assertEquals(300 , actual.availableSeats);
        assertEquals(10 , actual.seatsPerRow);
        assertEquals(30 , actual.rows);

        manager.endTransaction();

    }

    @Test
    public void testTheatersOfCinema()
    {
        TheaterSQLMapper mapper = new TheaterSQLMapper(manager.get());

        List<Theater> res = mapper.theathersOfCinema(1);
        int count = 0;
        for(Theater t : res){
            assertEquals(t.cid , 1);
            ++count;
        }

        assertEquals(res.size() , count);
    }
}
