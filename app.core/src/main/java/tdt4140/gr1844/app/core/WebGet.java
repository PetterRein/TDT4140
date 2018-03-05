package tdt4140.gr1844.app.core;

import sun.net.www.http.HttpClient;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import javax.ws.rs.GET;

public class WebGet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (Arrays.toString(request.getParameterValues("user")) != "null"){
        	SqlConnect conn = new SqlConnect();
            System.out.println("Para: " + Arrays.toString(request.getParameterValues("user")));
            System.out.println("Para: " + Arrays.toString(request.getParameterValues("password")));
            //Run sjekk om user og passord er i databasem
            String username = Arrays.toString(request.getParameterValues("user"));
            String password = Arrays.toString(request.getParameterValues("pasword"));
            if (Authentication.login(username, password)) {
            	String cookieValue = CookieValueGenerator.generateCookieValue(32);
            	Cookie myCookie = new Cookie("name", cookieValue);
            	//TODO insert new cookie value into database, send cookie to user
            	try {
            		PreparedStatement statement = conn.connect().prepareStatement("update users set cookie = ? where email = ?");
            		statement.setString(1, cookieValue);
            		statement.setString(2, username);
            		response.addCookie(myCookie);
            		response.setStatus(200);
            	}
            	catch(SQLException e) {
            		response.setStatus(401);
            	}
            }
            else {
            	response.setStatus(401);
            }
        }
        else if(Arrays.toString(request.getParameterValues("userID")) != "null"){
            System.out.println("Para UserID: " + Arrays.toString(request.getParameterValues("userID")));
        }
        else if (Arrays.toString(request.getParameterValues("patientID")) != "null"){
            System.out.println("Para PatientID: " + Arrays.toString(request.getParameterValues("patientID")));
        }
        else if (Arrays.toString(request.getParameterValues("doctorID")) != "null"){
            System.out.println("Para DoctorID: " + Arrays.toString(request.getParameterValues("doctorID")));
        }
        else if(Arrays.toString(request.getParameterValues("delPatient")) != "null"){
            System.out.println("Para DelPatient: " + Arrays.toString(request.getParameterValues("delPatient")));
        }
        else if(Arrays.toString(request.getParameterValues("addPatient")) != "null"){
            System.out.println("Para AddPatient: " + Arrays.toString(request.getParameterValues("addPatient")));
        }
        else if(Arrays.toString(request.getParameterValues("delDoctor")) != "null"){
            System.out.println("Para DelDoctor: " + Arrays.toString(request.getParameterValues("delDoctor")));
        }
        else if(Arrays.toString(request.getParameterValues("addPatient")) != "null"){
            System.out.println("Para AddPatient: " + Arrays.toString(request.getParameterValues("addPatient")));
        }
        else if(Arrays.toString(request.getParameterValues("delDataPatient")) != "null"){
            System.out.println("Para DelDataPatient: " + Arrays.toString(request.getParameterValues("delDataPatient")));
        }
        else if(Arrays.toString(request.getParameterValues("addDataPatient")) != "null"){
            System.out.println("Para AddDataPatient: " + Arrays.toString(request.getParameterValues("addDataPatient")));
        }
        else if(Arrays.toString(request.getParameterValues("getPatientData")) != "null"){
            System.out.println("Para PatientData: " + Arrays.toString(request.getParameterValues("getPatientData")));
        }
        Cookie[] r = request.getCookies();
        System.out.println(r);
        /** System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

         BufferedReader rd = new BufferedReader(
         new InputStreamReader(response.getEntity().getContent()));

         StringBuffer result = new StringBuffer();
         String line = "";
         while ((line = rd.readLine()) != null) {
         result.append(line);
         }

         System.out.println(result.toString());**/

    }

    @GET
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        String path = request.getPathInfo();
        if (path != null) {
            if (path.startsWith("/")) {
                path = path.substring(1);
                String[] segments = path.split("\\/");
                if(segments.length == 2){
                    String userlogUs = segments[0].toLowerCase();
                    String userlogPa = segments[1].toLowerCase();
                    if (userlogUs.startsWith("user") && userlogPa.startsWith("password")){
                        String user = segments[0].substring(5);
                        String passsword = segments[1].substring(9);
                        System.out.println(user + " " + passsword);
                    }
                }
            }
            /** String[] segments = path.split("\\/");
             if (segments.length == 1) {
             System.out.println(segments[0]);
             }**/
        }

        PrintWriter out = response.getWriter();
        try {
            out.println("<!DOCTYPE html>");  // HTML 5
            out.println("<html><head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            out.println("<title>"  + "</title></head>");
            out.println("<body>");
            String h1 = "Hello World";
            out.println("<h1>" + request + "  TESTWST  " + response + "</h1>");  // Prints "Hello, world!"
            out.println("<a href='" + request.getRequestURI() + "'><img src='images/return.gif'></a>");
            out.println("</body></html>");
        } finally {
            out.close();
        }
    }
}
