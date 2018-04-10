package tdt4140.gr1844.app.server;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;


public class AuthenticationTest {

	@Before
	public void setUp() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
		SQL.initDatabase();
	}

	@Test
	public void loginTestCorrectCredentials() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException {
		Assert.assertEquals("OK", Authentication.login("haavard@email.com", "password").get("status"));
	}

	@Test
	public void loginTestWrongUsername() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException {
		Assert.assertEquals("ERROR", Authentication.login("wrongUsername", "password").getString("status"));
	}

	@Test
	public void loginTestWrongPassword() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException {
		Assert.assertEquals("ERROR", Authentication.login("haavard@email.com", "wrongPassword").getString("status"));
	}

	@Test
	public void logoutCorrectCookie() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException {
		String cookie = Authentication.login("haavard@email.com", "password").get("cookie").toString();
		Assert.assertEquals("OK", Authentication.logout(cookie).getString("status"));
	}

	@Test
	public void logoutWrongCookie() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException {
		Assert.assertEquals("ERROR", Authentication.logout("b").getString("status"));
	}
}
