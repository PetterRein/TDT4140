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
     * TODO: Make the patients be able to see their own data
     */
    static JSONObject getPatientData(int patientID, String orderBy, String cookie) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject response = new JSONObject();
        if (Authentication.isAuthenticated(cookie, "doctor") || Authentication.isDataOwner(patientID, cookie)) {
            SQL conn = new SQL();
            PreparedStatement statement = conn.connect()
                    .prepareStatement(
                            "SELECT * FROM patientData " +
                                    "WHERE patientID = " + patientID + " " +
                                    "ORDER BY timestamp " + orderBy);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            response = SQLToJSONArray(rs, "feelings");
            conn.disconnect();
            return response;
        } else {
            response.put("status", "ERROR");
            response.put("message", "You are not authorized to retrieve this information");
            return response;
        }
    }
}
