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
	public void setUp() {
		SqlConnect conn = new SqlConnect();
		try {
			PreparedStatement statement1 = conn.connect(false).prepareStatement("drop table if exists users");
			statement1.execute();
			PreparedStatement statement2 = conn.connect(false).prepareStatement("create table users(id int, email varchar(64), passwordHash varchar(2000), salt varchar(256), primary key(id))");

			statement2.execute();
			String username = "correctEmail";
			String password = "correctPassword";
			salt = BCrypt.gensalt();
			String passwordHash = BCrypt.hashpw(password, salt);
			PreparedStatement statement3 = conn.connect(false).prepareStatement("insert into users values(1, ?, ?, ?)");
			statement3.setString(1, username);
			statement3.setString(2, passwordHash);
			statement3.setString(3, salt);
			statement3.execute();
		}
		catch(SQLException e){
			System.err.println(e);
			System.out.println("Setup failure");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void loginTestCorrectCredentials() { Assert.assertTrue(Authentication.login(false,"correctEmail", "correctPassword")); }
	
	@Test
	public void loginTestWrongUsername() {
		Assert.assertFalse(Authentication.login(false,"wrongUsername", "correctPassword"));
	}
	
	@Test
	public void loginTestWrongPassword() {
		Assert.assertFalse(Authentication.login(false,"correctUsername", "wrongPassword"));
	}
	
	@Test
	public void logoutCorrectCookie() {
		Assert.assertTrue(Authentication.logout("aaaa"));
	}
	
	@Test
	public void logoutWrongCookie() {
		Assert.assertFalse(Authentication.logout("b"));
	}
}
