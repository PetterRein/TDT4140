package tdt4140.gr1844.app.client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.apache.http.protocol.HTTP.USER_AGENT;

class WebCalls {

    static JSONObject sendGET(String params) throws Exception {

        URL url = new URL("http://api.moholt.me" + params);
        //URL url = new URL("http://localhost:8080/api" + QueryString.stringify(params));

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        return new JSONObject(response.toString());
    }
}
