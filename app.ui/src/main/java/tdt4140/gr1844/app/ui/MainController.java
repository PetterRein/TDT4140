package tdt4140.gr1844.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;
import tdt4140.gr1844.app.client.WebCalls;
import tdt4140.gr1844.app.core.QueryString;

import java.net.URL;
import java.util.ArrayList;


public class MainController {
    @FXML
    private VBox jobsList;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField faillogginn;

    Main main = new Main();

    @FXML
    public void initialize() {
        // Denne funksjonen blir kjørt automatisk når alt er loadet og du kan begynne å endre på ting.
        faillogginn.setVisible(false); //Setter failed boksen til borte så lenge vi ikke skifter den
        addLoginElementsInList();
    }

    public void addLoginElementsInList() {
        //Legge til default login knappene.

    }

    @FXML
    public void sendLogin() throws Exception {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        JSONObject userResponse = WebCalls.sendGET(
                "action=login" +
                        "&email=" + email +
                        "&password=" + password
        );



        if (userResponse.getString("status").equals("OK")){
            main.setUser(userResponse);
            switch (main.getRole()) {
                case "admin":
                    main.changeView(rootPane, "Admin");
                    break;
                case "doctor":
                    main.changeView(rootPane, "Doctor");
                    break;
                case "patient":
                    main.changeView(rootPane, "Patient");
                    break;
                default:
                    break;
            }
        }
        //TODO: Lag en alert som sier at email eller passord er feil
        else System.out.println(userResponse.getString("message"));
    }

}