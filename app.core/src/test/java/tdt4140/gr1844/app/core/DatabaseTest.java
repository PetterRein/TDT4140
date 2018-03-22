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
		SqlConnect conn = new SqlConnect();
		try {
			PreparedStatement statement1 = conn.connect(false).prepareStatement("drop table if exists users");
			statement1.execute();
			PreparedStatement statement2 = conn.connect(onlineOrOffline).prepareStatement("create table users(id int, role varchar(64), name varchar(64), email varchar(64), passwordHash varchar(2000), salt varchar(256), cookie varchar(256), doctorID int, primary key(id))");
			statement2.execute();
			Database.createUser(false,"Doctor", "s", "email", "password", null);
			Database.createUser(false,"Admin","Per","admin@o.com","33", null);
			PreparedStatement statement3 = conn.connect(false).prepareStatement("update users set cookie='a' where email = 'email'");
			statement3.executeUpdate();
		}
		catch(SQLException e){
			System.err.println(e);
			System.out.println("Setup failure");
		}
	}
	
	@Test
	public void addUserTest() throws NamingException, IllegalAccessException, ClassNotFoundException, InstantiationException {
		String email = "tom@doctor.com";
		String password = "password";
		Database.createUser(onlineOrOffline,"Doctor", "Tom", email, password, null);
		Assert.assertTrue(Authentication.login(onlineOrOffline, email, password));
	}

	@Test
	public void deleteUserTest() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
		String email = "tom@doctor.com";
		SqlConnect conn = new SqlConnect();
		try {
			Database.createUser(false,"role", "name", email, "password", null);
			Database.deleteUser(email, false);
			PreparedStatement statement = conn.connect(false).prepareStatement("select * from users where email = ?");
			statement.setString(1, email);
			statement.execute();
			ResultSet rs = statement.getResultSet();
			Assert.assertFalse(rs.isBeforeFirst());
		}
		catch(NamingException e){
			System.err.println(e);
		}
		catch(SQLException e) {
			System.err.println(e);
		}
	}

	
	@Test
	public void loginUserTest() throws NamingException, IllegalAccessException, ClassNotFoundException, InstantiationException {
		String email = "tom@doctor.com";
		String password = "password";
		Database.createUser(false,"Doctor", "Tom", email, password, null);
		Assert.assertTrue(Authentication.login(false, email, password));
	}
	
	@Test
	public void getRoleFromCookieWhenCookieExists() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
		Assert.assertEquals("Doctor", Database.getRoleFromCookie(false,"a"));
	}
	
	@Test
	public void getRoleFromCookieWhenCookieDoesNotExist() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
		Assert.assertNull(Database.getRoleFromCookie(false,"doesNotExist"));
	}
}
