package tdt4140.gr1844.app.server;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;


public class WebGetTest extends Mockito {

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException {
        SQL.initDatabase();
    }

    private JSONObject mockRequest(String QueryString) throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getQueryString()).thenReturn(QueryString);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new WebGet().doGet(request, response);
        JSONObject jsonObj = new JSONObject(stringWriter.toString());
        return jsonObj;
    }

    @Test
    public void doGetDefault() throws IOException {
        JSONObject defaultRequest = mockRequest("action=l");
        Assert.assertEquals("ERROR", defaultRequest.getString("status"));
    }


    @Test
    public void doGetLogin() throws IOException {
        JSONObject userLogin = mockRequest("action=login&email=haavard@email.com&password=password");
        Assert.assertEquals("OK", userLogin.getString("status"));
    }

    @Test
    public void doGetLogoutWrongCookie() throws IOException {
        JSONObject userLogout = mockRequest("action=logout&cookie=2");
        Assert.assertEquals("ERROR", userLogout.getString("status"));
    }

    @Test
    public void doGetCreatePatient() throws IOException {
        JSONObject userCreate = mockRequest("action=createPatient&email=test@email.com&password=password&name=Test&doctorID=2");
        Assert.assertEquals("OK", userCreate.getString("status"));
    }

    @Test
    public void doGetCreateAdminOrDoctor() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String cookie = Authentication.login("admin@email.com", "password").getString("cookie");
        JSONObject userCreateDoctor = mockRequest("action=createAdminOrDoctor&email=test1@email.com&password=password&name=Test&role=doctor&cookie=" + cookie);
        Assert.assertEquals("OK", userCreateDoctor.getString("status"));
    }

    @Test
    public void doGetDeleteUser() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String cookie = Authentication.login("admin@email.com", "password").getString("cookie");
        JSONObject deleteUser = mockRequest("action=deleteUser&userID=1&cookie=" + cookie);
        Assert.assertEquals("OK", deleteUser.getString("status"));
    }

    @Test
    public void doGetCreateFeeling() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userLogin = Authentication.login("haavard@email.com", "password");
        JSONObject createFeeling = mockRequest("action=createFeeling&patientID=" + userLogin.getJSONObject("user").getString("userID") +  "&cookie=" + userLogin.getString("cookie") + "&rating=5&message=hei");
        Assert.assertEquals("OK", createFeeling.getString("status"));
    }

    @Test
    public void doGetDeleteFeeling() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userLogin = Authentication.login("haavard@email.com", "password");
        mockRequest("action=createFeeling&patientID=" + userLogin.getJSONObject("user").getString("userID") +  "&cookie=" + userLogin.getString("cookie") + "&rating=5&message=hei");
        JSONObject deleteFeeling = mockRequest("action=deleteFeeling&patientID=" + userLogin.getJSONObject("user").getString("userID") +  "&cookie=" + userLogin.getString("cookie") + "&feelingID=1");
        Assert.assertEquals("OK", deleteFeeling.getString("status"));
    }

    @Test
    public void doGetListFeeling() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userLogin = Authentication.login("haavard@email.com", "password");
        mockRequest("action=createFeeling&patientID=" + userLogin.getJSONObject("user").getString("userID") +  "&cookie=" + userLogin.getString("cookie") + "&rating=5&message=hei");
        JSONObject listFeeling = mockRequest("action=listFeelings&patientID=" + userLogin.getJSONObject("user").getString("userID") +  "&cookie=" + userLogin.getString("cookie") + "&orderBy=asc");
        Assert.assertEquals("hei", listFeeling.getJSONArray("feelings").getJSONObject(0).getString("message"));
    }

    @Test
    public void doGetCreateFeedback() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userLogin = Authentication.login("petter@email.com", "password");
        JSONObject createFeedback = mockRequest("action=createFeedback&" + "&cookie=" + userLogin.getString("cookie") + "&message=test");
        Assert.assertEquals("OK", createFeedback.getString("status"));
    }

    @Test
    public void doGetMarkFeedbackRead() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userLogin = Authentication.login("petter@email.com", "password");
        mockRequest("action=createFeedback&" + "&cookie=" + userLogin.getString("cookie") + "&message=test");
        JSONObject adminLogin = Authentication.login("admin@email.com", "password");
        JSONObject markFeedbackRead = mockRequest("action=markFeedbackRead&" + "&cookie=" + adminLogin.getString("cookie") + "&feedbackID=1");
        Assert.assertEquals("OK", markFeedbackRead.getString("status"));
    }

    @Test
    public void doGetListPatients() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userLogin = Authentication.login("petter@email.com", "password");
        JSONObject createFeedback = mockRequest("action=listPatients&" + "&cookie=" + userLogin.getString("cookie") + "&doctorID=" + userLogin.getJSONObject("user").getString("userID"));
        Assert.assertEquals("Haavard", createFeedback.getJSONArray("patients").getJSONObject(0).getString("name"));
    }

    @Test
    public void doGetListPatientsAdmin() throws IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject userLogin = Authentication.login("admin@email.com", "password");
        JSONObject createFeedback = mockRequest("action=listPatients&" + "&cookie=" + userLogin.getString("cookie"));
        Assert.assertEquals("Admin", createFeedback.getJSONArray("patients").getJSONObject(0).getString("name"));
    }

    @Test
    public void doGetListFeedBacks() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        JSONObject userResponse = Authentication.login("petter@email.com", "password");
        Create.createFeedback("Hei kan du slette", userResponse.getString("cookie"));
        JSONObject fetchFeedbacks = mockRequest("action=listFeedbacks&" + "&cookie=" + "1&isRead=false");
        Assert.assertEquals("Hei kan du slette", fetchFeedbacks.getJSONArray("feedbacks").getJSONObject(0).getString("message"));
    }

    @Test
    public void doGetUpdatePatientsDoctor() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        JSONObject userResponse = Authentication.login("haavard@email.com", "password");
        JSONObject updateDoctorID = mockRequest("action=updatePatientsDoctor&" + "&cookie=" + userResponse.getString("cookie") + "&patientID=" + userResponse.getJSONObject("user").getInt("userID") + "&doctorID=2");
        Assert.assertEquals("OK", updateDoctorID.getString("status"));
    }

    @Test
    public void createAdmin() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
        JSONObject userResponse = Authentication.login("admin@email.com", "password");
        JSONObject createAdmin = mockRequest("action=createAdmin&" + "&cookie=" + userResponse.getString("cookie") + "&doctorID=" + 2 + "&name=test&email=test@email.com&password=password&role=patient");
        Assert.assertEquals("OK", createAdmin.getString("status"));
    }

}
