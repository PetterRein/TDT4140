package tdt4140.gr1844.app.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class Authentication {
	public static boolean login(String username, String password) {
		SqlConnect conn = new SqlConnect();
		try {
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
            
			String salt = retrievedSalt.getString("salt");
			String passwordHash = BCrypt.hashpw(password, salt);
			
			saltRetrieval.close();
			
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