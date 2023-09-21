package database;

import model.appointment.Template;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class OfficeQueries {

    MySQL_Connector connector = new MySQL_Connector();

    //todo: zwei identische Vorlagen vermeiden
    public int insertSingleTemplate(Template template){
        int id = -2;
        Connection conn = connector.connect();
        System.out.println("LOG OfficeQueries.insertSingleTemplate: Connection established");

        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps;

        String weekday = template.getWeekday().toString();
        String starttime = template.getStartTime().toString();
        boolean active = template.isActive();

        String query = "INSERT INTO templates (weekday, starttime, active) VALUES(?, ?, ?)";
        try {
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, weekday);
            ps.setString( 2, starttime);
            ps.setBoolean(3, active);
            ps.executeUpdate();
            //Qu: https://dev.mysql.com/doc/connector-j/8.1/en/connector-j-usagenotes-last-insert-id.html
            rs = ps.getGeneratedKeys();
            if (rs.next()){
                id = rs.getInt(1);
            }else {
                //todo: welche Exception?
                throw new RuntimeException("no Generated Key in my table 'templates'");
            }
            template.setId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            connector.close(conn);
            connector.close(stmt);
            connector.close(rs);
            System.out.println("LOG OfficeQueries.insertSingleTemplate: Connection closed");
        }
        return id;
    }


    public Set<Template> fetchTemplates_By_Weekday(DayOfWeek dayOfWeek){
        System.out.println("LOG enter OfficeQueries.fetchTemplates_By_Weekday");
        Connection conn = connector.connect();
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps;

        String query = "select * from templates where weekday = ?";

        Template template;
        Set<Template> templates = new HashSet<>();
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, dayOfWeek.name());
            rs = ps.executeQuery();
            while (rs.next()){
                template = new Template(null, null);
                DayOfWeek weekday = DayOfWeek.valueOf(rs.getString("weekday").toUpperCase());
                String time = rs.getString("starttime");
                LocalTime localTime = LocalTime.parse(time);
                int id = rs.getInt("id");
                template.setWeekday(weekday);
                template.setStartTime(localTime);
                template.setId(id);
                template.setActive(rs.getBoolean(4));
                System.out.println("LOG: OfficeQueries.fetchTemplates_By_Wee1kday fetched this template: " + template);
                templates.add(template);
                System.out.println("LOG: OfficeQueries.fetchTemplates_By_Weekday, number of Objects in HashSet: " + templates.size());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            connector.close(conn);
            connector.close(stmt);
            connector.close(rs);
            System.out.println("LOG: leave OfficeQueries.fetchTemplates_By_Weekday");
        }
        return templates;
    }//end fetchTemplates_By_Weekday(DayOfWeek dayOfWeek)

    public Set<Template>[] getAllTemplates(){
        //todo
        Set<Template>[] templates = new Set[]{};
        return templates;
    }//end getAllTemplates

    public MySQL_Connector getConnector() {
        return connector;
    }

}//end class
