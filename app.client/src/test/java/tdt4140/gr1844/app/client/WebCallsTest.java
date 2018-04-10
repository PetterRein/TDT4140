package tdt4140.gr1844.app.client;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import tdt4140.gr1844.app.core.QueryString;

import java.net.URL;

import static tdt4140.gr1844.app.core.createUrlFromString.createUrlFromStringMethode;

public class WebCallsTest {


    @Test
    public void testGet() throws Exception {
        String stringURL = "action=createPatient&name=name&password=pw&email=email&doctorID=1";
        URL url = createUrlFromStringMethode(stringURL);
        JSONObject json = WebCalls.sendGET(QueryString.parse(url));
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
    }
}
