package tdt4140.gr1844.app.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import javax.naming.NamingException;


/**
 * Contains the methods for handling/mutating the database.
 **/
import java.sql.ResultSet;
import java.util.ArrayList;

public class Database {
	public static void createUser(boolean onlineOrOffline, String role, String name, String email, String password) throws NamingException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		String salt = BCrypt.gensalt();
		String passwordHash = BCrypt.hashpw(password, salt);
		try {
			/**
			 * Todo legg inn sjekk på bruker allerede er lagt til
			 */
			SqlConnect conn = new SqlConnect();
			PreparedStatement statement1 = conn.connect(onlineOrOffline).prepareStatement("insert into users(role, name, email, passwordHash, salt) values(?, ?, ?, ?, ?)");
			statement1.setString(1, role);
			statement1.setString(2, name);
			statement1.setString(3, email);
			statement1.setString(4, passwordHash);
			statement1.setString(5, salt);
			statement1.executeUpdate();
			conn.disconnect();
		}
		catch(SQLException e) {
			System.err.println(e);
		}
	}

	/**
	 * Deletes a user from the database by their e-mail address.
	 * @param email The e-mail address of the user to be deleted.
	 * @param onlineOrOffline If the database is online or offline.
	 */
	public static void deleteUser(String email, boolean onlineOrOffline) throws NamingException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        try {
			/**
			 * Todo legg til sjekk som sjekker om bruker finnes før vi sletter null
			 */
			SqlConnect conn = new SqlConnect();
            PreparedStatement statement = conn.connect(onlineOrOffline).prepareStatement("delete from users where email=?");
            statement.setString(1, email);
            statement.executeUpdate();
            conn.disconnect();
        }
        catch(SQLException e) {
            System.err.println(e);
        }
	}

    /**
     *
     * @param onlineOrOffline Check of database is online or offline
     * @param cookie Users cookie you want to check the role on
     * @return Returns the users role
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
	public static String getRoleFromCookie(boolean onlineOrOffline, String cookie) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		String role = null;
		try {
			/**
			 * Todo legg til sjekk som sjekker om bruker har en role
			 */
			SqlConnect conn = new SqlConnect();
			PreparedStatement statement1 = conn.connect(onlineOrOffline).prepareStatement("select role from users where cookie = ?");
			statement1.setString(1, cookie);
			statement1.execute();
			ResultSet rs = statement1.getResultSet();
			rs.next();
			role = rs.getString("role");
			conn.disconnect();
		}
		catch(SQLException e) {
			
		}
		catch(NamingException e) {
			
		}
		return role;
	}

	/**
	 *
	 * @param onlineOrOffline Check if database is online or offline
	 * @param userName email on user you want data for
	 * @return A ArrayList<String> or a ArrayList<Int> Array that contains all data on the requestet user
	 */
	public static ArrayList<String> getAllDataOnUser(boolean onlineOrOffline, String userName){
		SqlConnect conn = new SqlConnect();
        ArrayList<String> arrayWithData = null;
        return arrayWithData;
	}

	public static boolean addDataToUser(boolean onlineOrOffline, String Data, String userName){
        /**
         * Create timestap
         * Add data to right place
         * Create a primarykey
         */
        return false;
    }

    public static boolean delDataFromUser(boolean onlineOrOffline, int primaryKey, String userName){
        /**
         * Del the data from the primarykey on that user from userName
         */

        return false;
    }

    public static boolean delDataInArray(boolean onlineOrOffline, int[] primaryKeys, String userName){
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
}