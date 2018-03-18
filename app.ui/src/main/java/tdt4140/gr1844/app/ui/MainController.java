package tdt4140.gr1844.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import util.WebCalls;

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

    Main a = new Main();

    @FXML
    public void initialize() {
        // Denne funksjonen blir kjørt automatisk når alt er loadet og du kan begynne å endre på ting.
        addLoginElementsInList();
    }

    public void addLoginElementsInList() {
        //Legge til default login knappene.

    }

    @FXML
    public void sendLogin() throws Exception {
        WebCalls webCalls = new util.WebCalls();
        String epost = Brukernavn.getText();
        String passord = Passord.getText();
        String [] response = webCalls.loginUser("Dette gjør ingenting...", epost, passord);
        if (response[1] == "200"){
            a.changeView(rootPane, "Logginn");
        }
        else {
            a.changeView(rootPane, "Logginn");
        }

    }

    public void addButtons(ArrayList<String> jobs, VBox jobsList) {
        for (int i = 0; i < jobs.size(); i++) {
            Button btnNumber = createButton(jobs.get(i));
            jobsList.getChildren().add(btnNumber);
        }
    }

    public Button createButton(String name) {
        final Button button = new Button(name);
        button.setId("jobButt");
        button.setPrefSize(210, 50);
        button.setOnMouseClicked(event -> {
            try {
                Main main = new Main();
                main.changeView(rootPane, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return button;
    }
}