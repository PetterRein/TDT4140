package tdt4140.gr1844.app.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;


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
	 */
	public static void createUser(String role, String name, String email, String password) {
		String salt = BCrypt.gensalt();
		String passwordHash = BCrypt.hashpw(password, salt);
		try {
			SqlConnect conn = new SqlConnect();
			PreparedStatement statement1 = conn.connect().prepareStatement("insert into users(role, name, email, passwordHash, salt) values(?, ?, ?, ?, ?)");
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

	public static void deleteUser(String email) {
        try {
            SqlConnect conn = new SqlConnect();
            PreparedStatement statement = conn.connect().prepareStatement("delete from users(email) where email='?'");
            statement.setString(1, email);
            statement.executeUpdate();
        }
        catch(SQLException e) {
            System.err.println(e);
        }
	}
}
