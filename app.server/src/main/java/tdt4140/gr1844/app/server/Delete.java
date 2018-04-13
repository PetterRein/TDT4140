package tdt4140.gr1844.app.server;

import org.json.JSONObject;
import java.sql.PreparedStatement;
import java.sql.SQLException;


class Delete {


    /**
     * Deletes a user from the database by their e-mail address.
     * @param userID The id of the user to be deleted.
     * @param cookie The cookie of the logged in person. It must be any of the admins
     */
    static JSONObject deleteUser(int userID, String cookie) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject response = new JSONObject();
        if (Authentication.isAuthenticated(cookie, "admin") || Authentication.isDataOwner(userID, cookie)) {
            SQL sql = new SQL();
            PreparedStatement statement = sql.connect().prepareStatement("DELETE FROM users WHERE id = ?");
            statement.setInt(1, userID);
            Boolean isDeleted = statement.executeUpdate() > 0;
            if (isDeleted) {
                response.put("status", "OK");
            } else {
                response.put("status", "ERROR");
                response.put("message", "The user could not be deleted.");

            }
            sql.disconnect();
        } else {
            response.put("status", "ERROR");
            response.put("message", "You are not authorized for that action.");
        }
        return response;
    }


    /**
     * Deletes a feeling.
     * @param feelingID The id of the feeling to be deleted.
     * @param cookie The cookie of the logged in person.
     *               It must be the same user who created the feeling.
     */
    static JSONObject deleteFeeling(int feelingID, int patientID, String cookie) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject response = new JSONObject();
        if (Authentication.isDataOwner(patientID, cookie)) {
            SQL sql = new SQL();
            PreparedStatement statement = sql.connect()
                    .prepareStatement(
                    "DELETE FROM ratings WHERE id = ?"
                    );
            statement.setInt(1, feelingID);
            Boolean isDeleted = statement.executeUpdate() > 0;
            if (isDeleted) {
                response.put("status", "OK");
            } else {
                response.put("status", "ERROR");
                response.put("message", "The feeling could not be deleted.");

            }
            sql.disconnect();
        } else {
            response.put("status", "ERROR");
            response.put("message", "You are not authorized for that action.");
        }
        return response;
    }




}
