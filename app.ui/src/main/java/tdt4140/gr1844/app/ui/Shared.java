package tdt4140.gr1844.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;
import tdt4140.gr1844.app.client.WebCalls;

public class Shared {
    private Button deletePatientButton;
    private TextField deletePatientID;
    private Button buttonName;

    // Active user panel
    @FXML
    private Label activePatientNameLabel;
    @FXML
    private Label lastRatingLabel;
    @FXML
    private Label ratingAvgLabel;
    @FXML
    private Label activePatientIDLabel;
    @FXML
    private TextField patientName;
    @FXML
    private TextField patientEmail;
    @FXML
    private TextField patientPassword;


    // Left sidebar
    @FXML
    private VBox patientListBox;

    // Right sidebar
    @FXML
    private TextArea feedbackTextField;


    private Main main = new Main();

    void setInfo(Button deletePatientButton, TextField deletePatientID, Label activePatientNameLabel, Label lastRatingLabel, Label ratingAvgLabel, Label activePatientIDLabel, TextField patientName, TextField patientEmail, TextField patientPassword, VBox patientListBox, TextArea feedbackTextField){
        this.deletePatientButton = deletePatientButton;
        this.deletePatientID = deletePatientID;
        this.activePatientNameLabel = activePatientNameLabel;
        this.lastRatingLabel = lastRatingLabel;
        this.ratingAvgLabel = ratingAvgLabel;
        this.activePatientIDLabel = activePatientIDLabel;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPassword = patientPassword;
        this.patientListBox = patientListBox;
        this.feedbackTextField = feedbackTextField;
    }







    void setPatientListBox(VBox patientListBox) {
        this.patientListBox = patientListBox;
    }
}
