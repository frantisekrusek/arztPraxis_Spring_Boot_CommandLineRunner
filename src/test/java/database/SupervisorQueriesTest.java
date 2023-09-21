package database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class SupervisorQueriesTest {

    private MySQL_Connector connector;
    private Connection connection;
    private Statement stmt;
    private PreparedStatement ps;
    private ResultSet rs;
    private SupervisorQueries supervisorQueries;

    @BeforeEach
    void connect(){
        this.connector = new MySQL_Connector();
        this.connector.setUrl("jdbc:mysql://localhost:3306/arztpraxistest?user=root&password=secret");

        connection = connector.connect();
        stmt = null;
        ps = null;
        rs = null;
    }

    void disconnect(){
        connector.close(stmt);
        connector.close(rs);
        connector.close(ps);
        connector.close(connection);
    }

    void setTo_31_05_2020__0h(){
        System.out.println("LOG: enter SupervisorQueriesTEST.setTo_31_05_2020__0h()");
        try {
            Timestamp _31_05_2020__0h = Timestamp.valueOf("2020-05-31 00:00:00");
            ps = connection.prepareStatement("UPDATE  supervisor SET lastUpdate = ? WHERE id = ?");
                ps.setTimestamp(1, _31_05_2020__0h);
                ps.setInt(2,1);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
            this.disconnect();
            }
        System.out.println("LOG: leave SupervisorQueriesTEST.setTo_31_05_2020__0h()");
    }//end setTo_31_05_2020__0h()

    void setTo_01_01_1900__0h(){
        System.out.println("LOG: enter SupervisorQueriesTEST.setTo_01_01_1900__0h()");
        try {
            Timestamp _01_01_1900__0h = Timestamp.valueOf("1900-01-01 00:00:00");
            ps = connection.prepareStatement("UPDATE  supervisor SET lastUpdate = ? WHERE id = ?");
            ps.setTimestamp(1, _01_01_1900__0h);
            ps.setInt(2,1);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
        }
        System.out.println("LOG: leave SupervisorQueriesTEST.setTo_01_01_1900__0h()");
    }//end setTo_31_05_2020__0h()

    void truncate_Table(){
        System.out.println("LOG: enter SupervisorQueriesTEST.truncate_Table()");
        try {
            stmt = connection.createStatement();
            stmt.execute("TRUNCATE TABLE supervisor");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        System.out.println("LOG: leave SupervisorQueriesTEST.truncate_Table");
    }//end truncate_Table()


    void initializeLastUpdate_To_CurrentTimeTEST() {
        System.out.println("LOG: enter SupervisorQueriesTEST.initializeLastUpdate_To_CurrentTimeTEST()");
        this.truncate_Table();
        this.connect();
        try {
            stmt = connection.createStatement();
            stmt.execute("insert into supervisor (lastupdate,id) values (CURRENT_TIMESTAMP, 1)");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
        }
        System.out.println("LOG: leave SupervisorQueriesTEST.initializeLastUpdate_To_CurrentTimeTEST()");

    }//End initializeLastUpdate_To_CurrentTimeTEST

    @Test
    void fetchLastUpdateTEST() {
        System.out.println("LOG: enter SupervisorQueriesTEST.fetchLastUpdateTEST()");
        this.setTo_31_05_2020__0h();
        //connector IS set to db 'arztpraxistest'
        //
        supervisorQueries = new SupervisorQueries();
        supervisorQueries.getConnector().setUrl("jdbc:mysql://localhost:3306/arztpraxistest?user=root&password=secret");
        //core
        Instant actualInstant = supervisorQueries.fetchLastUpdate();
        //Timestamp _31_05_2020__0h
        ZonedDateTime zdt = ZonedDateTime.of(2020,05, 31,0,0,0,0,ZoneId.of("Europe/Vienna"));
        Instant expectedInstant = zdt.toInstant();

        assertEquals(expectedInstant, actualInstant);
    }//end fetchLastUpdateTEST()

    @Test
    void initializeLastUpdate_TEST(){
        this.truncate_Table();
        supervisorQueries = new SupervisorQueries();
        supervisorQueries.getConnector().setUrl("jdbc:mysql://localhost:3306/arztpraxistest?user=root&password=secret");

        //https://stackoverflow.com/questions/63641201/mysql-timestamp-error-data-truncation-incorrect-datetime-value:
        // TIMESTAMP has a range of '1970-01-01 00:00:01' UTC to '2038-01-19 03:14:07' UTC; For "bigger" Dates switch to DATETIME
        LocalDate ld = LocalDate.of(1974,1, 1);
        LocalTime lt = LocalTime.of(0,0, 0,0);
        ZoneId zoneId = ZoneId.of("Europe/Vienna");
        //core
        supervisorQueries.initializeLastUpdate(ZonedDateTime.of(ld, lt, zoneId));
        Instant actual = supervisorQueries.fetchLastUpdate();
        Instant expected = ZonedDateTime.of(ld, lt, zoneId).toInstant();
        assertEquals(expected, actual, "lastUpdate from repo does not match");

        this.disconnect();
    }//end initializeLastUpdate_TEST

    @Test
    void update_LastUpdateTEST() {
        supervisorQueries = new SupervisorQueries();
        supervisorQueries.getConnector().setUrl("jdbc:mysql://localhost:3306/arztpraxistest?user=root&password=secret");
        this.setTo_31_05_2020__0h();
        this.connect();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2015, 1,1,
                14,0,0,0, ZoneId.of("Europe/Vienna"));
        //core
        supervisorQueries.update_LastUpdate(zonedDateTime);
        Instant expected = zonedDateTime.toInstant();
        Instant actual = supervisorQueries.fetchLastUpdate();

        this.disconnect();
    }//end update_LastUpdateTEST()

}//end class