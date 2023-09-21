package database;

import java.sql.*;
import java.time.Instant;
import java.time.ZonedDateTime;

public class SupervisorQueries {

    MySQL_Connector connector = new MySQL_Connector();

    public Instant fetchLastUpdate() {
        Connection conn = connector.connect();
        Statement stmt = null;
        ResultSet rs = null;
        Instant instant = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT lastUpdate FROM supervisor WHERE  id = 1");
            rs.next();/*first row is current row now*/
            Timestamp timestamp1 = rs.getTimestamp("lastUpdate");
            instant = timestamp1.toInstant();
            //Supervisor.getInstance().setLastUpdate(instant);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            connector.close(rs);
            connector.close(stmt);
            connector.close(conn);
        }
        System.out.println("LOG SupervisorQueries.fetchLastUpdate, Instant: " + instant);
        return instant;
    }//end fetchLastUpdate()

    public void initializeLastUpdate(ZonedDateTime zonedDateTime){
        Connection conn = connector.connect();
        PreparedStatement ps = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Timestamp timestamp = Timestamp.from(zonedDateTime.toInstant());
            System.out.println("LOG SupervisorQueries.initializeLastUpdate: timestamp: " + timestamp);

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT lastUpdate FROM supervisor");
            if (!rs.isBeforeFirst())/*Resultset is empty*/ {
                ps = conn.prepareStatement("INSERT INTO supervisor (lastupdate, id) VALUES (?,?)");
                ps.setTimestamp(1, timestamp);
                ps.setInt(2, 1);
                ps.executeUpdate();
            }
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                connector.close(rs);
                connector.close(stmt);
                connector.close(conn);
            }
    }//end initializeLastUpdate()


    public void update_LastUpdate(ZonedDateTime zonedDateTime){
        Connection conn = connector.connect();
        PreparedStatement ps = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Timestamp timestamp = Timestamp.from(zonedDateTime.toInstant());
            System.out.println("LOG SupervisorQueries.initializeLastUpdate: timestamp: " + timestamp);

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT lastUpdate FROM supervisor");
            if (rs.isBeforeFirst())/*Resultset is NOT empty*/ {
                ps = conn.prepareStatement("UPDATE supervisor SET lastupdate = ? WHERE id = 1");
                ps.setTimestamp(1, timestamp);
                ps.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            connector.close(rs);
            connector.close(stmt);
            connector.close(conn);
        }
    }//end update_LastUpdate(ZonedDateTime zonedDateTime)

    //GETTER
    public MySQL_Connector getConnector() {
        return connector;
    }
}
