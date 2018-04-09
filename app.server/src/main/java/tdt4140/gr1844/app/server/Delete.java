package tdt4140.gr1844.app.server;

import org.json.JSONObject;
import java.sql.PreparedStatement;
import java.sql.SQLException;


class Delete {


    /**
     * Deletes a user from the database by their e-mail address.
     * @param userId The id of the user to be deleted.
     * @param cookie The cookie of the logged in person. It must be any of the admins
     */
    static JSONObject deleteUser(int userId, String cookie) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject response = new JSONObject();
        if (Authentication.isAuthenticated(cookie, "admin") || Authentication.isDataOwner(userId, cookie)) {
            SQL conn = new SQL();
            PreparedStatement statement = conn.connect().prepareStatement("DELETE FROM users WHERE id = ?");
            statement.setInt(1, userId);
            Boolean isDeleted = statement.executeUpdate() > 0;
            if (isDeleted) {
                response.put("status", "OK");
            } else {
                response.put("status", "ERROR");
                response.put("message", "The user could not be deleted.");

            }
            conn.disconnect();
        } else {
            response.put("status", "ERROR");
            response.put("message", "You are not authorized for that action.");
        }
        return response;
    }
}
