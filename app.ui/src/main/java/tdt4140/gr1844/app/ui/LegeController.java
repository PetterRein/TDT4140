package tdt4140.gr1844.app.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tdt4140.gr1844.app.client.WebCalls;

import java.util.ArrayList;
import java.util.List;

public class LegeController {
    @FXML
    private VBox vboxBorderLeft;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox needsNotSent1;

    private Button buttonName;

    @FXML
    private ListView needListAdded;

    @FXML
    private Label pasientName;

    @FXML
    private Label sisteFoling;

    @FXML
    private Label score;

    String pasientNameString = "artistName";

    @FXML
    public void initialize() {
        ArrayList<ArrayList<String>> pasients = new ArrayList<>();
        ArrayList<String> pasient1 = new ArrayList<>();
        pasient1.add("Per");
        pasient1.add("03.03.2018");
        pasient1.add("-10");
        ArrayList<String> pasient2 = new ArrayList<>();
        pasient2.add("Tor");
        pasient2.add("04.04.2018");
        pasient2.add("5");
        pasients.add(pasient1);
        pasients.add(pasient2);
        // Denne funksjonen blir kjørt automatisk når alt er loadet og du kan begynne å endre på ting.
        addButtons(pasients, needsNotSent1);
    }

    private void popListView(List<String> needList, ListView listArea) {
        ObservableList<String> obsList = FXCollections.observableArrayList(needList);
        listArea.setItems(obsList);
    }

    private void addButtons(ArrayList<ArrayList<String>> pasients, VBox needsList) {
        for (ArrayList<String> pasient : pasients) {
            Button btnNumber = createButton(pasient);
            needsList.getChildren().add(btnNumber);
        }
    }

    private Button createButton(ArrayList<String> pasient) {
        final Button button = new Button(pasient.get(0));
        button.setId("offerButt");
        button.setPrefSize(200, 20);
        button.setOnMouseClicked(event -> {
            try {
                buttonName = button;
                updateStuff(pasient.get(0), pasient.get(1), pasient.get(2));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return button;
    }

    @FXML
    private void goHome() throws Exception {
        WebCalls webCalls = new WebCalls();
        String sessionCookie = "123";
        //webCalls.logoutUser("Denne brukes ikke", sessionCookie);
        Main main = new Main();
        main.changeView(rootPane, "Main");
    }

    private void updateStuff(String pasientName1, String sisteFoling1, String score1){
        this.pasientNameString = pasientName1;
        pasientName.setText("Pasient: " + pasientName1);
        sisteFoling.setText("Siste føling: " + sisteFoling1);
        score.setText("Score: " + score1);
    }
}
