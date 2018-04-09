package tdt4140.gr1844.app.server;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import javax.naming.NamingException;

/**
* contains the methods for handling the user's login state
*/
public class Authentication {

	/**
	 * to log in users by email and password
	 * @param email the user's email
	 * @param password the user's password
	 * @return {@code true} if the login was successful, {@code false} otherwise
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * TODO: Retrieve encrypted password from the client
	 */

	static JSONObject login(String email, String password) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {

		SqlConnect conn = new SqlConnect();
		JSONObject json = new JSONObject();

		PreparedStatement statement = conn.connect().prepareStatement("SELECT salt FROM users WHERE email = ?");
		statement.setString(1, email);
		statement.execute();
		ResultSet rs = statement.getResultSet();
		// If null was returned, the user does not exist and login() should return false
		if (!rs.isBeforeFirst()) {
			statement.close();
			conn.disconnect();
			json.put("status", "ERROR");
			json.put("message", "User does not exist");
		} else {
			rs.next();
			String salt = rs.getString("salt");
			String passwordHash = BCrypt.hashpw(password, salt);
			PreparedStatement statement2 = conn.connect().prepareStatement(
					"SELECT * FROM users WHERE (email = ? AND passwordHash = ?)"
			);
			statement2.setString(1, email);
			statement2.setString(2, passwordHash);
			statement2.execute();
			ResultSet authenticationResponse = statement2.getResultSet();
			Boolean isLoggedIn = authenticationResponse.isBeforeFirst();


			if (isLoggedIn) {
				JSONObject user = new JSONObject();
				user.put("userId", authenticationResponse.getString(1));
				user.put("role",authenticationResponse.getString(2));
				user.put("name", authenticationResponse.getString(3));
				user.put("doctorId",authenticationResponse.getInt(7));
				json.put("user", user);
				json.put("status", "OK");
			} else {
				json.put("status", "ERROR");
				json.put("message", "Wrong username or password");
			}

			statement2.close();
			conn.disconnect();

		}
		return json;
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