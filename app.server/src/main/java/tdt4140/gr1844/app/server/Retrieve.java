package tdt4140.gr1844.app.server;

import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static tdt4140.gr1844.app.server.SQL.SQLToJSONArray;

class Retrieve {


    /**
     *
     * @param patientID The patient's id
     * @param orderBy Can be asc or desc
     * @param cookie The logged in user's cookie
     * @return {JSONObject}
     * @throws SQLException
     * TODO: Only your own doctor should see your data
     */
    static JSONObject listFeelings(int patientID, String orderBy, String cookie) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject response = new JSONObject();

        if (Authentication.isAuthenticated(cookie, "doctor") || Authentication.isDataOwner(patientID, cookie)) {
            SQL sql = new SQL();
            System.out.println("YO");
            String query;
            if (orderBy.equals("asc")) {
                query = "SELECT * FROM ratings WHERE patientID = ? ORDER BY timestamp ASC";
            } else {
                query = "SELECT * FROM ratings WHERE patientID = ? ORDER BY timestamp DESC";
            }
            PreparedStatement statement = sql.connect()
                    .prepareStatement(query);
            statement.setInt(1, patientID);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            response = SQLToJSONArray(rs, "feelings");
            sql.disconnect();
            return response;
        } else {
            response.put("status", "ERROR");
            response.put("message", "You are not authorized to retrieve this information");
            return response;
        }
    }

    /**
     *
     * @param doctorID The doctor's id
     * @param cookie The logged in user's cookie
     * @return {JSONObject}
     */
    static JSONObject listPatients(int doctorID, String cookie) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject response = new JSONObject();

        if (Authentication.isAuthenticated(cookie, "doctor")) {
            SQL sql = new SQL();
            PreparedStatement statement = sql.connect()
                    .prepareStatement(
                    "SELECT name, email FROM users WHERE (role = ? AND doctorID = ?)"
                    );
            statement.setString(1, "patient");
            statement.setInt(2, doctorID);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            response = SQLToJSONArray(rs, "patients");
            sql.disconnect();
        } else {
            response.put("status", "ERROR");
            response.put("message", "You are not authorized to retrieve this information");
        }
        return response;
    }
}
