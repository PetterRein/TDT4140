package tdt4140.gr1844.app.server;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class RetrieveTest {

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        SQL.initDatabase();
    }

    @Test
    public void listPatientDoctor() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userResponse = Authentication.login("petter@email.com", "password");
        JSONObject response = Retrieve.listPatients(userResponse.getJSONObject("user").getInt("userID"), userResponse.getString("cookie"));
        Assert.assertEquals("Haavard", response.getJSONArray("patients").getJSONObject(0).getString("name"));
    }

    @Test
    public void listPatientInvalidDoctor() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        Assert.assertEquals("You are not authorized to retrieve this information",Retrieve.listPatients(2, "2").getString("message"));
    }

    @Test
    public void listPatientInvalidAdmin() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        Assert.assertEquals("You are not authorized to retrieve this information",Retrieve.listPatients("2").getString("message"));
    }

    @Test
    public void listPatientValidAdmin() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject loginAdmin = Authentication.login("admin@email.com", "password");
        Assert.assertEquals("Admin",Retrieve.listPatients(loginAdmin.getString("cookie")).getJSONArray("patients").getJSONObject(0).getString("name"));
    }

    @Test
    public void listFeelingsASCValidDoctor() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userResponse = Authentication.login("petter@email.com", "password");
        JSONObject patientLogin = Authentication.login("haavard@email.com", "password");
        Create.createFeeling(patientLogin.getJSONObject("user").getInt("userID"), 5, "hei", patientLogin.getString("cookie"));
        JSONObject feelings = Retrieve.listFeelings(3,"asc", userResponse.getString("cookie"));
        Assert.assertEquals("hei", feelings.getJSONArray("feelings").getJSONObject(0).getString("message"));
    }

    @Test
    public void listFeelingsOtherValidDoctor() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userResponse = Authentication.login("petter@email.com", "password");
        JSONObject patientLogin = Authentication.login("haavard@email.com", "password");
        Create.createFeeling(patientLogin.getJSONObject("user").getInt("userID"), 5, "hei", patientLogin.getString("cookie"));
        JSONObject feelings = Retrieve.listFeelings(3,"desc", userResponse.getString("cookie"));
        Assert.assertEquals("hei", feelings.getJSONArray("feelings").getJSONObject(0).getString("message"));
    }

    @Test
    public void listFeelingsInvalidUser() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject feelings = Retrieve.listFeelings(3,"asc", "4");
        Assert.assertEquals("ERROR", feelings.getString("status"));
    }

    @Test
    public void listFeedbacksNotRead() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userResponse = Authentication.login("admin@email.com", "password");
        JSONObject doctorResponse = Authentication.login("petter@email.com", "password");
        Create.createFeedback("Hei kan du slette", doctorResponse.getString("cookie")).getString("status");
        JSONObject feedbacksNotRead = Retrieve.listFeedbacks("false", userResponse.getString("cookie"));
        Assert.assertEquals("Hei kan du slette", feedbacksNotRead.getJSONArray("feedbacks").getJSONObject(0).getString("message"));
    }


}
