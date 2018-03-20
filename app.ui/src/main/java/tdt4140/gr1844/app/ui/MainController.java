package tdt4140.gr1844.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import tdt4140.gr1844.app.client.WebCalls;
import tdt4140.gr1844.app.ui.Main;


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

    Main a = new Main();

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
        String role = "Lege";
        WebCalls webCalls = new WebCalls();
        String epost = Brukernavn.getText();
        String passord = Passord.getText();
        /**CloseableHttpResponse response = webCalls.sendLoginPost(epost, passord, a.SessionCookie);
        a.updateCookie(response);
        int responseCode = response.getStatusLine().getStatusCode();
        if (responseCode == 200){
            if (role.equals("Lege")){
                a.changeView(rootPane, "Lege");
            }
            else if (role.equals("Pasient")){
                a.changeView(rootPane, "Logginn");
            }

        }
        else {
            if (role.equals("Lege")){
                a.changeView(rootPane, "Lege");
            }
            else if (role.equals("Pasient")){
                a.changeView(rootPane, "Logginn");
            }
        }**/
        a.changeView(rootPane, "Lege");
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