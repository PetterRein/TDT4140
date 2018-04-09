package tdt4140.gr1844.app.server;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import javax.naming.NamingException;


//Contains the methods for handling/mutating the database.

import java.sql.ResultSet;

public class Database {

    static void initDatabase() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NamingException, SQLException {
        // create a database connection
        SqlConnect conn = new SqlConnect();

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
                "times TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null," +
                "FOREIGN KEY (patientID) REFERENCES users(id))");
        PreparedStatement statement3 = conn.connect().prepareStatement("CREATE TABLE IF NOT EXISTS feedback" +
                "(id INTEGER PRIMARY KEY, message VARCHAR (20000000))");


        statement1.execute();
        statement2.execute();
        statement3.execute();


        // TODO: Fix test
        createUser("Doctor", "s", "email", "password", null);
        PreparedStatement statement4 = conn.connect().prepareStatement("update users set cookie='a' where email = 'email'");
        statement4.executeUpdate();
        conn.disconnect();
    }



	static void createUser(String role, String name, String email, String password, String usersDoctor) throws NamingException, IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
		String salt = BCrypt.gensalt();
		String passwordHash = BCrypt.hashpw(password, salt);
        int doctorId = -1;
        //  TODO: legg inn sjekk på bruker allerede er lagt til
        if (usersDoctor != null){
            doctorId = getIdByUserEmail(usersDoctor);
        }

        SqlConnect conn = new SqlConnect();
        PreparedStatement statement = conn.connect().prepareStatement("insert into users(role, name, email, passwordHash, salt, doctorID) values(?, ?, ?, ?, ?, ?)");

        statement.setString(1, role);
        statement.setString(2, name);
        statement.setString(3, email);
        statement.setString(4, passwordHash);
        statement.setString(5, salt);
        statement.setInt(6, doctorId);
        statement.executeUpdate();
        conn.disconnect();
	}

	/**
	 * Deletes a user from the database by their e-mail address.
	 * @param email The e-mail address of the user to be deleted.
     */
	static void deleteUser(String email) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        //  TODO: legg til sjekk som sjekker om bruker finnes før vi sletter null
        SqlConnect conn = new SqlConnect();
        PreparedStatement statement = conn.connect().prepareStatement("delete from users where email=?");
        statement.setString(1, email);
        statement.executeUpdate();
        conn.disconnect();
	}

    /**
     *
     * @param cookie Users cookie you want to check the role on
     * @return Returns the users role
     */
	static String getRoleFromCookie(String cookie) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NamingException {
		String role = null;

        try {
            SqlConnect conn = new SqlConnect();
            PreparedStatement statement = conn.connect().prepareStatement("select role from users where cookie = ?");
            statement.setString(1, cookie);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            rs.next();
            role = rs.getString("role");
            conn.disconnect();

        } catch (SQLException e) {
            System.err.println(e);
        }
		return role;
	}




    private static int getIdByUserEmail(String userEmail) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NamingException {
	    int userId = -1;
	    try {
            SqlConnect conn = new SqlConnect();
            PreparedStatement preparedStatement1 = conn.connect().prepareStatement("SELECT id FROM users WHERE email = ?");
            preparedStatement1.setString(1,userEmail);
            preparedStatement1.execute();
            ResultSet rs1 = preparedStatement1.getResultSet();
            rs1.next();
            userId = rs1.getInt("id");
            conn.disconnect();
        } catch (SQLException e) {
	        e.printStackTrace();
        }
        return userId;
    }


    static JSONObject handleLogin(String email, String passwordHash) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        return Authentication.login(email, passwordHash);
    }


    static JSONObject handleLogout(String cookie) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException, NamingException {
        return Authentication.logout(cookie);
    }

    static JSONObject handleGetPatientData(String patientId, String orderBy, String cookie) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        SqlConnect conn = new SqlConnect();
        if (Authentication.isAuthenticated(cookie, "Doctor")){
            PreparedStatement statement = conn.connect()
                    .prepareStatement(
                            "SELECT * FROM patientData " +
                                    "WHERE patientID = " + patientId + " " +
                                    "ORDER BY timestamp " + orderBy);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            JSONObject json = SQLToJSONArray(rs, "patients");
            conn.disconnect();
            return json;
        } else {
            JSONObject json = new JSONObject();
            json.put("status", "ERROR");
            json.put("message", "You are not authorized to retrieve this information");
            return json;
        }
	}



	private static JSONObject SQLToJSON (ResultSet rs) throws SQLException {
        JSONObject json = new JSONObject();
        ResultSetMetaData rsmd = rs.getMetaData();
        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            for (int i=1; i<=numColumns; i++) {
                String column_name = rsmd.getColumnName(i);
                System.out.println(column_name);
                json.put(column_name, rs.getObject(column_name));
            }
        }
        return json;
    }

}
