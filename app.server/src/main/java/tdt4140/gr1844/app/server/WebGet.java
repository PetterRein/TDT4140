package tdt4140.gr1844.app.server;

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

    private void manageAction( Map<String, List<String>> params) {
        String action = params.get("action").toArray()[0].toString();
        switch (action) {
            case "login":
                handleLogin(params.get("username"), params.get("password"));
                break;
            case "logout":
                handleLogout();
                break;
            case "createUser":
                handleCreateuser();
                break;
            case "deleteUser":
                handleDeleteUser();
                break;
            case "addDataToUser":
                handleAddDataToUser();
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String queryString = request.getQueryString();

        Map<String, List<String>> params = QueryString.parse(new URL("http://localhost:8080/api?" + queryString));
        manageAction(params);
        // Set the response message's MIME type
        response.setContentType("text/html;charset=UTF-8");
        // Allocate a output writer to write the response message into the network socket
        // Write the response message, in an HTML page
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            out.println("<title>Response from the server</title></head>");
            out.println("<body>");
            // Echo client's request information
            out.println("<p>Request URI: " + request.getRequestURI() + "</p>");
            out.println("<p>Protocol: " + request.getProtocol() + "</p>");
            out.println("<p>PathInfo: " + request.getPathInfo() + "</p>");
            out.println("<p>Remote Address: " + request.getRemoteAddr() + "</p>");
            out.println(queryString);
            out.println("</body>");
            out.println("</html>");
        }
    }
}
