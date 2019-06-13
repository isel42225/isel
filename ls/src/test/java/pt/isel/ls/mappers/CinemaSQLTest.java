package pt.isel.ls.mappers;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.mappers.sql.CinemaSQLMapper;
import pt.isel.ls.model.Cinema;
import pt.isel.ls.utils.SqlConnectionManager;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CinemaSQLTest {

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
    public void testCinemaSelectAll()
    {
        CinemaSQLMapper mapper = new CinemaSQLMapper(manager.get());

        List<Cinema> res = mapper.selectAll();

        int count = 0;

        for(Cinema c : res)
        {
            System.out.println(c);
            ++count;
        }

        assertEquals(res.size() , count);

    }

    @Test
    public void testCinemaSelectById()
    {
        CinemaSQLMapper mapper = new CinemaSQLMapper(manager.get());

        Cinema res = mapper.selectById(2);

        assertEquals(2, res.cid);
        assertEquals("C2", res.name);
        assertEquals("Porto", res.city);
    }

    @Test
    public void testCinemaInsertAndDelete()
    {
        CinemaSQLMapper mapper = new CinemaSQLMapper(manager.get());
        Cinema ins = new Cinema("King", "Seixal");

        manager.beginTransaction();

        int cid = mapper.insert(ins);

        Cinema actual = mapper.selectById(cid);

        assertEquals(cid, actual.cid);
        assertEquals(ins.name, actual.name);
        assertEquals(ins.city, actual.city);

        assertEquals(false , mapper.delete(actual)); //false because delete is an update to table

        manager.endTransaction();
    }

    @Test
    public void testCinemaUpdate()
    {
        CinemaSQLMapper mapper = new CinemaSQLMapper(manager.get());

        Cinema original = mapper.selectById(1);

        Cinema modified = new Cinema(original.cid , "Place" , "Faro");

        manager.beginTransaction();

        mapper.update(modified);

        Cinema actual = mapper.selectById(1);

        assertEquals(modified.name , actual.name);
        assertEquals(modified.city , actual.city);

        mapper.update(original);

        actual = mapper.selectById(1);

        assertEquals("C1" , actual.name);
        assertEquals("Lisbon" , actual.city);

        manager.endTransaction();
    }
}
