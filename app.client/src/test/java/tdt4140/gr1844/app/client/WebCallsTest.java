package tdt4140.gr1844.app.client;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1844.app.core.QueryString;
import java.net.URL;

public class WebCallsTest {
    private WebCalls http;
    private String API_URL = "http://api.moholt.me?";
    @Before
    public void setUp() { http = new WebCalls();
    }

    @Test
    public void testGet() {
        try {
            JSONObject json = WebCalls.sendGET(QueryString.parse(new URL(API_URL + "action=createPatient&name=Petter&email=email&password=password&doctorID=2")));
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
