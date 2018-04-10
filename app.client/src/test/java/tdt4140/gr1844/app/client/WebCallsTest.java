package tdt4140.gr1844.app.client;

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
        String stringURL = "action=createPatient&name=name&password=pw&email=email&doctorId=1";
        URL url = createUrlFromStringMethode(stringURL);
        Assert.assertEquals("OK", http.sendGET(QueryString.parse(url)).getString("status"));
    }
}
