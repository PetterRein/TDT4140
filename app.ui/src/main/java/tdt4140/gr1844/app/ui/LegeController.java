package tdt4140.gr1844.app.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;
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


    private String pasientNameString = "artistName";

    Main main = new Main();

    @FXML
    public void initialize() throws Exception {
        sisteFoling.setVisible(false);
        /**TODO
         * Finne alle pasienter til en lege og legge de i pasients
         * Regne ut gjennomsnittet til hver bruker for å sette det også
         * Gjør sånn at index 0 er Navn på Pasient, index 1 er siste rapport, index 2 er gjennomsnitt og index 3 til n er følinger så kan jeg fikse resten
         *
         */
        System.out.println(main.getSessionCookie());
        JSONObject response = WebCalls.sendGET("action=listPatients&doctorID=" + main.getUserID() + "&cookie=" + main.getSessionCookie());
        System.out.println(response);
        JSONArray patients = response.getJSONArray("patients");
        addButtons(patients, needsNotSent1);
    }

    private void popListView(List<String> needList, ListView listArea) {
        ObservableList<String> obsList = FXCollections.observableArrayList(needList);
        listArea.setItems(obsList);
    }

    private void addButtons(JSONArray patients, VBox needsList) throws Exception {
        for(Object patient : patients){
            Button btnNumber = createButton((JSONObject) patient);
            needsList.getChildren().add(btnNumber);
        }
    }

    private Button createButton(JSONObject patient) throws Exception {
        final Button button = new Button(patient.getString("name"));
        System.out.println(patient);
        JSONObject feelings = WebCalls.sendGET("action=listFeelings&patientID=" + patient.getInt("id") + "&orderBy=desc&cookie=" + main.getSessionCookie());
        int rating = feelings.getJSONArray("feelings").getJSONObject(0).getInt("rating");
        if(rating < 5){
            button.setId("dangerPasient");
        }
        else {
            button.setId("offerButt");
        }
        button.setPrefSize(200, 20);
        button.setOnMouseClicked(event -> {
            try {
                buttonName = button;
                updateStuff(patient.getString("name"), patient.getString("email"), rating);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return button;
    }

    @FXML
    private void goHome() throws Exception {
        //main.client.logoutUser();
        main.changeView(rootPane, "main");
    }

    private void updateStuff(String pasientName1, String sisteFoling1, int score1){
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
        JSONObject response = main.createUser(userName,userEmail,userPassword, main.getUserID());
        //TODO Lag at det kommer en alert om det var sukssess eller ikke
    }

    @FXML
    private void delPasient() throws Exception {
        String userID = slettBrukerEpost.getText();
        JSONObject response = main.delUser(Integer.parseInt(userID));
        //TODO Lag at det kommer en alert om det var sukssess eller ike
    }

    @FXML
    private void sendTilbakeMedling() throws Exception {
        String tilbakemedling = Tilbakemedling.getText();
        JSONObject response = main.sendFeedback(tilbakemedling);
        //TODO Lag at det kommer en alert om det var sukssess eller ike

    }

}
