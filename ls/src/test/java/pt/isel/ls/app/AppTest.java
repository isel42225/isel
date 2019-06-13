package pt.isel.ls.app;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.junit.BeforeClass;
import org.junit.Test;

import static java.util.Arrays.asList;
import static pt.isel.ls.utils.SqlConnectionManager.*;



public class AppTest {

    private static SQLServerDataSource ds = null;

    @BeforeClass
    public static void openCon()
    {
        ds = new SQLServerDataSource();
        ds.setUser(USER);
        ds.setPassword(PASS);
        ds.setDatabaseName(DB_NAME);
        ds.setServerName(DB_SERVER);

    }


    @Test
    public void  optionPrint(){

        App.main(new String[]{ "OPTION", "/"});


    }

}
