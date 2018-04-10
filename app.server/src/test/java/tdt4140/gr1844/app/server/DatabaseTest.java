package tdt4140.gr1844.app.server;

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
		String email = "tom@doctor.com";
		String password = "password";
		SQL conn = new SQL();
		Create.createPatient("tom", email, password, 2);
		Create.createAdminTestPropse("tom", "tom", "tom", 1);
		String cookieAdmin = Authentication.login("tom", "tom").get("cookie").toString();
		int idPatient = Authentication.login(email,password).getJSONObject("user").getInt("userId");
		Delete.deleteUser(idPatient, cookieAdmin);
		PreparedStatement statement = conn.connect().prepareStatement("SELECT * FROM users WHERE email = ?");
		statement.setString(1, email);
		statement.execute();
		ResultSet rs = statement.getResultSet();
		Assert.assertFalse(rs.isBeforeFirst());

	}

	
	@Test
	public void tryToAddAdminAssPatient() throws IllegalAccessException, ClassNotFoundException, InstantiationException, NamingException, SQLException {
		String cookiePatient = Authentication.login("haavard@email.com", "password").get("cookie").toString();
		String result = Create.createAdminOrDoctor("per", "per", "per", "admin", cookiePatient).getString("status");
		Assert.assertEquals("ERROR", result);
	}

}
