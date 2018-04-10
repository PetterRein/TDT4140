package tdt4140.gr1844.app.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
    boolean onlineOrOffline = false;
	@Before
	public void setUp() throws NamingException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		try {
			SQL.initDatabase();

		}
		catch(SQLException e){
			System.err.println(e);
			System.out.println("Setup failure");
		}
	}
	
	@Test
	public void addUserTest() throws NamingException, IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException {
		String email = "tom@doctor.com";
		String password = "password";
		Create.createPatient("tom", email, password, 2);
		Assert.assertTrue(Authentication.login(email, password).get("status").toString().equals("OK"));
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
		PreparedStatement statement = conn.connect().prepareStatement("select * from users where email = ?");
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
