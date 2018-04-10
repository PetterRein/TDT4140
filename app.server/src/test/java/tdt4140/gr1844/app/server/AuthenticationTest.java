package tdt4140.gr1844.app.server;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import javax.naming.NamingException;


public class AuthenticationTest {
	private String salt;
	private Authentication authentication;

	@Before
	public void setUp() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		try {
			SQL.initDatabase();

		}
		catch(SQLException e){
			System.err.println(e);
			System.out.println("Setup failure");
		}
	}

	@Test
	public void loginTestCorrectCredentials() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
		Assert.assertEquals("OK", Authentication.login("haavard@email.com", "password").get("status").toString());
	}

	@Test
	public void loginTestWrongUsername() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
		Assert.assertEquals("ERROR", Authentication.login("wrongUsername", "password").getString("status"));
	}

	@Test
	public void loginTestWrongPassword() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
		Assert.assertEquals("ERROR", Authentication.login("haavard@email.com", "wrongPassword").getString("status"));
	}

	@Test
	public void logoutCorrectCookie() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
		String cookie = Authentication.login("haavard@email.com", "password").get("cookie").toString();
		Assert.assertEquals("OK", Authentication.logout(cookie).getString("status"));
	}

	@Test
	public void logoutWrongCookie() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
		Assert.assertEquals("ERROR", Authentication.logout("b").getString("status"));
	}
}
