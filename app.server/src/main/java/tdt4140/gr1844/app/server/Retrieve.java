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
     */
    static JSONObject listFeelings(int patientID, String orderBy, String cookie) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject response = new JSONObject();

        if ((Authentication.isAuthenticated(cookie, "doctor") && Authentication.isDoctorsPatient(cookie, patientID)) || Authentication.isDataOwner(patientID, cookie)) {
            SQL sql = new SQL();
            String query;
            if (orderBy.equals("asc")) {
                query = "SELECT * FROM patientData WHERE patientID = ? ORDER BY timestamp ASC";
            } else {
                query = "SELECT * FROM patientData WHERE patientID = ? ORDER BY timestamp DESC";
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
        SQL sql2 = new SQL();
        PreparedStatement statement2 = sql2.connect().prepareStatement("SELECT * FROM users WHERE id=? and cookie=?");
        statement2.setInt(1, doctorID);
        statement2.setString(2, cookie);
        statement2.execute();
        ResultSet rs2 = statement2.getResultSet();
        boolean valid = rs2.isBeforeFirst();
        sql2.disconnect();


        if (Authentication.isAuthenticated(cookie, "doctor") && valid) {
            SQL sql = new SQL();
            PreparedStatement statement = sql.connect()
                    .prepareStatement(
                    "SELECT id, name, email FROM users WHERE (role = ? AND doctorID = ?)"
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


    static JSONObject listPatients(String cookie) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject response = new JSONObject();

        if (Authentication.isAuthenticated(cookie, "admin")) {
            SQL sql = new SQL();
            PreparedStatement statement = sql.connect()
                    .prepareStatement(
                            "SELECT id, name, email FROM users"
                    );
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

    static JSONObject listFeedbacks(String isRead, String cookie) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject response = new JSONObject();
        if (Authentication.isAuthenticated(cookie, "admin")) {
            if (isRead.equals("true") || isRead.equals("false")){
                SQL sql = new SQL();
                String query = null;
                if (isRead.equals("true")) {
                    query = "SELECT * FROM feedbacks WHERE isRead = 1";
                }
                else if(isRead.equals("false")){
                    query = "SELECT * FROM feedbacks WHERE isRead = 0 OR isRead = null";
                }
                PreparedStatement statement = sql.connect()
                        .prepareStatement(query);
                statement.execute();
                ResultSet rs = statement.getResultSet();
                response = SQLToJSONArray(rs, "feedbacks");
                sql.disconnect();
            }
            else {
                response.put("status", "ERROR");
                response.put("message", "Invalid IsRead");
            }

        } else {
            response.put("status", "ERROR");
            response.put("message", "You are not authorized to retrieve this information");
        }
        return response;
    }
}
