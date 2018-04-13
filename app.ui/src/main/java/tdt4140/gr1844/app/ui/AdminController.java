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

public class AdminController {
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

    private CommonFunctions comFun = new CommonFunctions();


    public void getInfo(){
        comFun.setInfo(deletePatientButton,deletePatientID,activePatientNameLabel,lastRatingLabel,ratingAvgLabel,activePatientIDLabel,patientName,patientEmail,patientPassword,patientListBox,feedbackTextField);
    }

    @FXML
    public void initialize() throws Exception {
        doctorLabel.setText("Welcome Dr. " + main.getName());
        getInfo();
        comFun.updatePatientList();
    }

    @FXML
    private void logout() throws Exception {
        main.logout();
        main.changeView(rootPane, "Main");
    }

    @FXML
    private void registerPatient() throws Exception {
        comFun.registerPatient();
    }

    @FXML
    private void removePatient() throws Exception {
        comFun.removePatient();
    }

    @FXML
    private void sendFeedback() throws Exception {
        comFun.sendFeedback();
    }
}
