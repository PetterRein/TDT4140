package tdt4140.gr1844.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
        String epost = Brukernavn.getText();
        String passord = Passord.getText();
        /**String [] response = main.client.loginUser(epost,passord,epost);
        response[0] = "200";
        if (response[0] == "200"){
            if (response[3].equals("Admin")){
                main.changeView(rootPane, "Lege");
            }
            else if (response[3].equals("Patient")){
                main.changeView(rootPane, "Pasient");
            }
            else if (response[3].equals("Doctor")){
                main.changeView(rootPane, "Lege");
            }
            else {
                System.out.println("Du har en role som ikke finnes");
            }
        }
        else {
            System.out.println("Du har ikke lov");
        }**/
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