package tdt4140.gr1844.app.client;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1844.app.core.QueryString;
import java.net.URL;

import static tdt4140.gr1844.app.core.createUrlFromString.createUrlFromStringMethode;

public class WebCallsTest {
    private WebCalls http;

    @Before
    public void setUp() { http = new WebCalls();
    }

    @Test
    public void testGet() throws Exception {
        String stringURL = "action=createPatient&name=name&password=pw&email=email&doctorID=1";
        URL url = createUrlFromStringMethode(stringURL);
        JSONObject json = http.sendGET(QueryString.parse(url));
        if (json.getString("status").equals("OK")){
            Assert.assertTrue(true);
        }
        else if (json.getString("status").equals("ERROR")){
            String message = json.getString("message");
            Assert.assertEquals("E-mail address (email) is taken.", message);
        }
        else{
            Assert.assertTrue(false);
        }
    }
}
