package tdt4140.gr1844.app.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class Authentication {
	//The login function takes the username and password
	//It then checks if they are correct and returns true if they are
	//It returns false if username and password is incorrect, or if the username doesn't exist in the database
	public static boolean login(String username, String password) {
		SqlConnect conn = new SqlConnect();
		try {
			//Retrieve the users salt from the database, if no salt is returned then the user doesn't exist
			PreparedStatement saltRetrieval = conn.connect().prepareStatement("select salt from users where email = ?");
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
			PreparedStatement authenticationQuery = conn.connect().prepareStatement("select * from users where (email = ? and passwordHash = ?)");
			authenticationQuery.setString(1, username);
			authenticationQuery.setString(2, passwordHash);
			authenticationQuery.execute();
			ResultSet authenticationResponse = authenticationQuery.getResultSet();
			//If query returned any results then username and password were correct and returns true, else it returns false
			Boolean validLogin = authenticationResponse.isBeforeFirst();
			authenticationQuery.close();
			conn.disconnect();
			return validLogin;
		}
		catch(SQLException e){
			conn.disconnect();
			System.err.println(e);
			return false;
		}
	}
}