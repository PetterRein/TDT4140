package tdt4140.gr1844.app.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import javax.naming.NamingException;


/**
 * Contains the methods for handling/mutating the database.
 */
public class Database {

	/**
	 * Adds a new user to the database.
	 * @param role The user's role.
	 * @param name The user's name.
	 * @param email The user's email address.
	 * @param password The user's password.
	 * @param onlineOrOffline If the database is online or offline.
	 */
	public static void createUser(String role, String name, String email, String password, boolean onlineOrOffline) throws NamingException {
		String salt = BCrypt.gensalt();
		String passwordHash = BCrypt.hashpw(password, salt);
		try {
			SqlConnect conn = new SqlConnect();
			PreparedStatement statement1 = conn.connect(onlineOrOffline).prepareStatement("insert into users(role, name, email, passwordHash, salt) values(?, ?, ?, ?, ?)");
			statement1.setString(1, role);
			statement1.setString(2, name);
			statement1.setString(3, email);
			statement1.setString(4, passwordHash);
			statement1.setString(5, salt);
			statement1.executeUpdate();
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
	public static void deleteUser(String email, boolean onlineOrOffline) throws NamingException {
        try {
            SqlConnect conn = new SqlConnect();
            PreparedStatement statement = conn.connect(onlineOrOffline).prepareStatement("delete from users where email=?");
            statement.setString(1, email);
            statement.executeUpdate();
        }
        catch(SQLException e) {
            System.err.println(e);
        }
        catch(NamingException e){
            System.err.println(e);
        }
	}
}
