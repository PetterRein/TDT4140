package tdt4140.gr1844.app.core;

import javax.naming.NamingException;
import java.sql.*;

public class dbExample {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NamingException {
        // create a database connection
        SqlConnect conn = new SqlConnect();
        try
        {
            // Drop tables
            System.out.println("Dropping all tables...");
            PreparedStatement drops = conn.connect(false).prepareStatement("DROP TABLE IF EXISTS users");
            PreparedStatement drops2 = conn.connect(false).prepareStatement("DROP TABLE IF EXISTS patientData");
            drops.execute();
            drops2.execute();

            // create table users
            PreparedStatement statement1 = conn.connect(false).prepareStatement("CREATE TABLE IF NOT EXISTS users" +
                    "(id INTEGER PRIMARY KEY, role varchar(64), name varchar(64), email varchar(64), passwordHash varchar(2000), salt varchar(256)," +
                    "cookie varchar(256), doctorID int, FOREIGN KEY (doctorID) REFERENCES users(id))");
            statement1.execute();
            // echo table users
            System.out.println("CREATE TABLE IF NOT EXISTS users\" +\n" +
                    "                    \"(_id INTEGER PRIMARY KEY , role varchar(64), name varchar(64), email varchar(64), passwordHash varchar(2000), salt varchar(256),\" +\n" +
                    "                    \"cookie varchar(256), doctorID, FOREIGN KEY (doctorID) REFERENCES  users(id))");

            // create table patientData
            PreparedStatement statement2 = conn.connect(false).prepareStatement("CREATE TABLE IF NOT EXISTS patientData" +
                    "(id INTEGER PRIMARY KEY, patientID int not null, rating int, extrainfo text," +
                    "times TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null," +
                    "FOREIGN KEY (patientID) REFERENCES users(id))");
            statement2.execute();
            PreparedStatement statement3 = conn.connect(false).prepareStatement("CREATE TABLE IF NOT EXISTS feedBack" +
                    "(id INTEGER PRIMARY KEY, message VARCHAR (20000000))");
            statement3.execute();
            conn.disconnect();
            // echo table patientData
            System.out.println("CREATE TABLE IF NOT EXISTS patientData" +
                    "(_id INTEGER PRIMARY KEY, patientID int not null, doctorID int, rating int, extrainfo text," +
                    "times TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null," +
                    "FOREIGN KEY (patientID) REFERENCES users(id), FOREIGN KEY  (doctorID) REFERENCES users(id))");

            Database.createUser(false, "Doctor", "Lars", "lege@mail.co","321", null);
            String legeId = "lege@mail.co";
            Database.createUser(false, "Patient", "Haavard", "syking@mail.co","123", legeId);
            Database.createUser(false,"Admin", "Admin", "admin@o.com", "33", null);
            Database.addDataToUser(false, "5", "Haavard");
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }
}
