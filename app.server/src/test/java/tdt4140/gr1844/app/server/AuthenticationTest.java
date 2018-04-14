package tdt4140.gr1844.app.server;


import org.json.JSONObject;
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

	@Test
	public void isAisAuthenticated() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		JSONObject userLoginResponse = Authentication.login("haavard@email.com", "password");
		Assert.assertTrue(Authentication.isAuthenticated(userLoginResponse.getString("cookie"), userLoginResponse.getJSONObject("user").getString("role")));
	}

	@Test
	public void loginTwice() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		Authentication.login("haavard@email.com", "password").get("status");
		String cookie = Authentication.login("haavard@email.com", "password").get("cookie").toString();
		Assert.assertEquals("OK", Authentication.logout(cookie).getString("status"));

	}

	@Test
	public void logoutTwice() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		String cookie = Authentication.login("haavard@email.com", "password").get("cookie").toString();
		Authentication.logout(cookie).getString("status");
		Assert.assertEquals("ERROR", Authentication.logout(cookie).getString("status"));

	}

	@Test
	public void validOwner() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		JSONObject userResponse = Authentication.login("haavard@email.com", "password");
		Assert.assertTrue(Authentication.isDataOwner(userResponse.getJSONObject("user").getInt("userID"), userResponse.getString("cookie")));
	}

	@Test
	public void isValidRating(){
		Assert.assertTrue(Authentication.isValid("rating", 5));
	}

	@Test
	public void isValidDefault(){
		Assert.assertFalse(Authentication.isValid("default", 5));
	}

	@Test
	public void isDoctorsPatient() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		JSONObject userResponse = Authentication.login("petter@email.com", "password");
		System.out.println(userResponse);
		Assert.assertTrue(Authentication.isDoctorsPatient(userResponse.getString("cookie"),3));
	}
}
