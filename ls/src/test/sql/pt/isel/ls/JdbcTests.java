package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.junit.*;


import static org.junit.Assert.*;
import static pt.isel.ls.utils.SqlConnection.*;

import java.sql.*;

public class JdbcTests {

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

    /**
     * Insert a student in Student table
     *
     * @param c      current connection
     * @param number student id
     * @param name   student name
     * @throws SQLException When error happens
     */
    private static void insertStudent(Connection c, int number, String name) throws SQLException {

        PreparedStatement st = c.prepareStatement("Insert into Student values (?, ?)");
        st.setInt(1, number);
        st.setString(2, name);
        st.executeUpdate();
        st.close();

    }



    @Test
    public void can_insert_new_student() throws SQLException {
        //Arrange
        try(Connection con = ds.getConnection()) {
            con.setAutoCommit(false);

            insertStudent(con, 1, "A");
            insertStudent(con, 2, "B");

            PreparedStatement st = con.prepareStatement("Select Count(*) from Student");
            PreparedStatement st1 = con.prepareStatement("Select name from Student where id = ?");

            String test_name = "Gonçalo";
            int id_test = 42225;
            int count;

            //Act
            ResultSet rs = st.executeQuery();
            rs.next();
            count = rs.getInt(1);

            insertStudent(con, id_test, test_name); //Insert a new Student
            rs = st.executeQuery();
            rs.next();

            st1.setInt(1, id_test);
            ResultSet rs1 = st1.executeQuery();
            rs1.next();

            //Assert
            assertEquals(rs.getInt(1), count + 1);
            assertEquals(test_name , rs1.getString(1));


            //Rollback to initial stage
            con.rollback();
        }
    }

    @Test
    public void can_update_a_student() throws SQLException {

        //Arrange
        try (Connection con = ds.getConnection()){
            con.setAutoCommit(false);

            insertStudent(con, 42225, "Gonçalo");

            String test_name = "Gonçalo";
            int id_test = 42225;
            PreparedStatement st = con.prepareStatement("UPDATE Student set id = -1 where id = ?");
            PreparedStatement st1 = con.prepareStatement("Select id from Student where name = ?");

            //Act
            st.setInt(1,id_test);
            st.executeUpdate();

            st1.setString(1,test_name);
            ResultSet rs = st1.executeQuery();
            rs.next();

            //Assert
            assertEquals(rs.getInt(1), -1);

            //Rollback to initial stage
            con.rollback();
        }
    }



    @Test
    public void select_student() throws SQLException {
        //Arrange
        try (Connection con = ds.getConnection()) {
            con.setAutoCommit(false);


            insertStudent(con, 1, "A");
            insertStudent(con, 2, "B");
            insertStudent(con, 3, "C");

            String test_name = "Gonçalo";

            PreparedStatement st = con.prepareStatement("Select Count(*) from Student where id > 1");
            PreparedStatement st1 = con.prepareStatement("Select id from Student where name = ?");

            //Act
            ResultSet rs = st.executeQuery();
            rs.next();

            st1.setString(1, test_name);
            ResultSet rs1 = st1.executeQuery();


            //Assert
            assertEquals(rs.getInt(1), 2);
            assertFalse(rs1.next());

            //Rollback to initial stage
            con.rollback();
        }


    }
    @Test
    public void delete_student() throws SQLException {
        //Arrange
        try (Connection con = ds.getConnection()) {
            con.setAutoCommit(false);

            insertStudent(con, 1, "A");
            insertStudent(con, 2, "B");
            insertStudent(con, 3, "C");

            String test_name = "A";

            PreparedStatement st = con.prepareStatement("Select Count(*) from Student");
            PreparedStatement st1 = con.prepareStatement("Delete from Student where name = ?");
            PreparedStatement st2 = con.prepareStatement("Select id from Student where name = ?");


            //Act
            ResultSet rs = st.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            st1.setString(1, test_name);
            st1.executeUpdate();
            rs = st.executeQuery();
            rs.next();

            st2.setString(1, test_name);
            ResultSet rs1 = st2.executeQuery();

            //Assert
            assertEquals(rs.getInt(1), count - 1);
            assertFalse(rs1.next());

            //Rollback to initial stage
            con.rollback();

        }
    }
}
