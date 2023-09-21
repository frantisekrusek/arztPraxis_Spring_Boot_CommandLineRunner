package database;

import com.sun.jdi.connect.Connector;
import model.appointment.Template;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OfficeQueriesTest {

    OfficeQueries officeQueries = new OfficeQueries();

    @BeforeEach
    void setUp() {
        System.out.println("LOG OfficeQueriesTest.setUp: Connection established");
        MySQL_Connector mySQL_connector = new MySQL_Connector();
        mySQL_connector.setUrl("jdbc:mysql://localhost:3306/arztpraxistest?user=root&password=secret");
        //3 Tupel=Zeilen in Datenbank einfügen
        Connection connection = mySQL_connector.connect();
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate("INSERT INTO templates (weekday, starttime, active)" +
                    " VALUES ('MONDAY', '08:15', true)");
            stmt.executeUpdate("INSERT INTO templates (weekday, starttime, active)" +
                    " VALUES ('Monday','08:30', true)");
            stmt.executeUpdate("INSERT INTO templates (weekday, starttime, active) " +
                    "VALUES ('Tuesday', '07:00', true)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            mySQL_connector.close(stmt);
            mySQL_connector.close(connection);
            System.out.println("LOG OfficeQueriesTest.setUp: Connection closed");
        }
    }

    @AfterEach
    void tearDown() {
        MySQL_Connector mySQL_connector = new MySQL_Connector();
        mySQL_connector.setUrl("jdbc:mysql://localhost:3306/arztpraxistest?user=root&password=secret");
        Connection connection = mySQL_connector.connect();
        System.out.println("LOG OfficeQueriesTest.tearDown: Connection established");
        System.out.println("LOG tearDown");
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate("TRUNCATE templates");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            mySQL_connector.close(stmt);
            mySQL_connector.close(connection);
            System.out.println("LOG OfficeQueriesTest.tearDown: Connection closed");
        }
    }

    @Test
    void insertSingleTemplateTest() {
        System.out.println("LOG OfficeQueriesTest.insertSingleTemplateTest: enter");

        this.tearDown();
        DayOfWeek friday = DayOfWeek.FRIDAY;
        LocalTime starttime_1045 = LocalTime.of(10,45);
        Boolean active = true;
        Set<Template> templates = new HashSet<>();
        Template template_1 = new Template(friday, starttime_1045, active);
        //core
        officeQueries.getConnector().setUrl("jdbc:mysql://localhost:3306/arztpraxistest?user=root&password=secret");
        officeQueries.insertSingleTemplate(template_1);

        MySQL_Connector connector = officeQueries.getConnector();
        connector.setUrl("jdbc:mysql://localhost:3306/arztpraxistest?user=root&password=secret");
        Connection conn = connector.connect();
        Statement stmt = null;
        ResultSet rs = null;
        Template templateFromDB = new Template(null, null);
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM templates");
            while (rs.next()){
                templateFromDB.setWeekday(DayOfWeek.valueOf(rs.getString(1)));
                templateFromDB.setStartTime(LocalTime.parse(rs.getString(2)));
                templateFromDB.setId(rs.getInt(3));
                templateFromDB.setActive(rs.getBoolean(4));
                System.out.println("LOG insertSingleTemplateTEST " + templateFromDB);

                assertEquals(DayOfWeek.FRIDAY, templateFromDB.getWeekday(),"mismatching Weekday");
                assertEquals(LocalTime.of(10,45), templateFromDB.getStartTime(), "mismatching Startime");
                assertEquals(1, templateFromDB.getId(),"mismatching Id");
                assertEquals(true, templateFromDB.isActive(), "mismatching activity status");
            }

        } catch (SQLException e) {
                    throw new RuntimeException(e);
        } finally {
             connector.close(conn);
             connector.close(stmt);
             connector.close(rs);
            System.out.println("LOG OfficeQueriesTest.insertSingleTemplateTest: leave");
        }
    }//end insertSingleTemplateTest()


    //public Set<DayOfWeek> provideWeekdays(){
    //}
    @ParameterizedTest
    @EnumSource(value = DayOfWeek.class, names = {"MONDAY","TUESDAY" } )
    //@MethodSource("provideWeekdays")
    public void fetchTemplates_By_Weekday_TEST(DayOfWeek dayOfWeek){
        System.out.println("LOG OfficeQueries.fetchTemplates_By_Weekday_TEST enter");
        //core
        officeQueries.getConnector().setUrl("jdbc:mysql://localhost:3306/arztpraxistest?user=root&password=secret");
        Set<Template> actual_templates = officeQueries.fetchTemplates_By_Weekday(dayOfWeek);

        Set<Template> expected_templates = new HashSet<>();
        Template monday_1 = new Template(DayOfWeek.MONDAY, LocalTime.of(8,15), 1, true);
        Template monday_2 = new Template(DayOfWeek.MONDAY, LocalTime.of(8,30), 2, true);
        Template tuesday_1 = new Template(DayOfWeek.TUESDAY, LocalTime.of(7,00), 3, true);
        if (dayOfWeek.equals(DayOfWeek.MONDAY)){
            expected_templates.add(monday_1); expected_templates.add(monday_2);
            assertEquals(2, actual_templates.size(), "count of templates does not match");
            System.out.println("LOG OfficeQueriesTest.fetchTemplates_By_Weekday_TEST: ");
        } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
            expected_templates.add(tuesday_1);
            assertEquals(1, actual_templates.size(),"count of templates does not match");
        }
        assertTrue(expected_templates.containsAll(actual_templates), " Templates from repository are not equal with expected objects");
        System.out.println("LOG OfficeQueries.fetchTemplates_By_Weekday_TEST leave");
    }//end fetchTemplates_By_Weekday_TEST()

    @Test
    void getAllTemplates() {

        //fail("todo");
    }

    @Test
    void getTemplates_ByWeekday() {

       // fail("todo");
    }

}