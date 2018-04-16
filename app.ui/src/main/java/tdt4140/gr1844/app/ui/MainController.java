package tdt4140.gr1844.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;
import tdt4140.gr1844.app.client.WebCalls;


public class MainController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label loginError;

    private Main main = new Main();

    @FXML
    public void initialize() {
        // Denne funksjonen blir kjørt automatisk når alt er loadet og du kan begynne å endre på ting.
        loginError.setVisible(false); //Setter failed boksen til borte så lenge vi ikke skifter den
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
                    loginError.setVisible(true);
                    loginError.setText("No such role");
                    break;
            }
            emailTextField.clear();
            passwordTextField.clear();
        }
        else {
            loginError.setVisible(true);
            loginError.setText("ERROR: " + userResponse.getString("message"));
        }
    }


}