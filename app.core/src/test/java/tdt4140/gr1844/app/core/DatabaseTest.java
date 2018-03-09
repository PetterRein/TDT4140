package tdt4140.gr1844.app.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
	@Before
	public void setUp() {
		SqlConnect conn = new SqlConnect();
		try {
			PreparedStatement statement1 = conn.connect(false).prepareStatement("drop table if exists users");
			statement1.execute();
			PreparedStatement statement2 = conn.connect(false).prepareStatement("create table users(id int, role varchar(64), name varchar(64), email varchar(64), passwordHash varchar(2000), salt varchar(256), cookie varchar(32), primary key(id))");
			statement2.execute();
			Database.addUser(false,"Doctor", "s", "email", "password", null);
			Database.addUser(false,"Admin","Per","admin@o.com","33", "41");
			PreparedStatement statement3 = conn.connect(false).prepareStatement("update users set cookie='a' where email = 'email'");
			statement3.executeUpdate();
		}
		catch(SQLException e){
			System.err.println(e);
			System.out.println("Setup failure");
		}
		catch(NamingException e) {
			
		}
	}
	
	@Test
	public void loginUserTest() {
		String email = "tom@doctor.com";
		String password = "password";
		Database.addUser(false,"Doctor", "Tom", email, password, "31");
		Assert.assertTrue(Authentication.login(false, email, password));
	}
	
	@Test
	public void getRoleFromCookieWhenCookieExists() {
		Assert.assertEquals("Doctor", Database.getRoleFromCookie(false,"a"));
	}
	
	@Test
	public void getRoleFromCookieWhenCookieDoesNotExist() {
		Assert.assertNull(Database.getRoleFromCookie(false,"doesNotExist"));
	}
	
}
