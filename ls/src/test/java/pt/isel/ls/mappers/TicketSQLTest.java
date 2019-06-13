package pt.isel.ls.mappers;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.mappers.sql.TicketSQLMapper;
import pt.isel.ls.model.Ticket;
import pt.isel.ls.utils.SqlConnectionManager;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TicketSQLTest {

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
    public void testTicketSelectAll()
    {
        TicketSQLMapper mapper = new TicketSQLMapper(manager.get());

        List<Ticket> res = mapper.selectAll();
        int count = 0;

        for(Ticket t : res)
        {
            System.out.println(t);
            ++count;
        }

        assertEquals(res.size(), count);
    }

    @Test
    public void testTicketSelectById()
    {
        TicketSQLMapper mapper = new TicketSQLMapper(manager.get());

        Ticket tk = mapper.selectById(1);

        assertEquals(1, tk.getSessionId());
        assertEquals("A", tk.getRow());
        assertEquals(1, tk.getSeat());
    }

    @Test
    public void testTicketInsertAndDelete()
    {
        TicketSQLMapper mapper = new TicketSQLMapper(manager.get());

        Ticket ins = new Ticket(2 , 1, "A");

        manager.beginTransaction();

        int tkid = mapper.insert(ins);

        Ticket actual = mapper.selectById(tkid);

        assertEquals(ins.getSessionId(), actual.getSessionId());
        assertEquals(ins.getRow(), actual.getRow());
        assertEquals(ins.getSeat(), actual.getSeat());

        assertEquals(false , mapper.delete(actual)); //false because delete is an update to table

        manager.endTransaction();
    }

    @Test
    public void testTicketUpdate()
    {
       TicketSQLMapper mapper = new TicketSQLMapper(manager.get());

       Ticket original = mapper.selectById(2);

       Ticket modified = new Ticket(original.tkid, 3, 1, "B");

       manager.beginTransaction();

       mapper.update(modified);

       Ticket actual = mapper.selectById(2);

       assertEquals(modified.getSessionId(), actual.getSessionId());
       assertEquals(modified.getSeat(), actual.getSeat());
       assertEquals(modified.getRow(), actual.getRow());

       mapper.update(original);

       actual = mapper.selectById(2);

       assertEquals(2, actual.getSessionId());
       assertEquals("B", actual.getRow());
       assertEquals(1, actual.getSeat());

       manager.endTransaction();
    }

    @Test
    public void testTicketOfSession()
    {
        TicketSQLMapper mapper = new TicketSQLMapper(manager.get());

        List<Ticket> res = mapper.getFromSession(1);

        assertEquals(1, res.size());

        manager.beginTransaction();

        Ticket ins = new Ticket(1, 1 , "B");

        int tkid = mapper.insert(ins);

        res = mapper.getFromSession(1);

        assertEquals(2 , res.size());

        Ticket actual = mapper.selectById(tkid);

        mapper.delete(actual);

        manager.endTransaction();

        res = mapper.getFromSession(1);

        assertEquals(1, res.size());
    }

    @Test
    public void testTicketOfSeat()
    {
        TicketSQLMapper mapper = new TicketSQLMapper(manager.get());

        Ticket res = mapper.getFromSeat(3, "A2");

        assertEquals(3 ,res.getSessionId());
        assertEquals("A", res.getRow());
        assertEquals(2, res.getSeat());
    }
}
