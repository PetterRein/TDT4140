package tdt4140.gr1844.app.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import tdt4140.gr1844.app.client.WebCalls;

import javax.swing.*;
import java.util.Collection;

public class DoctorController {
    public Label doctorLabel;
    public Button deletePatientButton;

    // Active user panel
    @FXML
    private VBox activePatient;
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

    private int activePatientID;
    @FXML
    private Label notification;

    @FXML
    private VBox showPatientCharts;

    private JSONObject patientFeelings = new JSONObject();
    @FXML
    private VBox feelingsList;


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
        String patientID = String.valueOf(patient.getInt("id"));
        Button button = new Button(patient.getString("name"));
        JSONArray feelings = WebCalls.sendGET(
                "action=listFeelings" +
                        "&patientID=" + patientID +
                        "&orderBy=desc" +
                        "&cookie=" + main.getCookie()
        ).getJSONArray("feelings");

        System.out.println(patientFeelings);
        if (!patientFeelings.keySet().contains(patientID)) {
            patientFeelings.put(patientID, feelings);
        }

        int lastRating = 0;
        float ratingAvg = 0;
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
        float finalRatingAvg = ratingAvg;
        int finalLastRating = lastRating;
        button.setOnMouseClicked(event -> {
            updateActivePatient(
                    patient.getString("name"),
                    patient.getInt("id"),
                    patient.getString("email"),
                    finalLastRating,
                    finalRatingAvg
            );
        });
        return button;
    }

    private void updateActivePatient(String patientName, int patientID, String emailAddress, int lastRating, float ratingAvg){
        Label name = new Label();
                name.setText("Patient's name: " + patientName);
        Label email = new Label();
                email.setText("Email: " + emailAddress);
        Label idLabel = new Label();
                idLabel.setText("Patient's id: " + patientID);
        activePatient.getChildren().clear();
        String finalLastRating = lastRating == 0 ? "no ratings yet" : Integer.toString(lastRating);
        String finalRatingAvg = ratingAvg== 0 ? "no ratings yet" : Float.toString(ratingAvg);
        Label lastRatingLabel = new Label();
        Label avgRatingLabel = new Label();
        lastRatingLabel.setText("Last rating: " + finalLastRating);
        avgRatingLabel.setText("Rating average: " + finalRatingAvg);
        activePatient.getChildren().addAll(name, email, idLabel, lastRatingLabel, avgRatingLabel);
        this.activePatientID = patientID;
        showPatientCharts.getChildren().clear();
        String id = String.valueOf(patientID);
        JSONArray patientsFeelings = patientFeelings.getJSONArray(id);
        showCharts(patientsFeelings);
        updateActiveFeelings(patientsFeelings);
    }

    private void updateActiveFeelings(JSONArray patientsFeelings) {
        feelingsList.getChildren().clear();
        for (Object feelingObject: patientsFeelings) {
            JSONObject feeling = (JSONObject) feelingObject;
            int rating = feeling.getInt("rating");
            String message = feeling.getString("message");
            Label feelingLabel = new Label();
            feelingLabel.setText(rating + ": " + message);
            feelingsList.getChildren().add(feelingLabel);
        }
    }

    private float getAverageRating(JSONArray feelings) {
        float sum = 0;
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
    void registerPatient() throws Exception {
        String userEmail = patientEmail.getText();
        String userName = patientName.getText();
        String userPassword = patientPassword.getText();

        JSONObject response = main.createPatient(
                userName,
                userEmail,
                userPassword,
                main.getUserID()
        );
        if (response.getString("status").equals("OK")) {
            patientEmail.clear();
            patientName.clear();
            patientPassword.clear();
            updatePatientList(getPatients());
            notification.setText("Patient was created");
        } else {
            notification.setText(response.getString("message"));
        }
    }

    @FXML
    private void updateDoctor() throws Exception {
        JSONObject response = main.updateDoctor(this.activePatientID);
        if (response.getString("status").equals("OK")) {
            updatePatientList(getPatients());
            notification.setText("Patient removed");
            activePatient.getChildren().clear();
            showPatientCharts.getChildren().clear();
            feelingsList.getChildren().clear();
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

    @FXML
    public void showCharts(JSONArray patientFeelings) {
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        //defining a series
        XYChart.Series series = new XYChart.Series();
        //populating the series with data

        for (int i = 1; i < patientFeelings.length(); i++) {
            //series.getData().add(new XYChart.Data(i, (relation)));
            series.getData().add(
                    new XYChart.Data(i, patientFeelings
                            .getJSONObject(i)
                            .getInt("rating")
                    ));
        }

//        for (Object feelingObject : patientFeelings) {
//            JSONObject feeling = (JSONObject) feelingObject;
//            feeling.getInt("rating");
//        }

        lineChart.getData().add(series);
        showPatientCharts.getChildren().addAll(lineChart);

    }

}
