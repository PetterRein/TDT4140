package tdt4140.gr1844.app.core;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import javax.naming.NamingException;


public class AuthenticationTest {
	private String salt;

	@Before
	public void setUp() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		SqlConnect conn = new SqlConnect();
		try {
			PreparedStatement statement1 = conn.connect().prepareStatement("drop table if exists users");
			statement1.execute();
			PreparedStatement statement2 = conn.connect().prepareStatement("create table users(id int, email varchar(64), passwordHash varchar(2000), salt varchar(256), cookie varchar(32), primary key(id))");
			statement2.execute();
			String username = "correctEmail";
			String password = "correctPassword";
			salt = BCrypt.gensalt();
			String passwordHash = BCrypt.hashpw(password, salt);
			PreparedStatement statement3 = conn.connect().prepareStatement("insert into users values(1, ?, ?, ?, 'aaaa')");
			statement3.setString(1, username);
			statement3.setString(2, passwordHash);
			statement3.setString(3, salt);
			statement3.execute();
		} catch (SQLException e) {
			System.err.println(e);
			System.out.println("Setup failure");
		}
	}

	@Test
	public void loginTestCorrectCredentials() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
		Assert.assertTrue(Authentication.login("correctEmail", "correctPassword"));
	}

	@Test
	public void loginTestWrongUsername() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
		Assert.assertFalse(Authentication.login("wrongUsername", "correctPassword"));
	}

	@Test
	public void loginTestWrongPassword() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
		Assert.assertFalse(Authentication.login("correctUsername", "wrongPassword"));
	}

	@Test
	public void logoutCorrectCookie() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
		Assert.assertTrue(Authentication.logout("aaaa"));
	}

	@Test
	public void logoutWrongCookie() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
		Assert.assertFalse(Authentication.logout("b"));
	}
}
