package tdt4140.gr1844.app.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.WebCalls;

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
            for (int i = 0; i < 10; i++){
                String[] response = http.loginUser("Per", "33", "admin@o.com");
            }
            String[] response = http.loginUser("Per", "33", "admin@o.com");
            Boolean falseOrTrue = http.stringToBoolean(response[2]);
            Assert.assertTrue(falseOrTrue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**@Test
    public void testAddUser() throws Exception {
        try {
            String[] response = http.addUser("Pål2", "Pal123", "pal@online.no", "Pasient", "41");
            Boolean falseOrTrue = http.stringToBoolean(response[2]);
            Assert.assertTrue(falseOrTrue);
        }catch (Exception e){
            e.printStackTrace();
        }
    }**/

    /**@Test
    public void testLogoutUser() throws Exception{
        try {
            String[] response = http.logoutUser("Doctor", "mFEMvsQIjQ7tiAy2qVJ4Wdq87zBWdy4V");
            Boolean falseOrTrue = http.stringToBoolean(response[2]);
            Assert.assertTrue(falseOrTrue);
        } catch (Exception e){
            e.printStackTrace();
        }
    }**/

    /**@Test
    public void testLoginLogoutUser() throws Exception{
        try {
            String[] response = http.loginUser("Per", "33", "admin@o.com");
            String[] response1 = http.logoutUser("Doctor", response[1]);
            Boolean falseOrTrue = http.stringToBoolean(response1[2]);
            Assert.assertTrue(falseOrTrue);
        }catch (Exception e){
            e.printStackTrace();
        }
    }**/

    /**@Test
    public void testLoginAdminAddUserLogoutLoginUserLogoutUser() throws Exception{
        try {
            String[] response = http.loginUser("Per", "33", "admin@o.com");
            String[] response1 = http.addUser("Pål2", "Pal123", "pal@online.no", "Pasient", response[1]);
            String[] response2 = http.logoutUser("Doctor", response[1]);
            String[] response3 = http.loginUser("Pål2", "Pal123", "pal@online.no");
            String[] response4 = http.logoutUser("Doctor", response3[1]);
            Boolean falseOrTrue = http.stringToBoolean(response4[2]);
            Assert.assertTrue(falseOrTrue);
        }catch (Exception e){
            e.printStackTrace();
        }
    }**/
}
