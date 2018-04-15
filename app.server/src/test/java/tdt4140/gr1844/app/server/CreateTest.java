package tdt4140.gr1844.app.server;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class CreateTest {
    @Before
    public void setUp() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        SQL.initDatabase();
    }

    @Test
    public void createFeeling() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userResponse = Authentication.login("haavard@email.com", "password");
        Assert.assertEquals("OK",Create.createFeeling(userResponse.getJSONObject("user").getInt("userID"), 5, "Føler meg bra", userResponse.getString("cookie")).getString("status"));
    }

    @Test
    public void createFeedbackValidUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userResponse = Authentication.login("petter@email.com", "password");
        Assert.assertEquals("OK", Create.createFeedback("Hei kan du slette", userResponse.getString("cookie")).getString("status"));
    }

    @Test
    public void createFeedbackInvalidUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userResponse = Authentication.login("haavard@email.com", "password");
        Assert.assertEquals("ERROR", Create.createFeedback("Hei kan du slette", userResponse.getString("cookie")).getString("status"));
    }

    /**TODO Make getFeedbacks
    @Test
    public void feedbackReadValidUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userResponse = Authentication.login("admin@email.com", "password");
        Assert.assertEquals("OK", Create.markFeedbackRead("Hei kan du slette", userResponse.getString("cookie")).getString("status"));
    }

     @Test
     public void feedbackReadInvlidUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
     JSONObject userResponse = Authentication.login("haavard@email.com", "password");
     Assert.assertEquals("ERROR", Create.markFeedbackRead("Hei kan du slette", userResponse.getString("cookie")).getString("status"));
     }**/
}
