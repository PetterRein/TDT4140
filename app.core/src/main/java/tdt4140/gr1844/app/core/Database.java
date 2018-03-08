package tdt4140.gr1844.app.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.mindrot.jbcrypt.BCrypt;

public class Database {
	public static void addUser(String role, String name, String email, String password) {
		String salt = BCrypt.gensalt();
		String passwordHash = BCrypt.hashpw(password, salt);
		try {
			SqlConnect conn = new SqlConnect();
			PreparedStatement statement1 = conn.connect(false).prepareStatement("insert into users(role, name, email, passwordHash, salt) values(?, ?, ?, ?, ?)");
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
		catch(NamingException e) {
			
		}
	}
	
	public static String getRoleFromCookie(String cookie) {
		String role = null;
		try {
			SqlConnect conn = new SqlConnect();
			PreparedStatement statement1 = conn.connect(false).prepareStatement("select role from users where cookie = ?");
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
}
