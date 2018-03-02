package tdt4140.gr1844.app.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
	@Before
	public void setUp() {
		SqlConnect conn = new SqlConnect();
		try {
			PreparedStatement statement1 = conn.connect().prepareStatement("drop table if exists users");
			statement1.execute();
			PreparedStatement statement2 = conn.connect().prepareStatement("create table users(id int, role varchar(64), name varchar(64), email varchar(64), passwordHash varchar(2000), salt varchar(256), primary key(id))");
			statement2.execute();
		}
		catch(SQLException e){
			System.err.println(e);
			System.out.println("Setup failure");
		}
	}
	
	@Test
	public void addUserTest() {
		String email = "tom@doctor.com";
		String password = "password";
		Database.addUser("Doctor", "Tom", email, password);
		Assert.assertTrue(Authentication.login(email, password));
	}
}
