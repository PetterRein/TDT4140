package tdt4140.gr1844.app.server;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import tdt4140.gr1844.app.core.CookieValueGenerator;

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

		PreparedStatement getSalt = conn.connect().prepareStatement("SELECT salt FROM users WHERE email = ?");
		getSalt.setString(1, email);
		getSalt.execute();
		ResultSet rs = getSalt.getResultSet();
		// If null was returned, the user does not exist and login() should return false
		if (!rs.isBeforeFirst()) {
			getSalt.close();
			conn.disconnect();
			json.put("status", "ERROR");
			json.put("message", "User does not exist");
		} else {
			rs.next();
			String salt = rs.getString("salt");
			String passwordHash = BCrypt.hashpw(password, salt);
			PreparedStatement getUser = conn.connect().prepareStatement(
					"SELECT * FROM users WHERE (email = ? AND passwordHash = ?)"
			);
			getUser.setString(1, email);
			getUser.setString(2, passwordHash);
			getUser.execute();
			ResultSet authenticationResponse = getUser.getResultSet();
			Boolean userExists = authenticationResponse.isBeforeFirst();
			if (userExists) {
				JSONObject user = new JSONObject();
				user.put("userId", authenticationResponse.getString(1));
				user.put("role",authenticationResponse.getString(2));
				user.put("name", authenticationResponse.getString(3));
				user.put("doctorId",authenticationResponse.getInt(7));
				json.put("user", user);
				json.put("status", "OK");
				PreparedStatement getCookie = conn.connect().prepareStatement(
						"SELECT cookie FROM users WHERE email = ?"
				);
				getCookie.setString(1, email);
				getCookie.execute();
				String cookie = getCookie.getResultSet().getString(1);

				// If this is the first login, generate a cookie and send it in the response
                if (cookie == null) {
                    String newCookie = CookieValueGenerator.generateCookieValue();
                    PreparedStatement setCookie = conn.connect().prepareStatement(
                            "UPDATE users SET cookie = ? WHERE email = ?"
                    );
                    setCookie.setString(1, newCookie);
                    setCookie.setString(2, email);
                    setCookie.execute();
                    json.put("cookie", newCookie);

                    getCookie.close();
                    setCookie.close();
                    // If there is a cookie, send it in the response
                } else {
                    json.put("cookie", cookie);
                }
			} else {
				json.put("status", "ERROR");
				json.put("message", "Wrong username or password");
			}

			getUser.close();
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