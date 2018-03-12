package tdt4140.gr1844.app.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NamingException;

public class DatabaseTest {
	boolean onlineOrOffline = false;
	
	@Before
	public void setUp() throws NamingException {
		SqlConnect conn = new SqlConnect();
		try {
			PreparedStatement statement1 = conn.connect(onlineOrOffline).prepareStatement("drop table if exists users");
			statement1.execute();
			PreparedStatement statement2 = conn.connect(onlineOrOffline).prepareStatement("create table users(id int, role varchar(64), name varchar(64), email varchar(64), passwordHash varchar(2000), salt varchar(256), primary key(id))");
			statement2.execute();
		}
		catch(SQLException e){
			System.err.println(e);
			System.out.println("Setup failure");
		}
	}
	
	@Test
	public void addUserTest() throws NamingException {
		String email = "tom@doctor.com";
		String password = "password";
		Database.createUser("Doctor", "Tom", email, password, onlineOrOffline);
		Assert.assertTrue(Authentication.login(onlineOrOffline, email, password));
	}

	@Test
	public void deleteUserTest() {
		String email = "tom@doctor.com";
		SqlConnect conn = new SqlConnect();
		try {
			Database.createUser("role", "name", email, "password", false);
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
}
