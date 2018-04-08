package tdt4140.gr1844.app.server;

import org.json.JSONArray;
import tdt4140.gr1844.app.core.Authentication;
import tdt4140.gr1844.app.core.Database;
import tdt4140.gr1844.app.core.QueryString;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class WebGet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private String getParam(List<String> param){
        return param.toArray()[0].toString();
    }

    private JSONArray getResponse(Map<String, List<String>> params) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        String action = params.get("action").toArray()[0].toString();
        JSONArray response = null;
        //if (!action.equals("login") && Authentication.isAuthenticated(params.get("cookie"))) {
            switch (action) {
                case "login":
                    //handleLogin(params.get("username"), params.get("password"));
                    System.out.println(params);
                    break;
                case "logout":
                    //handleLogout();
                    break;
                case "getPatientData":
                    response = Database.handleGetPatientData(
                            getParam(params.get("role")),
                            //params.get("cookie"),
                            getParam(params.get("patientId")),
                            getParam(params.get("orderBy"))
                    );
                    break;
                case "createUser":
                    //handleCreateuser();
                    break;
                case "deleteUser":
                    //handleDeleteUser();
                    break;
                case "addDataToUser":
                    //handleAddDataToUser();
                    break;
            }
        //} else {
            // TODO: Send authentication error response
        //}
        return response;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, List<String>> params = QueryString.parse(
                new URL("http://localhost:8080/api?" + request.getQueryString()
                ));
        // Set the response message's MIME type
        // Allocate a output writer to write the response message into the network socket
        // Write the response message, in an HTML page
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.print(getResponse(params));
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
