package tdt4140.gr1844.app.ui;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class HelloWorldTest {

    private tdt4140.gr1844.app.ui.HelloWorld app;

    @Before
    public void setUp() {
        app = new HelloWorld();
    }


    @Test
    public void testHello() {
        Assert.assertFalse(app.HelloWorld());
    }
}