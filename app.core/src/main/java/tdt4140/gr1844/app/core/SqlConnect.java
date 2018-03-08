package tdt4140.gr1844.app.core;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnect {
    // init database constants
    private static final String DATABASE_URL = "jdbc:sqlite:sample.db"; //Denne er feil se print fra Workiing Directory
    // init connection object
    // connect database
    public Connection connection = null;
    public Connection connect1() throws ClassNotFoundException, SQLException {
       /** //Class.forName("org.sqlite.JDBC");
        System.out.println("TEst203");
        //DriverManager.registerDriver(new JDBC());
        java.nio.file.Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.println("test89");
        if (connection == null) {
            try {
                System.out.println("Test0123");
                connection = DriverManager.getConnection(DATABASE_URL);
                System.out.println("Test23124");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;**/
       return connection;
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection connect(boolean onlineOrOffline) throws NamingException, SQLException {
        if (onlineOrOffline){
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/sample");
            connection = ds.getConnection();
            return connection;
        }
        else{
            //Class.forName("org.sqlite.JDBC");
            System.out.println("TEst203");
            //DriverManager.registerDriver(new JDBC());
            java.nio.file.Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            System.out.println("Current relative path is: " + s);
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            System.out.println("test89");
            if (connection == null) {
                try {
                    System.out.println("Test0123");
                    connection = DriverManager.getConnection(DATABASE_URL);
                    System.out.println("Test23124");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return connection;

        }
    }
}
/**
 /**
 Context ctx = new InitialContext();
 DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/sample");
 conn = ds.getConnection();
 return conn;**/