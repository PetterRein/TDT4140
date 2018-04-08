package tdt4140.gr1844.app.core;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import javax.naming.NamingException;


/**
 * Contains the methods for handling/mutating the database.
 **/
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static void initDatabase() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NamingException, SQLException {
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
        createUser(false,"Doctor", "s", "email", "password", null);
        PreparedStatement statement4 = conn.connect().prepareStatement("update users set cookie='a' where email = 'email'");
        statement4.executeUpdate();
        conn.disconnect();
    }



	public static void createUser(boolean onlineOrOffline, String role, String name, String email, String password, String usersDoctor) throws NamingException, IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
		String salt = BCrypt.gensalt();
		String passwordHash = BCrypt.hashpw(password, salt);
        int doctorId = -1;
        /**
         * Todo legg inn sjekk på bruker allerede er lagt til
         */
        if (usersDoctor != null){
            doctorId = getIdByUserEmail(onlineOrOffline, usersDoctor);
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

	public static void createFeedback(String feedback) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, NamingException {
        SqlConnect conn = new SqlConnect();
        PreparedStatement statement = conn.connect().prepareStatement("insert into feedBack(message) values(?)");
        statement.setString(1, feedback);
        statement.executeUpdate();
        conn.disconnect();
    }

	/**
	 * Deletes a user from the database by their e-mail address.
	 * @param email The e-mail address of the user to be deleted.
     */
	public static void deleteUser(String email) throws NamingException, IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        /**
         * Todo legg til sjekk som sjekker om bruker finnes før vi sletter null
         */
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
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
	public static String getRoleFromCookie(String cookie) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NamingException {
		String role = null;
        /**
         * Todo legg til sjekk som sjekker om bruker har en role
         */

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

	public static String getEmailFromCookie(String sid) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, NamingException {
        SqlConnect conn = new SqlConnect();
        PreparedStatement statement = conn.connect().prepareStatement("select email from users where cookie = ?");
        statement.setString(1, sid);
        statement.execute();
        ResultSet rs = statement.getResultSet();
        rs.next();
        String userEmail = rs.getString(1);
        conn.disconnect();
        return userEmail;
    }

	public static String getDoctorsPatients(String email) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException, NamingException{
        SqlConnect conn = new SqlConnect();
        PreparedStatement statement1 = conn.connect().prepareStatement("select id from users where email = ?");
        statement1.setString(1, email);
        statement1.execute();
        ResultSet rs1 = statement1.getResultSet();
        rs1.next();
        int doctorId =  (Integer) rs1.getObject("id");
        PreparedStatement statement2 = conn.connect().prepareStatement("select name, email from users where doctorID = ?");
        statement2.setInt(1, doctorId);
        statement2.execute();
        ResultSet rs2 = statement2.getResultSet();
        String result1 = "";
        while (rs2.next()){
            if (result1.equals("")){
                result1 = result1 + rs2.getString(1) + "/" + rs2.getString(2);
            }
            else {
                result1 = result1 + "/" + rs2.getString(1)+ "/" + rs2.getString(2);
            }
        }
        conn.disconnect();
        return result1;
    }

    public static ArrayList<String> getNLastPatientData(String email, int n) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        ArrayList<String> result = new ArrayList<>();
        SqlConnect conn = new SqlConnect();
        PreparedStatement statement1 = conn.connect().prepareStatement("select id from users where email = ?");
        statement1.setString(1, email);
        statement1.execute();
        ResultSet rs1 = statement1.getResultSet();
        rs1.next();
        int patientId = (Integer) rs1.getObject("id");
        PreparedStatement statement2 = conn.connect().prepareStatement("select rating, extrainfo from patientData where patientID = ? LIMIT ?");
        statement2.setInt(1, patientId);
        statement2.setInt(2, n+1);
        statement2.execute();
        ResultSet rs2 = statement2.getResultSet();
        conn.disconnect();
        rs2.next();
        while(rs2.next()) {
            result.add(rs2.getInt(1) + "/" + rs2.getString(2));
        }
        System.out.println("resultData " + result);
        return result;
    }

	/**
	 *
	 * @param userName email on user you want data for
	 * @return A ArrayList<String> or a ArrayList<Int> Array that contains all data on the requested user
	 */
	public static ArrayList<String> getAllDataOnUser(String userName){
		SqlConnect conn = new SqlConnect();
        ArrayList<String> arrayWithData = null;
        conn.disconnect();
        return arrayWithData;
	}

	public static boolean addDataToUser(String data, String userName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NamingException {
        /**
         * Create timestamp
         * Add data to right place
         * Create a primary key
         */
        try {
            SqlConnect conn = new SqlConnect();
            int rating = 1;
            int patientID = getIdByUsername(false, userName);
            PreparedStatement preparedStatement2 = conn.connect().prepareStatement("INSERT INTO patientData (patientID, rating, extrainfo) VALUES (?, ?, ?) ");
            preparedStatement2.setInt(1, patientID);
            preparedStatement2.setInt(2, rating);
            preparedStatement2.setString(3, data);
            preparedStatement2.executeUpdate();
            conn.disconnect();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return false;

    }
    public static int getIdByUsername(boolean onlineOrOffline, String userName) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, NamingException {
	    int userId = -1;
        SqlConnect conn = new SqlConnect();
        PreparedStatement preparedStatement1 = conn.connect().prepareStatement("SELECT id FROM users WHERE name = ?");
        preparedStatement1.setString(1,userName);
        preparedStatement1.execute();
        ResultSet rs1 = preparedStatement1.getResultSet();
        rs1.next();
        userId = (Integer) rs1.getObject("id");
        conn.disconnect();
        return userId;
    }

    public static int getIdByUserEmail(boolean onlineOrOffline, String userEmail) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NamingException {
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

    public static boolean delDataFromUser(boolean onlineOrOffline, int primaryKey, String userName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NamingException {
        /**
         * Del the data from the primarykey on that user from userName
         */
        try {
            SqlConnect conn = new SqlConnect();
            PreparedStatement preparedStatement = conn.connect().prepareStatement("DELETE FROM patientData WHERE id = ?");
            preparedStatement.setInt(1, primaryKey);
            preparedStatement.executeUpdate();
            conn.disconnect();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return false;
    }

    public static boolean delDataInArray(boolean onlineOrOffline, int[] primaryKeys, String userName) throws ClassNotFoundException, NamingException, InstantiationException, IllegalAccessException {
	    boolean doneOrNot = false;
	    for (int i = 0; i < primaryKeys.length; i++){
	        doneOrNot = delDataFromUser(onlineOrOffline, primaryKeys[i], userName);
        }
        return doneOrNot;
    }

    public static boolean delAllDataFromUser(boolean onlineOrOffline, String userName){
        /**
         * Get all primarykeys on data put into Int[]
         * Then send to delDataInArray
         */
        return false;
    }

    public static JSONArray handleGetPatientData(String role, String patientId, String orderBy) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        try {
            SqlConnect conn = new SqlConnect();
            PreparedStatement statement = conn.connect().prepareStatement("SELECT * FROM patientData WHERE patientID = ?");
            statement.setInt(1, Integer.parseInt(patientId));
            statement.execute();
            conn.disconnect();
            return SQLToJSON(statement.getResultSet());

        } catch (SQLException e) {
            System.err.println(e);
            return null;
        }
	}

	private static JSONArray SQLToJSON (ResultSet rs) throws SQLException {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();
        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i=1; i<=numColumns; i++) {
                String column_name = rsmd.getColumnName(i);
                System.out.println(column_name);
                obj.put(column_name, rs.getObject(column_name));
            }
            json.put(obj);
        }
        return json;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, NamingException, InstantiationException, IllegalAccessException {
        initDatabase();
    }
}
