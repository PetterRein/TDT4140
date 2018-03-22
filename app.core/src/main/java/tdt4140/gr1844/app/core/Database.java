package tdt4140.gr1844.app.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import javax.naming.NamingException;


/**
 * Contains the methods for handling/mutating the database.
 **/
import java.sql.ResultSet;
import java.util.ArrayList;

public class Database {
	public static void createUser(boolean onlineOrOffline, String role, String name, String email, String password) throws NamingException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		String salt = BCrypt.gensalt();
		String passwordHash = BCrypt.hashpw(password, salt);
		try {
			/**
			 * Todo legg inn sjekk på bruker allerede er lagt til
			 */
			SqlConnect conn = new SqlConnect();
			PreparedStatement statement1 = conn.connect(onlineOrOffline).prepareStatement("insert into users(role, name, email, passwordHash, salt) values(?, ?, ?, ?, ?)");
			statement1.setString(1, role);
			statement1.setString(2, name);
			statement1.setString(3, email);
			statement1.setString(4, passwordHash);
			statement1.setString(5, salt);
			statement1.executeUpdate();
			conn.disconnect();
		}
		catch(SQLException e) {
			System.err.println(e);
		}
	}

	/**
	 * Deletes a user from the database by their e-mail address.
	 * @param email The e-mail address of the user to be deleted.
	 * @param onlineOrOffline If the database is online or offline.
	 */
	public static void deleteUser(String email, boolean onlineOrOffline) throws NamingException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        try {
			/**
			 * Todo legg til sjekk som sjekker om bruker finnes før vi sletter null
			 */
			SqlConnect conn = new SqlConnect();
            PreparedStatement statement = conn.connect(onlineOrOffline).prepareStatement("delete from users where email=?");
            statement.setString(1, email);
            statement.executeUpdate();
            conn.disconnect();
        }
        catch(SQLException e) {
            System.err.println(e);
        }
	}

    /**
     *
     * @param onlineOrOffline Check of database is online or offline
     * @param cookie Users cookie you want to check the role on
     * @return Returns the users role
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
	public static String getRoleFromCookie(boolean onlineOrOffline, String cookie) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		String role = null;
		try {
			/**
			 * Todo legg til sjekk som sjekker om bruker har en role
			 */
			SqlConnect conn = new SqlConnect();
			PreparedStatement statement1 = conn.connect(onlineOrOffline).prepareStatement("select role from users where cookie = ?");
			statement1.setString(1, cookie);
			statement1.execute();
			ResultSet rs = statement1.getResultSet();
			rs.next();
			role = rs.getString("role");
			conn.disconnect();
		}
		catch(SQLException e) {
			
		}
		catch(NamingException e) {
			
		}
		return role;
	}

	public static ArrayList<String> getDoctorsPatients(boolean onlineOrOffline, String email)throws NamingException, IllegalAccessException, InstantiationException, ClassNotFoundException{
        SqlConnect conn = new SqlConnect();
        try {
            PreparedStatement statement1 = conn.connect(onlineOrOffline).prepareStatement("select id from users where email = ?");
            statement1.setString(1, email);
            statement1.execute();
            ResultSet rs1 = statement1.getResultSet();
            rs1.next();
            int doctorId = rs1.getInt("id");

            PreparedStatement statement2 = conn.connect(onlineOrOffline).prepareStatement("select distinct name, email from PatientData\n" +
                    "inner join users on id = patientID\n" +
                    "where doctorID = ?");
            statement2.setInt(1, doctorId);
            statement2.execute();
            ResultSet rs2 = statement2.getResultSet();
            ArrayList<String> result = new ArrayList<String>();
            while(rs2.next()) {
                result.add(rs2.getString("name") + " " + rs2.getString("email"));
            }
            return result;
        }
        catch(SQLException e) {

        }
        catch(NamingException e) {

        }
        return null;
    }

    public static ArrayList<String> getNLastPatientData(boolean onlineOrOffline, String email, int n)throws NamingException, IllegalAccessException, InstantiationException, ClassNotFoundException{
        SqlConnect conn = new SqlConnect();
        try {
            PreparedStatement statement1 = conn.connect(onlineOrOffline).prepareStatement("select id from users where email = ?");
            statement1.setString(1, email);
            statement1.execute();
            ResultSet rs1 = statement1.getResultSet();
            rs1.next();
            int patientId = rs1.getInt("id");

            PreparedStatement statement2 = conn.connect(onlineOrOffline).prepareStatement("select rating, extraInfo, timestamp from PatientData\n" +
                    "where patientId = ? order by timestamp desc limit ?");
            statement2.setInt(1, patientId);
            statement2.setInt(2, n);
            statement2.execute();
            ResultSet rs2 = statement2.getResultSet();
            ArrayList<String> result = new ArrayList<String>();
            while(rs2.next()) {
                result.add(rs2.getString("rating") + " " + rs2.getString("extraInfo") + " " + rs2.getTimestamp("timeStamp").toString());
            }
            return result;
        }
        catch(SQLException e) {

        }
        catch(NamingException e) {

        }
        return null;
    }

	/**
	 *
	 * @param onlineOrOffline Check if database is online or offline
	 * @param userName email on user you want data for
	 * @return A ArrayList<String> or a ArrayList<Int> Array that contains all data on the requestet user
	 */
	public static ArrayList<String> getAllDataOnUser(boolean onlineOrOffline, String userName){
		SqlConnect conn = new SqlConnect();
        ArrayList<String> arrayWithData = null;
        return arrayWithData;
	}

	public static boolean addDataToUser(boolean onlineOrOffline, String data, int rating, String userName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NamingException {
        /**
         * Create timestap
         * Add data to right place
         * Create a primarykey
         */
        try {
            SqlConnect conn = new SqlConnect();
            int patientID = getIdByUsername(false, userName);
            PreparedStatement preparedStatement2 = conn.connect(false).prepareStatement("INSERT INTO patientData" +
                    "(patientID, doctorID, rating, extrainfo) VALUES (?, ?, ?, ?) ");
            preparedStatement2.setInt(1, patientID);
            preparedStatement2.setInt(2, doctorID);
            preparedStatement2.setInt(3, rating);
            preparedStatement2.setString(4, data);
            preparedStatement2.executeUpdate();
            conn.disconnect();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return false;

    }
    public static int getIdByUsername(boolean onlineOrOffline, String userName) {
	    try {
            SqlConnect conn = new SqlConnect();
            PreparedStatement preparedStatement1 = conn.connect(false).prepareStatement("SELECT id FROM users WHERE name = ?");
            preparedStatement1.setString(1,userName);
            ResultSet rs1 = preparedStatement1.executeQuery();
            rs1.next();
            return rs1.getInt("id");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean delDataFromUser(boolean onlineOrOffline, int primaryKey, String userName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NamingException {
        /**
         * Del the data from the primarykey on that user from userName
         */
        try {
            SqlConnect conn = new SqlConnect();
            PreparedStatement preparedStatement = conn.connect(false).prepareStatement("DELETE FROM patientData WHERE id = ?");
            preparedStatement.setInt(1, primaryKey);
            preparedStatement.executeUpdate();
            conn.disconnect();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return false;
    }

    public static boolean delDataInArray(boolean onlineOrOffline, int[] primaryKeys, String userName) throws ClassNotFoundException, NamingException, InstantiationException, IllegalAccessException {
	    boolean doneOrNot = false;
	    for (int i = 0; i < primaryKeys.length; i++){
	        doneOrNot = delDataFromUser(onlineOrOffline, primaryKeys[i], userName);
        }
        return doneOrNot;
    }

    public static boolean delAllDataFromUser(boolean onlineOrOffline, String userName){
        /**
         * Get all primarykeys on data put into Int[]
         * Then send to delDataInArray
         */
        return false;
    }
}
