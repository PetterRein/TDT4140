package tdt4140.gr1844.app.core;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;

/**
* Contains the methods for handling the user's login state.
*/
public class Authentication {
	/**
	* Logs the user in.
	* @param username Username.
	* @param password Password.
	* @return {@code true} if the login was successful, {@code false} otherwise.
	*/
	public static boolean login(String username, String password) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException, NamingException {
		SqlConnect conn = new SqlConnect();
		//Retrieve the users salt from the database, if no salt is returned then the user doesn't exist
		PreparedStatement saltRetrieval = conn.connect().prepareStatement("select salt from users where email = ?");
		saltRetrieval.setString(1, username);
		saltRetrieval.execute();
		ResultSet retrievedSalt = saltRetrieval.getResultSet();

		//Check if any salt was returned by the query. If no salt was returned then the user doesn't exst and return false
		if (!retrievedSalt.isBeforeFirst()) {
			System.out.println("Email error error");
			return false;
		}
		retrievedSalt.next();
		//use salt and password to make a password hash
		String salt = retrievedSalt.getString("salt");
		String passwordHash = BCrypt.hashpw(password, salt);

		saltRetrieval.close();
		//Make a query that checks if the user and password is valid
		PreparedStatement authenticationQuery = conn.connect().prepareStatement("select * from users where (email = ? and passwordHash = ?)");
		authenticationQuery.setString(1, username);
		authenticationQuery.setString(2, passwordHash);
		authenticationQuery.execute();
		ResultSet authenticationResponse = authenticationQuery.getResultSet();
		//If query returned any results then username and password were correct and returns true, else it returns false
		Boolean validLogin = authenticationResponse.isBeforeFirst();
		authenticationQuery.close();
		conn.disconnect();
		return validLogin;
	}

	/**
	 * Logs the user out
	 * @param cookie Session Cookie for the user that we want to log out.
	 * @return {@code true} if the login was successful, {@code false} otherwise.
	 */
	public static Boolean logout(String cookie) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException, NamingException {
		SqlConnect conn = new SqlConnect();
		PreparedStatement statement = conn.connect().prepareStatement("update users set cookie = null where cookie = ?");
		statement.setString(1, cookie);
		int updateCheck = statement.executeUpdate();
		//Returns true if something was updated
		conn.disconnect();
		return updateCheck > 0;
	}
}