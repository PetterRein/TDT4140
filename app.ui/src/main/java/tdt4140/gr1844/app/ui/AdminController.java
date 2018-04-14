package tdt4140.gr1844.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class AdminController {
    public Label doctorLabel;
    public Button deletePatientButton;
    public TextField deletePatientID;

    // Active user panel
    @FXML
    private Label activePatientNameLabel;

    @FXML
    private  Label activePatientIDLabel;


    @FXML
    private VBox patientListBox;


    private Main main = new Main();

    private Shared shared = new Shared();

    @FXML
    private AnchorPane rootPane;


    @FXML
    public void initialize() throws Exception {
        doctorLabel.setText("Welcome Admin " + main.getName());
        shared.updatePatientList();
    }

    @FXML
    private void logout() throws Exception {
        main.logout();
        main.changeView(rootPane, "Main");
    }

    @FXML
    private void removePatient() throws Exception {
        shared.removePatient();
    }

}
