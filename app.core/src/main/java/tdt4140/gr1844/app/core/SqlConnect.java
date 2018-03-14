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
    Connection connection = null;
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

    public Connection connect(boolean onlineOrOffline) throws NamingException, SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
            Class.forName("org.sqlite.JDBC").newInstance();
            //DriverManager.registerDriver(new JDBC());
            java.nio.file.Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            //System.out.println("Current relative path is: " + s);
            //System.out.println("Working Directory = " + System.getProperty("user.dir"));
            if (connection == null) {
                try {
                    connection = DriverManager.getConnection(DATABASE_URL);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return connection;

        }
}
/**
 /**
 Context ctx = new InitialContext();
 DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/sample");
 conn = ds.getConnection();
 return conn;**/