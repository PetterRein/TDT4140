package tdt4140.gr1844.app.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

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

    /**@Test
    public void testLoginUser(){
        try {
            Assert.assertTrue(http.loginUser("Tom", "password", "tom@doctor.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }**/

    /**@Test
    public void testAddUser() throws Exception {
        try {
            Assert.assertTrue(http.addUser("Pål2", "Pal123", "pal@online.no", "Pasient", "41"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }**/

    /**@Test
    public void testLogoutUser() throws Exception{
        try {
            Assert.assertTrue(http.logoutUser("Doctor", "mFEMvsQIjQ7tiAy2qVJ4Wdq87zBWdy4V"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }**/
}
