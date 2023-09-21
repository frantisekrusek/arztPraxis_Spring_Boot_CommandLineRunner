package database;

import model.appointment.Appointment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentsRepoTest {

    private MySQL_Connector connector;
    private Connection connection;
    private Statement stmt;
    private PreparedStatement ps;
    private ResultSet rs;
    private AppointmentsRepo appointmentsRepo;

    void connect(){
        this.connector = new MySQL_Connector();
        this.connector.setUrl("jdbc:mysql://localhost:3306/arztpraxistest?user=root&password=secret");

        connection = connector.connect();
        stmt = null;
        ps = null;
        rs = null;
    }//end connect()

    void disconnect(){
        connector.close(stmt);
        connector.close(rs);
        connector.close(ps);
        connector.close(connection);
    }//end disconnect()

    void truncate_Table(){
        this.connect();
        try {
            stmt = connection.createStatement();
            stmt.execute("TRUNCATE TABLE appointments");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }//end truncate_Table()


    private static Stream<Set> insertSetOfAppointments_TEST(){
        //String name = zdt.format(DateTimeFormatter.ofPattern("HH:mm, EEEE dd.MM.uuuu"));
        Appointment sunday_1300 = new Appointment(
                "13:00, Sunday 01.01.1995",
                ZonedDateTime.of(1995,1,1,13,0,0,0,
                        ZoneId.of("Europe/Vienna")),
                false);
        Appointment monday_0800 = new Appointment(
                "08:00, Monday 12.02.1934",
                ZonedDateTime.of(1934,2,12,8,0,0,0,
                        ZoneId.of("Europe/Vienna")),
                false);
        return Stream.of(new HashSet<Appointment>(Arrays.asList(sunday_1300, monday_0800)));
    }//end provideAppointments

    @ParameterizedTest
    @MethodSource
    void insertSetOfAppointments_TEST(Set<Appointment> appointmentSet) {
        this.truncate_Table();
        appointmentsRepo = new AppointmentsRepo();
        appointmentsRepo.getConnector().setUrl("jdbc:mysql://localhost:3306/arztpraxistest?user=root&password=secret");
        //core
        appointmentsRepo.insertSetOfAppointments(appointmentSet);

        Set<Appointment> actual = appointmentsRepo.fetchAllAppointments();
        Set<Appointment> expected = appointmentSet;
        System.out.println("expected: " +expected);
        System.out.println("actual: " +actual);
        assertTrue(expected.containsAll(actual), "appointments from repo do not match");
    }

    @Test
    void fetchAllAppointments() {
    }

    @Test
    void fetchAppointmentsByDate() {
    }
}