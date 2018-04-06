package tdt4140.gr1844.app.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static javax.swing.text.html.HTML.Tag.HEAD;

public class DatabaseTest {
    boolean onlineOrOffline = false;
	@Before
	public void setUp() throws NamingException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		try {
			Database.initDatabase();
			Database.createUser(false,"Admin","Per","admin@o.com","33", null);

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
		Database.createUser(onlineOrOffline,"Doctor", "Tom", email, password, null);
		Assert.assertTrue(Authentication.login(onlineOrOffline, email, password));
	}

	@Test
	public void deleteUserTest() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
		String email = "tom@doctor.com";
		SqlConnect conn = new SqlConnect();
		Database.createUser(false,"role", "name", email, "password", null);
		Database.deleteUser(email, false);
		PreparedStatement statement = conn.connect(false).prepareStatement("select * from users where email = ?");
		statement.setString(1, email);
		statement.execute();
		ResultSet rs = statement.getResultSet();
		Assert.assertFalse(rs.isBeforeFirst());

	}

	
	@Test
	public void loginUserTest() throws NamingException, IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException {
		String email = "tom@doctor.com";
		String password = "password";
		Database.createUser(false,"Doctor", "Tom", email, password, null);
		Assert.assertTrue(Authentication.login(false, email, password));
	}
	
	@Test
	public void getRoleFromCookieWhenCookieExists() throws IllegalAccessException, ClassNotFoundException, InstantiationException, NamingException {
		Assert.assertEquals("Doctor", Database.getRoleFromCookie(false,"a"));
	}
	
	@Test
	public void getRoleFromCookieWhenCookieDoesNotExist() throws IllegalAccessException, ClassNotFoundException, InstantiationException, NamingException {
		Assert.assertNull(Database.getRoleFromCookie(false,"doesNotExist"));
	}
}
