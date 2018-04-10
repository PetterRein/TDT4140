package tdt4140.gr1844.app.core;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class createUrlFromString {
    public static URL createUrlFromStringMethode(String urlString) throws UnsupportedEncodingException, MalformedURLException {
        String beginningOfUrl = "http://localhost:8080/api?";
        String[] splitedOnAnd = urlString.split("&");
        for (String string : splitedOnAnd){
            String[] splitedOnEqual = string.split("=");
            beginningOfUrl = beginningOfUrl + splitedOnEqual[0] + "=" + java.net.URLEncoder.encode(splitedOnEqual[1], "UTF-8") + "&";
        }
        return new URL(beginningOfUrl);
    }
}
