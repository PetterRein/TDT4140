package tdt4140.gr1844.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import tdt4140.gr1844.app.client.WebCalls;

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

    private void getInfo(){
        shared.setInfo(deletePatientButton,deletePatientID,activePatientNameLabel,lastRatingLabel,ratingAvgLabel,activePatientIDLabel,patientName,patientEmail,patientPassword,patientListBox,feedbackTextField);
    }

    @FXML
    public void initialize() throws Exception {
        doctorLabel.setText("Welcome Dr. " + main.getName());
        getInfo();
        shared.updatePatientList(getPatients());
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
        shared.updatePatientList(getPatients());
    }

    @FXML
    private void updateDoctor() throws Exception {
        // TODO: Update doctorID to null
    }

    @FXML
    private void sendFeedback() throws Exception {
        shared.sendFeedback();
    }

}
