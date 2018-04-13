package tdt4140.gr1844.app.client;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class WebCallsTest {

    @Test
    public void testGet() {
        try {
            JSONObject json = WebCalls.sendGET("action=createPatient&name=Petter&email=email&password=password&doctorID=2");
            switch (json.getString("status")) {
                case "OK":
                    Assert.assertTrue(true);
                    break;
                case "ERROR":
                    String message = json.getString("message");
                    Assert.assertEquals("E-mail address (email) is taken.", message);
                    break;
                default:
                    Assert.fail();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
