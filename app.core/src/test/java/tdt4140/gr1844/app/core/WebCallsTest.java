package tdt4140.gr1844.app.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WebCallsTest {
    private WebCalls http;

    @Before
    public void setUp() {
        http = new WebCalls();
    }

    @Test
    public void testGet() {
        try {
            Assert.assertEquals(200 , http.sendGet());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoginUser(){
        try {
            Assert.assertTrue(http.loginUser("correctEmail", "correctPassword"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
