package tdt4140.gr1844.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONObject;
import tdt4140.gr1844.app.client.WebCalls;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Objects;

public class Main extends Application {
    /**public static List<Festival> festivals;
    public static List<Offer> offers;**/
    private static String name;
    private static String role;
    private static String cookie;
    private static int userID;
    private static int doctorID;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("Main.fxml")));
        primaryStage.setTitle("How do you feel?");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    void changeView(AnchorPane rootPane, String fxmlFile) throws IOException {
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(fxmlFile + ".fxml")));
        rootPane.getChildren().setAll(pane);
    }


    JSONObject createPatient(String name, String email, String password, int doctorID) throws Exception {
        return  WebCalls.sendGET(
                "action=createPatient" +
                        "&name=" + name +
                        "&email=" + email +
                        "&password=" + password +
                        "&doctorID=" + doctorID
        );
    }


    JSONObject deleteUser(int userID) throws Exception {
        return  WebCalls.sendGET(
                "action=deleteUser&userID=" + userID +
                        "&cookie=" + getCookie()
        );
    }

    JSONObject markAsRead(int feedbackID) throws Exception {
        JSONObject response =
        WebCalls.sendGET(
                "action=markFeedbackRead" +
                        "&feedbackID=" + feedbackID +
                        "&cookie=" + getCookie()
        );
        System.out.println(response);
        return response;
    }

    JSONObject sendFeedback(String feedback) throws Exception {
        return WebCalls.sendGET(
                "action=createFeedback&" +
                        "message=" + URLEncoder.encode(feedback, "UTF-8") +
                        "&cookie=" + cookie);
    }



    JSONObject updateDoctor(int patientID) throws Exception {
        return WebCalls.sendGET(
                "action=updateDoctor" +
                        "&patientID=" + patientID +
                        "&cookie=" + cookie
        );
    }

    void setUser(JSONObject userResponse) {
        JSONObject user = userResponse.getJSONObject("user");
        System.out.println(user);
        name = user.getString("name");
        userID = user.getInt("userID");
        doctorID = user.getInt("doctorID");
        role = user.getString("role");
        setCookie(userResponse.getString("cookie"));
    }

    void logout() throws Exception {
        WebCalls.sendGET("action=logout&cookie=" + cookie);
    }



    int getUserID() {
        return userID;
    }

    void setUserID(String userID) {
        userID = userID;
    }


    String getCookie() {
        return cookie;
    }

    private void setCookie(String sessionCookie) {
        cookie = sessionCookie;
    }


    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }


}
