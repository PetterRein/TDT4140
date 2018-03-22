package tdt4140.gr1844.app.client;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WebCalls {
    private final String USER_AGENT = "Mozilla/5.0";
    private final String urlServer = "https://localhost:3000/api/";
    private String sessionCookie = "123";
    private String userEmail = "per";
    private String userRole = "pasient";

    public static void main(String[] args) throws Exception {

        WebCalls http = new WebCalls();
        System.out.println("Testing 1 - Send Http GET request");
        http.sendGet();
    }

    private void updateSessionCookie(String cookie){
        if (!cookie.equals(null)){
            sessionCookie = cookie;
            System.out.println(sessionCookie);
        }
        else {
            System.out.println("Something worng with sessionCookie");
        }
    }

    private void updateUserRole(String role){
        if (!userRole.equals(null)){
            userRole = role;
        }
        else {
            System.out.println("Something worng with userRole");
        }
    }

    private void updateUserEmailSession(String email){
        if (!email.equals(null)){
            userEmail = email;
        }
        else {
            System.out.println("Something worng with userEmail");
        }
    }

    public String decodeResponseCode(int responseCode){ 
        if (responseCode == 200){
            return "1";
        }
        else {
            return "-1";
        }
     }

     public boolean stringToBoolean(String trueFalse){
         if (trueFalse.equals("1")){
            return true;
         } 
         else if (trueFalse.equals("-1")){ 
             return false; 
         } 
         else { 
             System.out.println("Someting wrong on trueFalseParse");
             return false; 
         } 
    }

     public String[] loginUser(String userName, String userPassword, String userEmail ) throws Exception {
         String[] response = sendPost("loginUser", userName, userPassword, userEmail, null);
         for (int i = 0; i < response.length; i++){
             System.out.println(response[i]);
         }
         System.out.println(response[0]);
         System.out.println(response[1]);
         updateUserEmailSession(userEmail);
         updateSessionCookie(response[1]);
         response[2] = decodeResponseCode(Integer.parseInt(response[0]));
         return response;
     }

     public String[] addUser(String userName, String userPassword, String userEmail, String role) throws Exception {
         String[] response = sendPost("addUser", userName, userPassword, userEmail, role);
         response[2] = decodeResponseCode(Integer.parseInt(response[0]));
         return response;
     }

     public String[] logoutUser() throws Exception {
        String[] response = sendPost("logout", userEmail, null, null, null);
        response[2] = decodeResponseCode(Integer.parseInt(response[0]));
        return response;
     }

     public String[] sendUserData(String data) throws Exception{
         String[] response = sendPost("addDataPatient", data,null,userEmail,null);
         response[2] = decodeResponseCode(Integer.parseInt(response[0]));
         return response;
     }

     public String[] delUser(String userEmail) throws Exception{
         String[] response = sendPost("delUser", null,null,userEmail,null);
         response[2] = decodeResponseCode(Integer.parseInt(response[0]));
         return response;
     }

     public String[] getDoctorsPatients() throws Exception {
         String[] response = sendPost("getDoctorsPatients", null,null,userEmail,null);
         response[2] = decodeResponseCode(Integer.parseInt(response[0]));
         return response;
     }

    // HTTP GET request
    public int sendGet() throws Exception {
        String url = "http://www.google.com/search?q=developer";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        // add request header
        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
        return response.getStatusLine().getStatusCode();
    }


    public CloseableHttpResponse sendPostArray(String userEmail, String userPassword, ArrayList<ArrayList<String>> params) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            List<NameValuePair> form = new ArrayList<>();
            form.add(new BasicNameValuePair("userEmail", userEmail));
            form.add(new BasicNameValuePair("userPassword", userPassword));
            for (int i = 0; i < params.size(); i++){
                form.add(new BasicNameValuePair(params.get(i).get(0),params.get(i).get(1)));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
            HttpPost httpPost = new HttpPost(urlServer + "login");
            if (sessionCookie != null){
                httpPost.addHeader("cookie", sessionCookie);
            }
            else {
                httpPost.addHeader("cookie", "12312309084214");
            }
            httpPost.setEntity(entity);
            System.out.println("Executing request " + httpPost.getRequestLine());
            // Create a custom response handler
            CloseableHttpResponse response = httpclient.execute(httpPost);
            return response;
        }
    }


    public CloseableHttpResponse sendLoginPost(String userEmail, String userPassword) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            List<NameValuePair> form = new ArrayList<>();
            form.add(new BasicNameValuePair("userEmail", userEmail));
            form.add(new BasicNameValuePair("userPassword", userPassword));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

            HttpPost httpPost = new HttpPost(urlServer + "login");
            if (sessionCookie != null){
                httpPost.addHeader("cookie", sessionCookie);
            }
            else {
                httpPost.addHeader("cookie", "12312309084214");
            }

            httpPost.setEntity(entity);
            System.out.println("Executing request " + httpPost.getRequestLine());

            // Create a custom response handler
            CloseableHttpResponse response = httpclient.execute(httpPost);
            return response;
        }
    }

    public CloseableHttpResponse sendGet(String endpoint) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()){
            HttpGet request = new HttpGet(urlServer + endpoint);
            request.addHeader("User-Agent", USER_AGENT);
            if (sessionCookie != null){
                request.addHeader("cookie", sessionCookie);
            }
            else {
                request.addHeader("cookie", "12312309084214");
            }
            CloseableHttpResponse response = httpclient.execute(request);
            return response;
        }
    }

    public CloseableHttpResponse sendPut(String endpoint) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()){
            HttpPut request = new HttpPut(urlServer + endpoint);
            request.addHeader("User-Agent", USER_AGENT);
            if (sessionCookie != null){
                request.addHeader("cookie", sessionCookie);
            }
            else {
                request.addHeader("cookie", "12312309084214");
            }
            CloseableHttpResponse response = httpclient.execute(request);
            return response;
        }
    }

    public CloseableHttpResponse sendDel(String endpoint) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()){
            HttpDelete request = new HttpDelete(urlServer + endpoint);
            request.addHeader("User-Agent", USER_AGENT);
            if (sessionCookie != null){
                request.addHeader("cookie", sessionCookie);
            }
            else {
                request.addHeader("cookie", "12312309084214");
            }
            CloseableHttpResponse response = httpclient.execute(request);
            return response;
        }
    }

    public String[] sendPost(String whatPost, String userName, String userPassword, String userEmail, String userRole) throws Exception {
     //Setter urlen vi sender til
     String url = "http://localhost:8080/webapi";
     //Lager en cleint object og lagrer en cookie lagrings object til det
     CloseableHttpClient client;

     // Cookie nedover fungerer ikke
     CookieStore httpCookieStore = new BasicCookieStore();
     BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", sessionCookie);
     cookie.setDomain("moholt.me");
     cookie.setPath("/");
     cookie.setAttribute(ClientCookie.PATH_ATTR, "/");
     cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "moholt.me");
     httpCookieStore.addCookie(cookie);

     HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
     client = builder.build();
     HttpPost httpPost = new HttpPost(url);

     //men denne cookie'n gjør det
     if (sessionCookie != null){
         httpPost.addHeader("cookie", sessionCookie);
     }
     else {
        httpPost.addHeader("cookie", "12312309084214");
     }


     //Legger ved parameterene til Posten
     if(whatPost.equals("loginUser")) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("loginUser", userEmail));
        params.add(new BasicNameValuePair("password", userPassword));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
     }
     else if(whatPost.equals("addUser")){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("addUser", userName));
        params.add(new BasicNameValuePair("userName", userName));
        params.add(new BasicNameValuePair("userPassword", userPassword));
        params.add(new BasicNameValuePair("userEmail", userEmail));
        params.add(new BasicNameValuePair("role", userRole));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
     }
     else if (whatPost.equals("logout")){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("logout", userName));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
     }
     else if(whatPost.equals("addDataPatient")){
         List<NameValuePair> params = new ArrayList<NameValuePair>();
         params.add(new BasicNameValuePair("addDataPatient", userName));
         params.add(new BasicNameValuePair("User", userEmail));
         params.add(new BasicNameValuePair("data", userName));
         httpPost.setEntity(new UrlEncodedFormEntity(params));
     }
     else if (whatPost.equals("delUser")){
         List<NameValuePair> params = new ArrayList<NameValuePair>();
         params.add(new BasicNameValuePair("delUser", userEmail));
         httpPost.setEntity(new UrlEncodedFormEntity(params));
     }
     else if(whatPost.equals("getDoctorsPatients")){
         List<NameValuePair> params = new ArrayList<NameValuePair>();
         params.add(new BasicNameValuePair("getDoctorsPatients", userEmail));
         httpPost.setEntity(new UrlEncodedFormEntity(params));
     }
     else {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair(userName, userPassword));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
     }

     //Sender og lagrer svaret
     CloseableHttpResponse response = client.execute(httpPost);

     //Luker clienten
     client.close();

     //Skriver ut informasjonen fra svaret
     System.out.println("\nSending 'POST' request to URL : " + url);
     Header[] ws = response.getAllHeaders();
     String cookie1 = "non";
     String role1 = "non";
     String patients = "non";
     for (Header header: ws){
        if (header.getName().equals("cookie")){
            cookie1 = header.getValue();
        }
        if (header.getName().equals("role")){
            role1 = header.getValue();
        }
        if (header.getName().equals("doctorPatients")){
            patients = header.getValue();
        }
        System.out.println(header);
     }
     System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
     BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
     StringBuffer result = new StringBuffer();
     String line = "";
     while ((line = rd.readLine()) != null) {
        result.append(line);
     }
     String a = "";
     a = a + response.getStatusLine().getStatusCode();
        String[] returnVars = new String[5];
        returnVars[0] = a;
        returnVars[1] = cookie1;
        returnVars[3] = role1;
        returnVars[4] = patients;
        return returnVars;
     }
}
