package tdt4140.gr1844.app.core;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WebCalls {
    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        WebCalls http = new WebCalls();

        System.out.println("Testing 1 - Send Http GET request");
        http.sendGet();
        System.out.println("\nTesting 2 - Send Http POST request");
        boolean e = http.loginUser("tom@doctor.com","password");
        System.out.println("GAD:" + e);
    }

    public boolean loginUser(String user, String password) throws Exception {
        int response = sendPost(true, user, password);
        if (response == 200){
            return true;
        }
        else{
            return false;
        }

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

    // HTTP POST request
    public int sendPost(boolean twoKeysOrNot, String user, String password) throws Exception {
        //Setter urlen vi sender til
        String url = "http://localhost:8080/webapi";

        //Lager en cleint object og lagrer en cookie lagrings object til det
        CloseableHttpClient client;
        CookieStore httpCookieStore = new BasicCookieStore();
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        client = builder.build();
        HttpPost httpPost = new HttpPost(url);

        //Legger ved parameterene til Posten
        if(twoKeysOrNot) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user", user));
            params.add(new BasicNameValuePair("password", password));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
        }
        else {
            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair(user, password));
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        }

        //Sender og lagrer svaret
        CloseableHttpResponse response = client.execute(httpPost);
        System.out.println("HER" + httpCookieStore.getCookies());

        //Luker clienten
        client.close();

        //Skriver ut informasjonen fra svaret
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + httpPost.getEntity());
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
}
