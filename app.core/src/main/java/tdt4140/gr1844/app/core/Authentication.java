package tdt4140.gr1844.app.core;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

public class Authentication {
	//The login function takes the username and password
	//It then checks if they are correct and returns true if they are
	//It returns false if username and password is incorrect, or if the username doesn't exist in the database
	public static boolean login(boolean onlineOrOffline, String username, String password) {
		SqlConnect conn = new SqlConnect();
		System.out.println("test5");
		try {
			if (onlineOrOffline){
				System.out.println("Test5.1");
				PreparedStatement statement1 = conn.connect(onlineOrOffline).prepareStatement("drop table if exists users");
				statement1.execute();
				System.out.println("test5.2");
				PreparedStatement statement2 = conn.connect(onlineOrOffline).prepareStatement("create table users(id int, email varchar(64), passwordHash varchar(2000), salt varchar(256), primary key(id))");
				statement2.execute();
				System.out.println("2test5.2");
				String username1 = "correctEmail";
				String password1 = "correctPassword";
				String salt1 = BCrypt.gensalt();
				System.out.println("3test5.2");
				String passwordHash1 = BCrypt.hashpw(password1, salt1);
				PreparedStatement statement3 = conn.connect(onlineOrOffline).prepareStatement("insert into users values(1, ?, ?, ?)");
				System.out.println("test5.2");
				statement3.setString(1, username1);
				statement3.setString(2, passwordHash1);
				statement3.setString(3, salt1);
				statement3.execute();
			}


		    System.out.println("test6");
			//Retrieve the users salt from the database, if no salt is returned then the user doesn't exist
			PreparedStatement saltRetrieval = conn.connect(onlineOrOffline).prepareStatement("select salt from users where email = ?");
			System.out.println(saltRetrieval.getResultSet());
			System.out.println("test6.2");
			saltRetrieval.setString(1, username);
			saltRetrieval.execute();
			ResultSet retrievedSalt = saltRetrieval.getResultSet();
			
			//Check if any salt was returned by the query. If no salt was returned then the user doesn't exst and return false
			if (!retrievedSalt.isBeforeFirst()) {
				System.out.println("Email error error");
				return false;
			}
			retrievedSalt.next();
            //use salt and password to make a password hash
			String salt = retrievedSalt.getString("salt");
			String passwordHash = BCrypt.hashpw(password, salt);
			
			saltRetrieval.close();
			//Make a query that checks if the user and password is valid
			System.out.println("Hang");
			PreparedStatement authenticationQuery = conn.connect(onlineOrOffline).prepareStatement("select * from users where (email = ? and passwordHash = ?)");
			authenticationQuery.setString(1, username);
			authenticationQuery.setString(2, passwordHash);
			authenticationQuery.execute();
			ResultSet authenticationResponse = authenticationQuery.getResultSet();
			//If query returned any results then username and password were correct and returns true, else it returns false
			Boolean validLogin = authenticationResponse.isBeforeFirst();
			authenticationQuery.close();
			conn.disconnect();
			System.out.println("test6.1");
			return validLogin;
		}
		catch(SQLException e){
			//conn.disconnect();
			System.out.println("test7");
			System.err.println(e);
			return false;
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Boolean logout(String cookie) {
		SqlConnect conn = new SqlConnect();
		try {
			System.out.println(cookie);
			PreparedStatement statement = conn.connect(false).prepareStatement("update users set cookie = null where cookie = ?");
			statement.setString(1, cookie);
			int updateCheck = statement.executeUpdate();
			//Returns true if something was updated
			return updateCheck > 0;
		}
		catch(SQLException e) {
			System.err.println(e);
			return false;
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return true;
	}
}