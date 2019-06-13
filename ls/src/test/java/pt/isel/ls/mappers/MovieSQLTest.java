package pt.isel.ls.mappers;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.mappers.sql.MovieSQLMapper;
import pt.isel.ls.model.Movie;
import pt.isel.ls.utils.SqlConnectionManager;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MovieSQLTest {

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
    public void testMovieSelectAll()
    {
        MovieSQLMapper mapper = new MovieSQLMapper(manager.get());

        List<Movie> res = mapper.selectAll();
        int count = 0;

        for(Movie m : res)
        {
            System.out.println(m);
            ++count;
        }

        assertEquals(res.size() , count);
    }

    @Test
    public void testMovieSelectById()
    {
        MovieSQLMapper mapper = new MovieSQLMapper(manager.get());

        Movie res = mapper.selectById(2);

        assertEquals(2, res.mid);
        assertEquals("M2", res.title);
        assertEquals(2001 , res.releaseYear);
        assertEquals(120, res.duration);
    }

    @Test
    public void testMovieInsertAndDelete()
    {
        MovieSQLMapper mapper = new MovieSQLMapper(manager.get());

        Movie ins = new Movie("Star Wars" , 1977, 121 );

        manager.beginTransaction();

        int mid = mapper.insert(ins);

        Movie actual = mapper.selectById(mid);

        assertEquals(mid , actual.mid);
        assertEquals("Star Wars", actual.title );
        assertEquals(1977 , actual.releaseYear);
        assertEquals(121 , actual.duration);

        assertEquals(false , mapper.delete(actual)); //false because delete is an update to table

        manager.endTransaction();
    }

    @Test
    public void testMovieUpdate()
    {
        MovieSQLMapper mapper = new MovieSQLMapper(manager.get());

        Movie original = mapper.selectById(1);

        Movie modified = new Movie(original.mid , "Happiness", 1998, 134);

        manager.beginTransaction();

        mapper.update(modified);

        Movie actual = mapper.selectById(1);

        assertEquals(modified.title , actual.title);
        assertEquals(modified.duration, actual.duration);
        assertEquals(modified.releaseYear, actual.releaseYear);

        mapper.update(original);

        actual = mapper.selectById(1);

        assertEquals("M1", actual.title);
        assertEquals(90 , actual.duration);
        assertEquals(2000 , actual.releaseYear);

        manager.endTransaction();

    }
}
