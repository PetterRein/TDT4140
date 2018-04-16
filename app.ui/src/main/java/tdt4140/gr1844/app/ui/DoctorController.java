package tdt4140.gr1844.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;
import tdt4140.gr1844.app.client.WebCalls;

import javax.swing.*;

public class DoctorController {
    public Label doctorLabel;
    public Button deletePatientButton;
    public TextField deletePatientID;

    // Active user panel
    @FXML
    private Label activePatientNameLabel;
    @FXML
    private Label lastRatingLabel;
    @FXML
    private Label ratingAvgLabel;
    @FXML
    private  Label activePatientIDLabel;
    @FXML
    private TextField patientName;
    @FXML
    private TextField patientEmail;
    @FXML
    private TextField patientPassword;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox patientListBox;

    @FXML
    private TextArea feedbackTextField;

    private Main main = new Main();

    private Shared shared = new Shared();
    private int activePatientID;
    @FXML
    private Label notification;


    @FXML
    public void initialize() throws Exception {
        doctorLabel.setText("Welcome Dr. " + main.getName());
        updatePatientList(getPatients());
    }


    void updatePatientList(JSONArray patients) throws Exception {
        listPatients(patients);
    }



    private void listPatients(JSONArray patients) throws Exception {
        patientListBox.getChildren().clear();
        for(Object patient : patients){
            Button btnNumber = createPatient((JSONObject) patient);
            patientListBox.getChildren().add(btnNumber);
        }
    }

    private Button createPatient(JSONObject patient) throws Exception {
        System.out.println(patient);
        Button button = new Button(patient.getString("name"));
        JSONArray feelings = WebCalls.sendGET(
                "action=listFeelings" +
                        "&patientID=" + patient.getInt("id") +
                        "&orderBy=desc" +
                        "&cookie=" + main.getCookie()
        ).getJSONArray("feelings");

        int lastRating = 0;
        int ratingAvg = 0;
        if (feelings.length() != 0) {
            ratingAvg = getAverageRating(feelings);
            lastRating = feelings.getJSONObject(0).getInt("rating");
            if (ratingAvg < 2) {
                button.setId("unhealthyRating");
            } else if(ratingAvg < 3.5) {
                button.setId("averageHealthRating");
            } else {
                button.setId("healthyRating");
            }

        }
        int finalRatingAvg = ratingAvg;
        int finalLastRating = lastRating;
        button.setOnMouseClicked(event -> {
            updateActivePatient(
                    patient.getString("name"),
                    patient.getInt("id"),
                    finalLastRating,
                    finalRatingAvg
            );
        });
        return button;
    }

    private void updateActivePatient(String patientName, int patientID, int lastRating, int ratingAvg){
        activePatientNameLabel.setText("Patient's name: " + patientName);
        activePatientIDLabel.setText("Patients's ID: " + patientID);
        String finalLastRating = lastRating == 0 ? "no ratings yet" : Integer.toString(lastRating);
        String finalRatingAvg = ratingAvg== 0 ? "no ratings yet" : Integer.toString(ratingAvg);
        lastRatingLabel.setText("Last rating: " + finalLastRating);
        ratingAvgLabel.setText("Rating average: " + finalRatingAvg);
        this.activePatientID = patientID;
    }

    private int getAverageRating(JSONArray feelings) {
        int sum = 0;
        for (Object feelingObject : feelings) {
            JSONObject feeling = (JSONObject) feelingObject;
            sum += feeling.getInt("rating");
        }
        return sum/feelings.length();
    }

    private JSONArray getPatients() throws Exception {
        return WebCalls.sendGET(
                "action=listPatients&" +
                        "doctorID=" + main.getUserID() +
                        "&cookie=" + main.getCookie()
        ).getJSONArray("patients");
    }

    @FXML
    private void logout() throws Exception {
        main.logout();
        main.changeView(rootPane, "Main");
    }

    @FXML
    private void registerPatient() throws Exception {
        shared.registerPatient();
        updatePatientList(getPatients());
    }

    @FXML
    private void updateDoctor() throws Exception {
        JSONObject response = main.updateDoctor(this.activePatientID);
        if (response.getString("status").equals("OK")) {
            updatePatientList(getPatients());
            notification.setText("Patient removed");
            this.activePatientID = -1;
        } else {
            notification.setText(response.getString("message"));
        }
    }

    @FXML
    private void sendFeedback() throws Exception {
        String message = feedbackTextField.getText();
        if (!message.equals("")) {
            JSONObject response = main.sendFeedback(feedbackTextField.getText());
            if (response.getString("status").equals("OK")) {
                notification.setText("Feedback sent");
                feedbackTextField.clear();
            } else {
                notification.setText(response.getString("message"));

            }
        } else {
            notification.setText("Please write a feedback!");
        }
    }

}
