package database;

import model.appointment.Appointment;
import model.appointment.Template;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class AppointmentsRepo {

    private MySQL_Connector connector = new MySQL_Connector();
    private Connection connection;
    private Statement stmt;
    private PreparedStatement ps;
    private ResultSet rs;

    void connect(){
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


    public void createTable(){
        this.connect();
        stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute("CREATE TABLE appointments (" +
                    "name VARCHAR(40), datetime DATETIME, taken BOOLEAN, patient VARCHAR(40), " +
                    "id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY (id))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.disconnect();
    }//end createTable()

    public void insertSetOfAppointments(Set<Appointment> appointments){
        this.connect();
        ps = null;
        try {
            ps = connection.prepareStatement("INSERT INTO appointments (name, datetime, taken) " +
                    "VALUES (?, ?, ?)");
            stmt = connection.createStatement();
            for (Appointment a : appointments) {
                ps.setString(1, a.getName());
                Timestamp timestamp = Timestamp.valueOf(a.getDateTime().toLocalDateTime());
                ps.setTimestamp(2, timestamp);
                ps.setBoolean(3, a.isTaken());
                ps.execute();
                if (a.getPatient() != null){
                    stmt.executeUpdate("UPDATE appointments SET patient = " + a.getPatient().toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.disconnect();
    }//end insertSetOfAppointments



    public Set<Appointment> fetchAllAppointments(){
        this.connect();
        String query = "select * from appointments";
        Appointment a;
        Set<Appointment> appointments = new HashSet<>();
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString(1);
                Timestamp timestamp = rs.getTimestamp(2);
                ZonedDateTime datetime = timestamp.toLocalDateTime().atZone(ZoneId.of("Europe/Vienna"));
                Boolean taken = rs.getBoolean(3);
                String patient = rs.getString(4);
                int id = rs.getInt(5);
                a = new Appointment(name, datetime, taken);
                appointments.add(a);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        this.disconnect();
        return appointments;
    }//end fetchAllAppointments()

    public Set<Appointment> fetchAppointmentsByDate(){
        this.connect();

        this.disconnect();
        return new HashSet<>();
    }//end createTable()


    //GETTER
    public MySQL_Connector getConnector() {
        return connector;
    }
}//end AppointmentsRepo
