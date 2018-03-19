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

public class LogginnController {
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

    String sessionCookie = "123";

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
    private void focusTabThree() {
        repeatFocus(vBoxGenre.getChildren().get(0));
        putGenreInfoInLists("Pop");
    }

    public Button createButtonTab(String buttonInput, String tab) {
        // Denne lager og returnerer en Button.
        final Button button = new Button(buttonInput);
        button.setId("arrScenes");
        button.setPrefSize(200,20);
        button.setOnMouseClicked(event -> {
            if (tab.equals("tab1")) {
                // Når du trykker på knappen så kjøres putBandInfoInLists med bandets navn som argument.
                putBandInfoInLists(buttonInput);
            } else if (tab.equals("tab2")) {
                textFieldArtist.setText(buttonInput);
            }
            else if (tab.equals("tab3")) {
                putGenreInfoInLists(buttonInput);
            } else {
                textFieldArtist.setText(buttonInput);
            }
        });
        return button;
    }

    private void putGenreInList() {
        //Legger til alle sjangere i listen i tab3

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
    private void sendOffer() {
        // Jeg formaterer datoer til dag - måned - år.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            String date = datePicker.getValue().format(formatter);
            int[] dateSplitted = new int[3];
            int[] festivalStart = new int[3];
            int[] festivalSlutt = new int[3];



            String artist = textFieldArtist.getText();
            int pris = Integer.valueOf(textFieldPris.getText());
        } catch (Exception e) {
            //System.out.println("Velg dato.");
        }
        textFieldArtist.setText("");
        textFieldPris.setText("");
        // Fjerner informasjon i textfieldene etter den har blitt lagret.
    }




    @FXML
    private void goHome() throws Exception {
        WebCalls webCalls = new WebCalls();

        webCalls.logoutUser("Denne brukes ikke", sessionCookie);
        Main main = new Main();
        main.changeView(rootPane, "Main");
    }
}