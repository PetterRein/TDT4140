package tdt4140.gr1844.app.server;

import org.json.JSONObject;
import tdt4140.gr1844.app.core.QueryString;

import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;

public class WebGet extends HttpServlet {

    private int toInt(String param) {return Integer.parseInt(param);}

    private JSONObject getResponse(Map<String, String> params) throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
        String action = params.get("action");
        JSONObject response = new JSONObject();
        switch (action) {
            case "login":
                response = Database.handleLogin(
                    params.get("email"),
                    params.get("password")
                );
                break;
            case "logout":
                response = Database.handleLogout(params.get("cookie"));
                break;
            case "getPatientData":
                response = Database.handleGetPatientData(
                    toInt(params.get("patientID")),
                    params.get("orderBy"),
                    params.get("cookie")
                );
                break;
            case "createPatient":
                response = Database.handleCreatePatient(
                    params.get("name"),
                    params.get("email"),
                    params.get("password"),
                    toInt(params.get("doctorID"))
                );
                break;
            case "createAdminOrDoctor":
                response = Database.handleCreateAdminOrDoctor(
                    params.get("name"),
                    params.get("email"),
                    params.get("password"),
                    params.get("role"),
                    params.get("cookie")
                );
                break;
            case "deleteUser":
                response = Database.handleDeleteUser(
                    toInt(params.get("userID")),
                    params.get("cookie")
                );
                break;
            case "createFeeling":
                response = Database.handleCreateFeeling(
                    toInt(params.get("patientID")),
                    toInt(params.get("rating")),
                    params.get("message"),
                    params.get("cookie")
                );
                break;
            default:
                response.put("status", "ERROR");
                response.put("message", "There is no action '" + action +"' specified in the database.");
        }
        return response;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> params = QueryString.parse(
            new URL("http://localhost:8080/api?" + request.getQueryString()
        ));
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.print(getResponse(params));
        } catch (InstantiationException | NamingException | SQLException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
