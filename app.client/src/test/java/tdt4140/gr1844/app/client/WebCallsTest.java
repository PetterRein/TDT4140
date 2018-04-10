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
        String url1 = "http://localhost:8080/api";
        String url2 = "http://localhost:8080/";
        String url3 = "http://localhost:8080/api/tdt4140-gr1844.app.server-0.0.1-SNAPSHOT";
        String url4 = "http://localhost:8080/tdt4140-gr1844.app.server-0.0.1-SNAPSHOT";
        String url5 = "http://localhost:8080/tdt4140-gr1844.app.server-0.0.1-SNAPSHOT/api";
        WebCalls.sendGet(url1);
        WebCalls.sendGet(url2);
        WebCalls.sendGet(url3);
        WebCalls.sendGet(url4);
        WebCalls.sendGet(url5);

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
