package tdt4140.gr1844.app.server;

import org.json.JSONObject;
import tdt4140.gr1844.app.core.QueryString;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;

import static tdt4140.gr1844.app.server.Authentication.login;
import static tdt4140.gr1844.app.server.Authentication.logout;
import static tdt4140.gr1844.app.server.Create.*;
import static tdt4140.gr1844.app.server.Delete.deleteFeeling;
import static tdt4140.gr1844.app.server.Delete.deleteUser;
import static tdt4140.gr1844.app.server.Retrieve.listFeelings;
import static tdt4140.gr1844.app.server.Retrieve.listPatients;

public class WebGet extends HttpServlet {

    private int toInt(String param) {return Integer.parseInt(param);}

    private JSONObject getResponse(Map<String, String> params) throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException {
        String action = params.get("action");
        switch (action) {
            case "login":
                return login(
                    params.get("email"),
                    params.get("password")
                );
            case "logout":
                return logout(params.get("cookie"));

            case "createPatient":
                return createPatient(
                    params.get("name"),
                    params.get("email"),
                    params.get("password"),
                    toInt(params.get("doctorID"))
                );
            case "createAdminOrDoctor":
                return createAdminOrDoctor(
                    params.get("name"),
                    params.get("email"),
                    params.get("password"),
                    params.get("role"),
                    params.get("cookie")
                );
            case "deleteUser":
                return deleteUser(
                    toInt(params.get("userID")),
                    params.get("cookie")
                );
            case "createFeeling":
                return createFeeling(
                    toInt(params.get("patientID")),
                    toInt(params.get("rating")),
                    params.get("message"),
                    params.get("cookie")
                );
            case "deleteFeeling":
                return deleteFeeling(
                    toInt(params.get("feelingID")),
                    toInt(params.get("patientID")),
                    params.get("cookie")
                );
            case "listFeelings":
                return listFeelings(
                    toInt(params.get("patientID")),
                    params.get("orderBy"),
                    params.get("cookie")
            );
            // only doctor

            case "createFeedback":
                return createFeedback(
                    params.get("message"),
                    params.get("cookie")
                );

            // only admin
            case "markFeedbackRead":
                return markFeedbackRead(
                    toInt(params.get("feedbackID")),
                    params.get("cookie")
                );

            case "listPatients":
                return listPatients(
                    toInt(params.get("doctorID")),
                    params.get("cookie")
                );
            default:
                JSONObject response = new JSONObject();
                response.put("status", "ERROR");
                response.put("message", "There is no action '" + action +"' specified in the database.");
                return response;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        java.nio.file.Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println(s);
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.print(getResponse(
                QueryString.parse(
                    new URL("http://localhost:8080/api?" + request.getQueryString())
                ))
            );
        } catch (InstantiationException | SQLException | ClassNotFoundException | IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
    }
}
