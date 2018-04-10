package tdt4140.gr1844.app.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tdt4140.gr1844.app.client.WebCalls;

import java.io.IOException;
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

    @FXML
    private TextArea Tilbakemedling;
	
	@FXML
	private TextField nyBrukerNavn;
	
	@FXML
	private TextField nyBrukerEpost;
	
	@FXML
	private TextField nyBrukerPassord;
	
	@FXML
	private TextField slettBrukerEpost;

    Main main = new Main();

    String pasientNameString = "artistName";

    @FXML
    public void initialize() throws Exception {
        sisteFoling.setVisible(false);
        /**TODO
         * Finne alle pasienter til en lege og legge de i pasients
         * Regne ut gjennomsnittet til hver bruker for å sette det også
         * Gjør sånn at index 0 er Navn på Pasient, index 1 er siste rapport, index 2 er gjennomsnitt og index 3 til n er følinger så kan jeg fikse resten
         *
         */
        /**String[] response = Main.client.getDoctorsPatients();
        System.out.println(response[4]);
        String[] pasients  = response[4].split("/");
        ArrayList<ArrayList<String>> pasientsName = new ArrayList<>();
        int numbersOfPasients = 0;
        if (pasients.length > 1){
            System.out.println("#");
            for (int i = 0; i < pasients.length; i = i + 2){
                ArrayList<String> pasient = new ArrayList<>();
                pasient.add(pasients[i]);
                //String[] response1 = Main.client.getPatientData(pasients[i+1]);
                //System.out.println(response1[5]);
                String rating = "3"; //Hard Coder inn noe ikke vi egenltig skal gjøre
                pasient.add(pasients[i+1]);
                pasient.add(rating);
                pasientsName.add(pasient);

            }
        }

        ArrayList<ArrayList<String>> pasients = new ArrayList<>();
        ArrayList<String> pasient1 = new ArrayList<>();
        pasient1.add("Per");
        pasient1.add("03.03.2018");
        pasient1.add("-10");
        pasient1.add("5");
        ArrayList<String> pasient2 = new ArrayList<>();
        pasient2.add("Tor");
        pasient2.add("04.04.2018");
        pasient2.add("5");
        pasient2.add("4");
        pasients.add(pasient1);
        pasients.add(pasient2);

        // Denne funksjonen blir kjørt automatisk når alt er loadet og du kan begynne å endre på ting.
        addButtons(pasientsName, needsNotSent1);**/
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
        if (pasient.size() > 1){
            int scoreAverage = Integer.parseInt(pasient.get(2));
            if(scoreAverage < 5){
                button.setId("dangerPasient");
            }
            else {
                button.setId("offerButt");
            }
        }
        else {
            button.setId("offerButt");
        }
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
        //main.client.logoutUser();
        main.changeView(rootPane, "Main");
    }

    private void updateStuff(String pasientName1, String sisteFoling1, String score1){
        this.pasientNameString = pasientName1;
        pasientName.setText("Pasient: " + pasientName1);
        sisteFoling.setText("Siste føling: " + sisteFoling1);
        score.setText("Score: " + score1);
    }

    @FXML
    private void addNewPasient() throws Exception {
        String userEmail = nyBrukerEpost.getText();
        String userName = nyBrukerNavn.getText();
        String userPassword = nyBrukerPassord.getText();
        System.out.println(userEmail);
        System.out.println(userName);
        System.out.println(userPassword);
        //main.client.addUser(userName,userPassword,userEmail, "Patient");
    }

    @FXML
    private void delPasient() throws Exception {
        String userEmail = slettBrukerEpost.getText();
        //main.client.delUser(userEmail);
    }

    @FXML
    private void sendTilbakeMedling() throws Exception {
        String tilbakemedling = Tilbakemedling.getText();
        //main.client.sendFeedback(tilbakemedling);
    }

}
