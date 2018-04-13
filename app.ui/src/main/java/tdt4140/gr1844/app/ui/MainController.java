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
    private TextField Brukernavn;

    @FXML
    private TextField Passord;

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
        String email = Brukernavn.getText();
        String password = Passord.getText();
        JSONObject json = WebCalls.sendGET("action=login&email=" + email +  "&password=" + password);
        if (json.getString("status").equals("OK")){
            main.setSessionCookie(json.getString("cookie"));
            System.out.println(main.getSessionCookie());
            main.setUserID(json.getJSONObject("user").getString("userID"));
            switch (json.getJSONObject("user").getString("role")) {
                case "admin":
                    main.changeView(rootPane, "admin");
                    break;
                case "doctor":
                    main.changeView(rootPane, "Lege");
                    break;
                case "patient":
                    main.changeView(rootPane, "Pasient");
                    break;
                default:
                    System.out.println("Du har en role som ikke finnes");
                    break;
            }
        }
        //TODO: Lag en alert som sier at email eller passord er feil
        else System.out.println(json.getString("message"));
    }

}