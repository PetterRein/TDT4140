package tdt4140.gr1844.app.web;


import tdt4140.gr1844.app.core.Authentication;
import tdt4140.gr1844.app.core.CookieValueGenerator;
import tdt4140.gr1844.app.core.Database;
import tdt4140.gr1844.app.core.SqlConnect;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

public class WebGet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SqlConnect conn = new SqlConnect();
        Boolean onlineOrOffline = false;
        String sid = null;
        Cookie[] r = request.getCookies();
        //Check if there is a session cookie provided
    	for (Cookie c: r){
            System.out.println("CookieValue " + c.getName());
            sid = c.getName();
        }

        ArrayList<String> parameterNames = new ArrayList<String>();
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameterName = (String) enumeration.nextElement();
            parameterNames.add(parameterName);
        }
        for (String r3 : parameterNames){
            System.out.println("Paramet: " + r3);
        }

        if (Arrays.toString(request.getParameterValues("loginUser")) != "null"){
            //Run sjekk om user og passord er i databasem
            String username = Arrays.toString(request.getParameterValues("loginUser")).replaceAll("[\\[\\]]", "");
            String password = Arrays.toString(request.getParameterValues("password")).replaceAll("[\\[\\]]", "");
            Boolean loginCheck = null;
            try {
                loginCheck = Authentication.login(onlineOrOffline,username, password);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("Sjekker va Authentication retunerer: " + loginCheck);
            if (loginCheck) {
            	String cookieValue = CookieValueGenerator.generateCookieValue(32);
            	Cookie myCookie = new Cookie("SID", cookieValue);
            	//TODO insert new cookie value into database, send cookie to user
                try {
                    PreparedStatement statement = conn.connect(onlineOrOffline).prepareStatement("update users set cookie = ? where email = ?");
                    statement.setString(1, cookieValue);
                    statement.setString(2, username);
                    statement.execute();
                    conn.disconnect();
                    response.addCookie(myCookie);
                    response.setHeader("cookie", cookieValue);
                    response.setStatus(200);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NamingException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println("Login failed");
            	response.setStatus(401);
            }
        }
        else if(Arrays.toString(request.getParameterValues("logout")) != "null"){
            System.out.println("Logout: ");
            if (sid != null) {
                try {
                    if (Authentication.logout(onlineOrOffline, sid)) {
                        //Send logout success response
                        response.setStatus(200);
                    }
                    else {
                        //Send logout failure response
                        response.setStatus(422);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else {
            	//Send logout failure response
            	response.setStatus(422);
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
        else if(Arrays.toString(request.getParameterValues("addUser")) != "null"){
            System.out.println("Para AddUserRole: " + Arrays.toString(request.getParameterValues("role")));
            try {
                if (Database.getRoleFromCookie(onlineOrOffline,sid).equals("Admin") && sid != null) {
                    String role = Arrays.toString(request.getParameterValues("role"));
                    role = role.replaceAll("[\\[\\]]", "");
                    if (role.equals("Doctor") || role.equals("Patient") || role.equals("Doktor") || role.equals("Pasient")) {
                        System.out.println("We good? Code God?");
                        String userName = Arrays.toString(request.getParameterValues("userName"));
                        userName = userName.replaceAll("[\\[\\]]", "");
                        String userEmail = Arrays.toString(request.getParameterValues("userEmail"));
                        userEmail = userEmail.replaceAll("[\\[\\]]", "");
                        String userPassword = Arrays.toString(request.getParameterValues("userPassword"));
                        userPassword = userPassword.replaceAll("[\\[\\]]", "");
                        try {
                            Database.createUser(onlineOrOffline,role , userName, userEmail, userPassword);
                        } catch (NamingException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        //TODO response to illegal role
                        response.setStatus(422);
                    }
                }
                else {
                    //TODO response to attempt from non-admin
                    response.setStatus(401);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (Arrays.toString(request.getParameterValues("delUser")) != "null"){
            try {
                if (Database.getRoleFromCookie(onlineOrOffline, sid).equals("Admin") && sid != null){
                    String user = Arrays.toString(request.getParameterValues("user"));
                    try {
                        Database.deleteUser(user, onlineOrOffline);
                    } catch (NamingException e) {
                    e.printStackTrace();
                    }
                    response.setStatus(200);
                }
                else{
                    response.setStatus(401);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
        else{
            System.out.println("Unown Parameter");
            response.setStatus(9999);
        }
    }

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
