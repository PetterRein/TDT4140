package tdt4140.gr1844.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;
import tdt4140.gr1844.app.client.WebCalls;

import javax.swing.*;

public class AdminController {
    public Label doctorLabel;
    public Button deletePatientButton;

    @FXML
    private Label notification;
    // Active user panel
    @FXML
    private Label activePatientNameLabel;

    @FXML
    private  Label activePatientIDLabel;


    @FXML
    private VBox patientsContainer;


    private Main main = new Main();

    private Shared shared = new Shared();

    @FXML
    private AnchorPane rootPane;
    private int activeUserID;

    @FXML
    private VBox feedbacksContainer;
    private int activeFeedbackID;
    @FXML
    private Label activeFeedbackIDLabel;
    @FXML
    private Label activeFeedbackMessage;

    @FXML
    public void initialize() throws Exception {
        doctorLabel.setText("Welcome Admin " + main.getName());
        updateList(patientsContainer, "patients", getPatients());
        updateList(feedbacksContainer, "feedbacks", getFeedbacks("false"));
    }


    private void updateList(VBox container, String type, JSONArray list) {
        container.getChildren().clear();
        for(Object listElement : list){
            Button button = createElement(type, (JSONObject) listElement);
            container.getChildren().add(button);
        }
    }

    private Button createElement(String type, JSONObject element) {
        Button button = new Button();
        int id = element.getInt("id");
        switch (type) {
            case "patients":
                String name = element.getString("name");
                button.setOnMouseClicked(event -> {
                    updateActivePatient(name, id);
                });
                button.setText(name);
                break;
            case "feedbacks":
                String message = element.getString("message");
                button.setOnMouseClicked(event -> {
                    updateActiveFeedback(message, id);
                });
                String title = message.length() < 16 ? message : message.substring(0, 16) + "...";
                button.setText(title);
                button.setId("feedback-button");
                break;
        }
        return button;
    }

    private void updateActivePatient(String patientName, int patientID){
        activePatientNameLabel.setText("Patient's name: " + patientName);
        activePatientIDLabel.setText("Patients's ID: " + patientID);
        this.activeUserID = patientID;

    }private void updateActiveFeedback(String message, int feedbackID){
        activeFeedbackIDLabel.setText("Feedback ID: " + feedbackID);
        activeFeedbackMessage.setText(message);
        this.activeFeedbackID = feedbackID;
    }

    private JSONArray getPatients() throws Exception {
        return WebCalls.sendGET(
                "action=listPatients" +
                        "&cookie=" + main.getCookie()
        ).getJSONArray("patients");
    }

    private JSONArray getFeedbacks(String isRead) throws Exception {
        return WebCalls.sendGET(
                "action=listFeedbacks" +
                        "&cookie=" + main.getCookie() +
                        "&isRead=" + isRead
        ).getJSONArray("feedbacks");
    }

    @FXML
    private void logout() throws Exception {
        main.logout();
        main.changeView(rootPane, "Main");
    }


    public void deletePatient() throws Exception {
        JSONObject response = main.deleteUser(this.activeUserID);
        if (response.getString("status").equals("OK")) {
            updateList(patientsContainer, "patients", getPatients());
            notification.setText("User removed.");
        } else {
            notification.setText("ERROR: " + response.getString("message"));
        }
    }

    public void markAsRead() throws Exception {
        JSONObject response = main.markAsRead(this.activeFeedbackID);
        if (response.getString("status").equals("OK")) {
            updateList(feedbacksContainer, "feedbacks", getFeedbacks("false"));
            notification.setText("Feedback read.");
            this.activeFeedbackID = -1;
        } else {
            notification.setText("ERROR: " + response.getString("message"));
        }
    }
}
