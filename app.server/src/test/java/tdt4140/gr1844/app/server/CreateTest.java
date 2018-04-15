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
        Assert.assertEquals("OK", Create.createFeeling(userResponse.getJSONObject("user").getInt("userID"), 5, "Føler meg bra", userResponse.getString("cookie")).getString("status"));
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

    @Test
    public void feedbackReadValidUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject adminResponse = Authentication.login("admin@email.com", "password");
        JSONObject doctorResponse = Authentication.login("petter@email.com", "password");
        Create.createFeedback("Hei kan du slette", doctorResponse.getString("cookie")).getString("status");
        JSONObject feedbacksNotRead = Retrieve.listFeedbacks("false", adminResponse.getString("cookie"));
        JSONObject response = Create.markFeedbackRead(feedbacksNotRead.getJSONArray("feedbacks").getJSONObject(0).getInt("id"), adminResponse.getString("cookie"));
        Assert.assertEquals("OK", response.getString("status"));
    }

    @Test
    public void feedbackReadInvlidUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject doctorResponse = Authentication.login("petter@email.com", "password");
        Create.createFeedback("Hei kan du slette", doctorResponse.getString("cookie")).getString("status");
        JSONObject response = Create.markFeedbackRead(1, "2");
        Assert.assertEquals("ERROR", response.getString("status"));
    }

    @Test
    public void updatePatientsDoctor() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userResponse = Authentication.login("haavard@email.com", "password");
        Assert.assertEquals("OK", Create.updatePatientsDoctor(userResponse.getJSONObject("user").getInt("userID"), 2, userResponse.getString("cookie")).getString("status"));

    }

    @Test
    public void updatePatientsDoctorInvalidCookie() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userResponse = Authentication.login("haavard@email.com", "password");
        Assert.assertEquals("ERROR", Create.updatePatientsDoctor(userResponse.getJSONObject("user").getInt("userID"), 2, "5").getString("status"));

    }

    @Test
    public void createAdminValid() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userResponse = Authentication.login("admin@email.com", "password");
        Assert.assertEquals("OK", Create.createAdmin("Test", "test@email.com", "password", 1, userResponse.getString("cookie"), "patient").getString("status"));
    }

    @Test
    public void createAdminInvalid() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        Assert.assertEquals("ERROR", Create.createAdmin("Test", "test@email.com", "password", 1, "3", "patient").getString("status"));
    }
}