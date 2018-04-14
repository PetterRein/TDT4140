package tdt4140.gr1844.app.server;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NamingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseTest {
	@Before
	public void setUp() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
		SQL.initDatabase();
	}
	
	@Test
	public void addUserTest() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException {
		String email = "tom@doctor.com";
		String password = "password";
		Create.createPatient("tom", email, password, 2);
		Assert.assertEquals("OK", Authentication.login(email, password).get("status").toString());
	}

	@Test
	public void deleteUserTest() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
		String email = "tom@patient.com";
		String password = "password";
		SQL sql = new SQL();
		Create.createPatient("tom", email, password, 2);
		Create.createAdminTestPurpose();
		int idPatient = Authentication.login(email,password).getJSONObject("user").getInt("userID");
		String cookieAdmin = Authentication.login("admin@email.com", "password").getString("cookie");
		Delete.deleteUser(idPatient, cookieAdmin);
		PreparedStatement statement = sql.connect().prepareStatement("SELECT * FROM users WHERE email = ?");
		statement.setString(1, email);
		statement.execute();
		ResultSet rs = statement.getResultSet();
		Assert.assertFalse(rs.isBeforeFirst());
	}

	@Test
	public void deleteUserNoneExistingUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		String cookieAdmin = Authentication.login("admin@email.com", "password").getString("cookie");
		Delete.deleteUser(1, cookieAdmin).getString("status");
		Assert.assertEquals("ERROR", Delete.deleteUser(1, cookieAdmin).getString("status"));
	}

	@Test
	public void deleteUserNonePower() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		String cookieAdmin = Authentication.login("petter@email.com", "password").getString("cookie");
		Assert.assertEquals("ERROR", Delete.deleteUser(1, cookieAdmin).getString("status"));
	}

	@Test
	public void deleteFeelingValidUserAndFeelingExits() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		JSONObject userResponse = Authentication.login("haavard@email.com", "password");
		Create.createFeeling(userResponse.getJSONObject("user").getInt("userID"), 5, "Føler meg bra", userResponse.getString("cookie")).getString("status");
		Assert.assertEquals("OK", Delete.deleteFeeling(1,userResponse.getJSONObject("user").getInt("userID"), userResponse.getString("cookie")).getString("status"));
	}

	@Test
	public void deleteAlreadyDeletedFeeling() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		JSONObject userResponse = Authentication.login("haavard@email.com", "password");
		Create.createFeeling(userResponse.getJSONObject("user").getInt("userID"), 5, "Føler meg bra", userResponse.getString("cookie")).getString("status");
		Delete.deleteFeeling(1,userResponse.getJSONObject("user").getInt("userID"), userResponse.getString("cookie")).getString("status");
		Assert.assertEquals("ERROR", Delete.deleteFeeling(1,userResponse.getJSONObject("user").getInt("userID"), userResponse.getString("cookie")).getString("status"));
	}

	@Test
	public void deleteFeelingInvalidUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		JSONObject userResponse = Authentication.login("haavard@email.com", "password");
		JSONObject userResponse2 = Authentication.login("petter@email.com", "password");
		Assert.assertEquals("ERROR", Delete.deleteFeeling(1,userResponse.getJSONObject("user").getInt("userID"), userResponse2.getString("cookie")).getString("status"));
	}
	
	@Test
	public void tryToAddAdminAssPatient() throws IllegalAccessException, ClassNotFoundException, InstantiationException, NamingException, SQLException {
		String cookiePatient = Authentication.login("haavard@email.com", "password").get("cookie").toString();
		String result = Create.createAdminOrDoctor("per", "per", "per", "admin", cookiePatient).getString("status");
		Assert.assertEquals("ERROR", result);
	}

}
