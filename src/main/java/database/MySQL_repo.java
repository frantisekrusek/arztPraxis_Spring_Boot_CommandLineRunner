package database;

import model.appointment.Template;
import model.generator.Supervisor;
import model.office.Office;
import model.person.officeManager.OfficeManager;

import java.sql.*;
import java.time.Instant;
import java.util.Iterator;
import java.util.Set;

public class MySQL_repo {

    OfficeManager officeManager;
    Office office;

    public MySQL_repo(OfficeManager officeManager, Office office){
        this.officeManager = officeManager;
        this.office = office;
    }

    String url = "jdbc:mysql://localhost:3306/arztpraxis?user=root&password=secret";
    Connection con;
    Statement st;
    ResultSet rs;
    PreparedStatement ps;

    //todo
    public void _24hMethode(){
        try {
            con = DriverManager.getConnection(url);
            //ERSETZE Timestamp statt 'insert into .. '
//            ps = con.prepareStatement("UPDATE supervisor SET lastUpdate = ? WHERE id = 1");
//            ps.setTimestamp(1, now);
//            ps.executeUpdate();

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }// end _24hMethode()

    public void updateTemplate(Template template){
        System.out.println("LOG: enter MySQL_repo.updateTemplate");
        try {
            con = DriverManager.getConnection(url);
            String weekday = template.getWeekday().toString();
            String startTime = template.getStartTime().toString();
            //? todo: if not exists ?
            PreparedStatement ps = con.prepareStatement("INSERT INTO TEMPLATES (weekday, starttime)" +
                        "VALUES (?, ?)");
            ps.setString(1,weekday);
            ps.setString(2,startTime);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
            System.out.println("LOG: leave MySQL_repo.updateTemplate");
        }
    }// end updateTemplate()

    public void updateTemplates(){
        System.out.println("LOG: enter MySQL_repo.updateTemplates");
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            con = DriverManager.getConnection(url);
            System.out.println("LOG: Connection established");
            //update
            for(Set weekdaySet : office.getTemplates()) {

                Iterator iterator = weekdaySet.iterator();

                while (iterator.hasNext()) {
                    Template template = (Template) iterator.next();
                    String startTime = template.getStartTime().toString();
                    String weekday = template.getWeekday().toString();

                    PreparedStatement ps = con.prepareStatement("INSERT INTO TEMPLATES (weekday, starttime)" +
                            "VALUES (?, ?)");
                    ps.setString(1, weekday);
                    ps.setString(2, startTime);
                    ps.executeUpdate();
                    ps.close();
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                con.close();
                System.out.println("LOG: Connection closed");
            }catch (SQLException e){
                e.printStackTrace();
            }
            System.out.println("LOG: leave MySQL_repo.updateTemplates");
        }
    }// end updateTemplates()

}//end class
