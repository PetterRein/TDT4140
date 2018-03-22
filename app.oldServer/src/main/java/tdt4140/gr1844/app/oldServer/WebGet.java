package tdt4140.gr1844.app.oldServer;


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
import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

public class WebGet extends HttpServlet {
    /**
     *
     * @param request Forespørsel som kommer fra en client, inneholder en standart nettverks forespørsel
     * @param response Svar som vi sender tilbake til client pga requesten vi fikk inn
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Boolean onlineOrOffline = false;
        String sid = null;
        Cookie[] cookies = request.getCookies();
        //Check if there is a session cookie provided
        if(cookies != null){
            for (Cookie cookie: cookies){
                if (cookie.getName().equals("SID")){
                    System.out.println("CookieValue " + cookie.getName());
                    sid = cookie.getValue();
                }
                else {
                    System.out.println("CookieValue " + cookie.getName());
                    sid = cookie.getName();
                    break;
                }
            }
        }

        ArrayList<String> parameterNames = new ArrayList<String>();
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameterName = (String) enumeration.nextElement();
            parameterNames.add(parameterName);
        }
        for (String parametere : parameterNames){
            System.out.println("Paramet: " + parametere);
        }

        if (Arrays.toString(request.getParameterValues("loginUser")) != "null"){
            //Run sjekk om user og passord er i databasem
            String username = Arrays.toString(request.getParameterValues("loginUser"));
            username = username.substring(1, username.length()-1);
            String password = Arrays.toString(request.getParameterValues("password"));
            password = password.substring(1, password.length()-1);
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
                    SqlConnect conn = new SqlConnect();
                    PreparedStatement statement = conn.connect(onlineOrOffline).prepareStatement("update users set cookie = ? where email = ?");
                    statement.setString(1, cookieValue);
                    statement.setString(2, username);
                    statement.execute();
                    conn.disconnect();
                    response.addCookie(myCookie);
                    response.setHeader("cookie", cookieValue);
                    String role = Database.getRoleFromCookie(onlineOrOffline,cookieValue);
                    response.setHeader("role",role);
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
            System.out.println("SID: " + sid);
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
        else if(Arrays.toString(request.getParameterValues("addUser")) != "null"){
            System.out.println("Para AddUserRole: " + Arrays.toString(request.getParameterValues("role")));
            try {
                String eee = Database.getRoleFromCookie(onlineOrOffline,sid);
                System.out.println(eee);
                boolean userRole = Database.getRoleFromCookie(onlineOrOffline,sid).equals("Admin");
                if (eee != null && sid != null && (userRole || Database.getRoleFromCookie(onlineOrOffline,sid).equals("Doctor"))) {
                    String role = Arrays.toString(request.getParameterValues("role"));
                    role = role.substring(1, role.length()-1);
                    if (role.equals("Doctor") || role.equals("Patient") || role.equals("Doktor") || role.equals("Pasient")) {
                        System.out.println("We good? Code God?");
                        String userName = Arrays.toString(request.getParameterValues("userName"));
                        userName = userName.substring(1, userName.length()-1);
                        String userEmail = Arrays.toString(request.getParameterValues("userEmail"));
                        userEmail = userEmail.substring(1, userEmail.length()-1);
                        String userPassword = Arrays.toString(request.getParameterValues("userPassword"));
                        userPassword = userPassword.substring(1, userPassword.length()-1);
                        try {
                            if (!userRole){
                                String doctorEmail = null;
                                try {
                                    doctorEmail = Database.getEmailFromCookie(onlineOrOffline,sid);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("docotr email " + doctorEmail);
                                Database.createUser(onlineOrOffline,role , userName, userEmail, userPassword, doctorEmail);
                                response.setStatus(200);
                            }
                            else {
                                System.out.println("Here");
                                Database.createUser(onlineOrOffline,role , userName, userEmail, userPassword, null);
                                response.setStatus(200);
                            }

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
            System.out.println("Vi sletter bruker=?");
            try {
                if (Database.getRoleFromCookie(onlineOrOffline, sid).equals("Admin") || Database.getRoleFromCookie(onlineOrOffline, sid).equals("Doctor")&& sid != null){
                    System.out.println("Admin eller Lege sletter Pasient");
                    String user = Arrays.toString(request.getParameterValues("delUser"));
                    user = user.substring(1, user.length()-1);
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
        else if(Arrays.toString(request.getParameterValues("delDataPatient")) != "null"){
            String username = Arrays.toString(request.getParameterValues("User"));
            username = username.substring(1, username.length()-1);

            if (Arrays.toString(request.getParameterValues("IntArray")) != "null"){
                String[] intStringArray = request.getParameterValues("intArray");
               /**
                * convert to intStringArray to int[]
                */
           }
           else if(Arrays.toString(request.getParameterValues("delAllData")) != "null"){
               if (Database.delAllDataFromUser(onlineOrOffline, username)){
                   response.setStatus(200);
               }
               else {
                   response.setStatus(401);
               }
           }
           else if(Arrays.toString(request.getParameterValues("delData")) != null){
               String dataString = Arrays.toString(request.getParameterValues("dataKey"));
               dataString = dataString.substring(1, dataString.length()-1);
               int dataInt = Integer.parseInt(dataString);
                try {
                    if (Database.delDataFromUser(onlineOrOffline, dataInt, username)){
                        response.setStatus(200);
                    }
                    else {
                        response.setStatus(401);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
            response.setStatus(401);
        }
        else if(Arrays.toString(request.getParameterValues("addDataPatient")) != "null"){
            String username = Arrays.toString(request.getParameterValues("User"));
            username = username.substring(1, username.length()-1);
            String data = Arrays.toString(request.getParameterValues("data"));
            data = data.substring(1, data.length()-1);
            System.out.println("Para AddDataPatient: " + Arrays.toString(request.getParameterValues("addDataPatient")));
            try {
                if(Database.addDataToUser(onlineOrOffline,data,username)){
                    response.setStatus(200);
                }
                else {
                    response.setStatus(401);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        else if(Arrays.toString(request.getParameterValues("getPatientData")) != "null"){
            String email = Arrays.toString(request.getParameterValues("getPatientData"));
            email = email.substring(1, email.length()-1);
            System.out.println("Para PatientData: " + Arrays.toString(request.getParameterValues("getPatientData")));
            String payLoad = null;
            try {
                ArrayList<String> data = Database.getNLastPatientData(onlineOrOffline, email, 1);
                payLoad = data.get(1);
            } catch (NamingException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            /**
             * Lage payload av Data
             */
            System.out.println(payLoad);
            response.addHeader("data", payLoad);
            response.setStatus(200);
        }
        else if(Arrays.toString(request.getParameterValues("feedback")) != "null"){
            String feedback = Arrays.toString(request.getParameterValues("feedback"));
            feedback = feedback.substring(1, feedback.length()-1);
            try {
                Database.createFeedback(false, feedback);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
            response.setStatus(200);
        }
        else if(Arrays.toString(request.getParameterValues("getDoctorsPatients")) != "null"){
            try {
                if (Database.getRoleFromCookie(onlineOrOffline, sid).equals("Doctor")) {
                    String userEmail = Arrays.toString(request.getParameterValues("getDoctorsPatients"));
                    userEmail = userEmail.substring(1, userEmail.length() - 1);
                    System.out.println("Para getDocotrsPatients: " + userEmail);
                    try {
                        String data = Database.getDoctorsPatients(onlineOrOffline, userEmail);
                        System.out.println(data);
                        response.addHeader("doctorPatients", data);
                        response.setStatus(200);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (NamingException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    response.addHeader("doctorPatients", "non");
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
        else{
            System.out.println("Unknown Parameter");
            response.setStatus(405);
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
