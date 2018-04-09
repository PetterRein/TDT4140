package tdt4140.gr1844.app.server;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.json.JSONObject;


/**
 * Contains the methods for handling/mutating the database.
 */
public class Database {

    static void initDatabase() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        // create a database connection
        SQL conn = new SQL();

        // drop tables if they exist
        System.out.println("Dropping all tables...");
        PreparedStatement usersTable = conn.connect().prepareStatement("DROP TABLE IF EXISTS users");
        PreparedStatement patientDataTable = conn.connect().prepareStatement("DROP TABLE IF EXISTS patientData");
        PreparedStatement feedback = conn.connect().prepareStatement("DROP TABLE IF EXISTS feedback");
        usersTable.execute();
        patientDataTable.execute();
        feedback.execute();

        // create table users
        PreparedStatement statement1 = conn.connect().prepareStatement("CREATE TABLE IF NOT EXISTS users" +
                "(id INTEGER PRIMARY KEY, role varchar(64), name varchar(64), email varchar(64), passwordHash varchar(2000), salt varchar(256)," +
                "cookie varchar(256), doctorID int, FOREIGN KEY (doctorID) REFERENCES users(id))");
        // create table patientData
        PreparedStatement statement2 = conn.connect().prepareStatement("CREATE TABLE IF NOT EXISTS patientData" +
                "(id INTEGER PRIMARY KEY, patientID int not null, rating int, extrainfo text," +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null," +
                "FOREIGN KEY (patientID) REFERENCES users(id))");
        PreparedStatement statement3 = conn.connect().prepareStatement("CREATE TABLE IF NOT EXISTS feedback" +
                "(id INTEGER PRIMARY KEY, message VARCHAR (20000000))");


        statement1.execute();
        statement2.execute();
        statement3.execute();


        // TODO: Fix test
        handleCreatePatient("Haavard", "haavard@email.com", "password", 2);
        handleCreatePatient("Balazs", "balazs@email.com", "password", 3);
        handleCreatePatient("Mats", "mats@email.com", "password", 1);
        PreparedStatement statement4 = conn.connect().prepareStatement("update users set cookie='a' where email = 'email'");
        statement4.executeUpdate();
        conn.disconnect();
    }


    // Handling Authentication actions
    static JSONObject handleLogin(String email, String password) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        return Authentication.login(email, password);
    }

    static JSONObject handleLogout(String cookie) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        return Authentication.logout(cookie);
    }

    // Handling Create actions
    static JSONObject handleCreateAdminOrDoctor(String name, String email, String password, String role, String cookie) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return Create.createAdminOrDoctor(name, email, password, role, cookie);
    }

    static JSONObject handleCreatePatient(String name, String email, String password, int doctorId) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        return Create.createPatient(name, email, password, doctorId);
    }

    static JSONObject handleCreateFeeling(int patientId, int rating, String extraInfo, String cookie) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return Create.createFeeling(patientId, rating, extraInfo, cookie);
    }


    // Handling Delete actions
	static JSONObject handleDeleteUser(int userId, String cookie) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        return Delete.deleteUser(userId, cookie);
	}


	// Handling Retrieve actions
    static JSONObject handleGetPatientData(int patientID, String orderBy, String cookie) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        return Retrieve.getPatientData(patientID, orderBy, cookie);
    }

}
