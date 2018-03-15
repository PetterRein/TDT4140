package tdt4140.gr1844.app.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1844.app.client.HelloWorld;


public class HelloWorldTest {

    private tdt4140.gr1844.app.client.HelloWorld app;

    @Before
    public void setUp() {
        app = new HelloWorld();
    }


    @Test
    public void testHello() {
        Assert.assertFalse(app.HelloWorld());
    }
}