package database;

import java.io.IOException;
import java.sql.*;

public class MySQL_Connector {

    String url = "jdbc:mysql://localhost:3306/arztpraxis?user=root&password=secret";
    Connection con;

    public Connection connect(){
        try {
            con = DriverManager.getConnection(url);
            if (con != null) {
            }
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    return null;
    }// end connect

    public void close(Connection con){
        if (con != null){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(Statement st){
        if (st != null){
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(ResultSet rs){
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//close(ResultSet rs)

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
}//end MySQL_Connector

