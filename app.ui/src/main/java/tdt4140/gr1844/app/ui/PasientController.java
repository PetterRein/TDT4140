package tdt4140.gr1844.app.ui;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tdt4140.gr1844.app.client.WebCalls;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PasientController {
    @FXML
    private CheckBox remove2017;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox vBoxBands, vBoxBandsTab2, vBoxGenre;

    @FXML
    private ListView listViewEarlierConcerts, listviewPop, listViewArtist, listViewPublikumsantall, listViewScene;

    @FXML
    private Label labelBandInfo;

    @FXML
    private TextField textFieldArtist, textFieldPris, searchBand;

    @FXML
    private DatePicker datePicker;

    @FXML
    ListView listViewTekniskeBehov;

    @FXML
    private  TextField foleFelt;



    Main main = new Main();

    private boolean hasInitialized = false;

    public void initialize() {
        hasInitialized = true;
    }

    private void repeatFocus(Node node) {
        Platform.runLater(() -> {
            if (!node.isFocused()) {
                node.requestFocus();
            }
        });
    }

    @FXML
    private void focusTabOne() {
        if (hasInitialized) {
            repeatFocus(vBoxBands.getChildren().get(0));
            putBandInfoInLists("Lorde");
        }
    }

    @FXML
    private void onKeyPressSearchBar() {
        String searchText = searchBand.getText();
        String festival = "all";
        if(remove2017.isSelected()){
            festival = "previous";
        }
        putBandsInList(festival, searchText);
    }

    @FXML
    private void checkRemove2017(){
        String searchText = "";
        if(!remove2017.isSelected()){
            putBandsInList("all", searchText);
        }
        else if(remove2017.isSelected()){
            putBandsInList("previous", searchText);
        }

    }


    private void putBandsInList(String festival, String searchText) {
        //System.out.println(observableListToAdd); //for å sjekke om vi faktisk klarer å hente bandene
        vBoxBands.getChildren().clear(); // fjerner de gamle knappene før vi legger til de nye.
    }

    private void putGenreInfoInLists(String genre) {
        List<String> artister = new ArrayList<>();
        List<String> festivaler = new ArrayList<>();
        List<String> publikumstall = new ArrayList<>();


        listViewArtist.setEditable(true);
        listViewScene.setEditable(true);
        listViewPublikumsantall.setEditable(true);

        listViewPublikumsantall.setItems(FXCollections.observableArrayList(publikumstall));
        listViewScene.setItems(FXCollections.observableArrayList(festivaler));
        listViewArtist.setItems(FXCollections.observableArrayList(artister));

    }

    private void putBandInfoInLists(String band) {
        ArrayList<String> bandInfo = new ArrayList<>();
        String stringToPutInTextArea = band;
        // Lager en string hvor jeg putter inn all informasjonen før jeg setter labelen sin tekst til hele stringen.

    }

    @FXML
    private void sendFoling() throws Exception {
        String data = foleFelt.getText();
        main.client.sendUserData(data);
    }

    @FXML
    private void goHome() throws Exception {
        main.client.logoutUser();
        main.changeView(rootPane, "Main");
    }
}