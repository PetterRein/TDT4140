package tdt4140.gr1844.app.server;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnect {
    // init database constants
    private static final String DATABASE_URL = "jdbc:sqlite:sample.db"; //Denne er feil se print fra Working Directory
    // init connection object
    // connect database
    private Connection connection = null;
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

    public Connection connect() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
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
