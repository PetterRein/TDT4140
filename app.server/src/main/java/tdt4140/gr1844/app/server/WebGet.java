package tdt4140.gr1844.app.server;

import org.json.JSONObject;
import tdt4140.gr1844.app.core.QueryString;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class WebGet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private String getParam(List<String> param){
        return param.toArray()[0].toString();
    }

    private JSONObject getResponse(Map<String, List<String>> params) throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException, NamingException {
        String action = params.get("action").toArray()[0].toString();
        JSONObject response = null;
        switch (action) {
            case "login":
                response = Database.handleLogin(
                        getParam(params.get("email")),
                        getParam(params.get("password"))
                );
                break;
            case "logout":
                //handleLogout();
                break;
            case "getPatientData":
                response = Database.handleGetPatientData(
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
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException | NamingException e) {
            e.printStackTrace();
        }
    }
}
