package tdt4140.gr1844.app.server;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import tdt4140.gr1844.app.core.CookieValueGenerator;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
* contains the methods for handling the user's login state
*/
public class Authentication {


	/**
	 * to log in users by email and password
	 * @param email the user's email
	 * @param password the user's password
	 * @return {@code true} if the login was successful, {@code false} otherwise
	 * @throws SQLException
	 * TODO: Retrieve encrypted password from the client
	 */
	static JSONObject login(String email, String password) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {

		SQL sql = new SQL();
		JSONObject json = new JSONObject();

		PreparedStatement getSalt = sql.connect()
				.prepareStatement(
				"SELECT salt FROM users WHERE email = ?"
				);
		getSalt.setString(1, email);
		getSalt.execute();
		ResultSet rs = getSalt.getResultSet();
		// If null was returned, the user does not exist and login() should return false
		if (!rs.isBeforeFirst()) {
			getSalt.close();
			sql.disconnect();
			json.put("status", "ERROR");
			json.put("message", "Wrong username or password");
		} else {
			rs.next();
			String salt = rs.getString("salt");
			String passwordHash = BCrypt.hashpw(password, salt);
			PreparedStatement getUser = sql.connect()
					.prepareStatement(
					"SELECT * FROM users WHERE (email = ? AND passwordHash = ?)"
					);
			getUser.setString(1, email);
			getUser.setString(2, passwordHash);
			getUser.execute();
			ResultSet authenticationResponse = getUser.getResultSet();
			Boolean userExists = authenticationResponse.isBeforeFirst();
			if (userExists) {
				JSONObject user = new JSONObject();
				user.put("userID", authenticationResponse.getString(1));
				user.put("role",authenticationResponse.getString(2));
				user.put("name", authenticationResponse.getString(3));
				user.put("doctorID",authenticationResponse.getInt(8));
				json.put("user", user);
				json.put("status", "OK");
				PreparedStatement getCookie = sql.connect()
						.prepareStatement(
						"SELECT cookie FROM users WHERE email = ?"
						);
				getCookie.setString(1, email);
				getCookie.execute();
				String cookie = getCookie.getResultSet().getString(1);

				// If this is the first login, generate a cookie and send it in the response
                if (cookie == null) {
                    String newCookie = CookieValueGenerator.generateCookieValue();
                    PreparedStatement setCookie = sql.connect()
							.prepareStatement(
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
			sql.disconnect();

		}
		return json;
	}


	/**
	 * Logs the user out
	 * @param cookie Session Cookie for the user that we want to log out.
	 * @return {@code true} if the login was successful, {@code false} otherwise.
	 */
	static JSONObject logout(String cookie) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
		SQL sql = new SQL();
		JSONObject json = new JSONObject();
		PreparedStatement doLogout = sql.connect()
				.prepareStatement(
						"UPDATE users SET cookie = null WHERE cookie = ?"
				);
		doLogout.setString(1, cookie);
		int successfulLogout = doLogout.executeUpdate();
		if (successfulLogout > 0) {
			json.put("status", "OK");
		} else {
			json.put("status", "ERROR");
			json.put("message", "Logout was not successful.");
		}
		doLogout.close();
		sql.disconnect();
		return json;
	}


	static Boolean isAuthenticated(String cookie, String role) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
		SQL sql = new SQL();
		PreparedStatement getCookie = sql.connect()
				.prepareStatement(
				"SELECT cookie FROM users WHERE (cookie = ? AND role = ?)"
				);
		getCookie.setString(1, cookie);
		getCookie.setString(2, role);
		getCookie.execute();
		ResultSet rs = getCookie.getResultSet();
		Boolean hasCookie = rs.isBeforeFirst();
		sql.disconnect();
		return hasCookie;
	}


	static boolean isDataOwner(int id, String cookie) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		SQL sql = new SQL();
		PreparedStatement getUser = sql.connect()
				.prepareStatement(
				"SELECT cookie FROM users WHERE (id = ? AND cookie = ?)"
				);
		getUser.setInt(1, id);
		getUser.setString(2, cookie);
		getUser.execute();
		ResultSet rs = getUser.getResultSet();
		Boolean isRightUser = rs.isBeforeFirst();
		sql.disconnect();
		return isRightUser;
	}


	static boolean isValid(String type, int value) {
		switch (type) {
			case "rating":
				return value > 0 && value < 6;
			default:
				return false;
		}
	}

	static boolean isDoctorsPatient(String cookie, int patientID)throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException{
	    SQL sql = new SQL();
	    PreparedStatement getDoctorId = sql.connect().prepareStatement("SELECT * FROM users WHERE cookie = ?");
	    getDoctorId.setString(1, cookie);
	    getDoctorId.execute();
	    ResultSet getDoctorIdResult = getDoctorId.getResultSet();
	    getDoctorIdResult.next();
	    int doctorID = getDoctorIdResult.getInt("id");
	    PreparedStatement getPatientsDoctorId = sql.connect().prepareStatement("SELECT * FROM users WHERE id = ?");
	    getPatientsDoctorId.setInt(1, patientID);
	    getPatientsDoctorId.execute();
	    ResultSet getPatientsDoctorIdResult = getPatientsDoctorId.getResultSet();
	    getPatientsDoctorIdResult.next();
	    int patientsDoctorId = getPatientsDoctorIdResult.getInt("doctorID");
	    sql.disconnect();
	    return patientsDoctorId == doctorID;
    }
}