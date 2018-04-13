package tdt4140.gr1844.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;
import tdt4140.gr1844.app.client.WebCalls;

public class DoctorController {
    public Label doctorLabel;
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


    private String pasientNameString;

    private Main main = new Main();

    @FXML
    public void initialize() throws Exception {
        sisteFoling.setVisible(false);
        /**TODO
         * Finne alle pasienter til en lege og legge de i pasients
         * Regne ut gjennomsnittet til hver bruker for å sette det også
         * Gjør sånn at index 0 er Navn på Pasient, index 1 er siste rapport, index 2 er gjennomsnitt og index 3 til n er følinger så kan jeg fikse resten
         *
         */
        JSONObject response = WebCalls.sendGET("action=listPatients&doctorID=" + main.getUserID() + "&cookie=" + main.getCookie());
        JSONArray patients = response.getJSONArray("patients");
        doctorLabel.setText("Welcome Dr. " + main.getName());
        addButtons(patients, needsNotSent1);
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
        JSONObject feelings = WebCalls.sendGET("action=listFeelings&patientID=" + patient.getInt("id") + "&orderBy=desc&cookie=" + main.getCookie());
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
    private void logout() throws Exception {
        main.logout();
        main.changeView(rootPane, "Main");
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
