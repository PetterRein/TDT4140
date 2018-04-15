package tdt4140.gr1844.app.core;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class QueryStringTest {

    @Test
    public void parse() throws UnsupportedEncodingException, MalformedURLException {
        Assert.assertEquals("login", QueryString.parse("?action=login").get("action"));
    }
}
