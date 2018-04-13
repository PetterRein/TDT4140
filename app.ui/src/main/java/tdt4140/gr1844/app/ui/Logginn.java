package tdt4140.gr1844.app.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Logginn {

    @FXML
    private ListView needListAdded;

    @FXML
    private TextField listOfNeedsAddedLabel, need, inputFieldNeed;

    @FXML
    private Label artist, scene, date;

    @FXML
    private Button sendButton, addButton;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox needsNotSent1;

    private String artistNameString = "", sceneNameString, datoString;
    private Button buttonName;
    private List<String> needsList = new ArrayList<>();
    private String err = "error";

    @FXML
    public void initialize() {
        // listViews = Arrays.asList(listOfOfferView, needListAdded);
    }


    @FXML
    private void alertShow(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    private void showWithMouseClick() {
        hideStuffOrShow(true);
    }

    @FXML
    private void hideStuffOrShow(boolean what) {
        addButton.setVisible(what);
        need.setVisible(what);
        artist.setVisible(what);
        scene.setVisible(what);
        needListAdded.setVisible(what);
        listOfNeedsAddedLabel.setVisible(what);
        sendButton.setVisible(what);
        date.setVisible(what);
    }


    @FXML
    private void logout() throws IOException {
        Main main = new Main();
        main.changeView(rootPane, "Main");
    }


    @FXML
    private void addHoy() {
        addItem("Høyttalere", "Høyttaler");
        repeatFocus(buttonName);
    }

    @FXML
    private void addMic() {
        addItem("Mikrofoner", "Mikrofon");
        repeatFocus(buttonName);
    }

    @FXML
    private void addMon() {
        addItem("Monitorer", "Monitor");
        repeatFocus(buttonName);
    }

    @FXML
    private void addSing() {
        addItem("Sangere", "Sanger");
        repeatFocus(buttonName);
    }

    @FXML
    private void delHoy() {
        delItem("Høyttalere", "Høyttaler");
        repeatFocus(buttonName);
    }

    @FXML
    private void delMic() {
        delItem("Mikrofoner", "Mikrofon");
        repeatFocus(buttonName);
    }

    @FXML
    private void delMon() {
        delItem("Monitorer", "Monitor");
        repeatFocus(buttonName);
    }

    @FXML
    private void delSing() {
        delItem("Sangere", "Sanger");
        repeatFocus(buttonName);
    }

    private void repeatFocus(Node node) {
        Platform.runLater(() -> {
            if (!node.isFocused()) {
                node.requestFocus();
            }
        });
    }

    @FXML
    private void addItem(String itemMulti, String itemSingel) {
        if (artistNameString.equals("")) {
            alertShow(err, "Du må velge en artist først");
            inputFieldNeed.setText("");
        } else {
            boolean check = false;
            for (String s : needsList) {
                if (s.toLowerCase().contains(itemSingel.toLowerCase())) {
                    check = true;
                    String[] P = s.split("\\s+");
                    int nr = Integer.parseInt(P[0]);
                    nr++;
                    needsList.set(needsList.indexOf(s), nr + " " + itemMulti);
                    popListView(needsList, needListAdded);
                }
            }
            if (!check) {
                needsList.add("1 " + itemSingel);
                popListView(needsList, needListAdded);
            }
        }
    }

    @FXML
    private void delItem(String itemMulti, String itemSingel) {
        if (artistNameString.equals("")) {
            alertShow(err, "Du må velge en artist først");
            inputFieldNeed.setText("");
        } else {
            try {
                for (String s : needsList) {
                    if (s.toLowerCase().contains(itemMulti.toLowerCase())) {
                        String[] P = s.split("\\s+");
                        int nr = Integer.parseInt(P[0]);
                        if (nr > 2) {
                            nr--;
                            needsList.set(needsList.indexOf(s), nr + " " + itemMulti);
                            popListView(needsList, needListAdded);
                        } else {
                            nr--;
                            needsList.set(needsList.indexOf(s), nr + " " + itemSingel);
                            popListView(needsList, needListAdded);
                        }
                    } else if (s.toLowerCase().contains(itemSingel.toLowerCase())) {
                        needsList.remove(needsList.indexOf(s));
                        popListView(needsList, needListAdded);
                        break;
                    }
                }
            } catch (Exception e) {
                alertShow(err, "Du kan ikke slette noe som ikke er der");
                System.err.println(e.getMessage());
            }
        }
    }

    @FXML
    private void delNeed() {
        if (artistNameString.equals("")) {
            alertShow(err, "Du må velge en artist først");
        } else {
            int nrOfNeed = needListAdded.getSelectionModel().getSelectedIndex();
            if (nrOfNeed == -1) {
                alertShow(err, "Du må velge et behov først");
            } else {
                needsList.remove(nrOfNeed);
                popListView(needsList, needListAdded);
            }
        }
    }

    private void popListView(List<String> needList, ListView listArea) {
        ObservableList<String> obsList = FXCollections.observableArrayList(needList);
        listArea.setItems(obsList);
    }

    @FXML
    private void addNeedsToList() {
        if (artistNameString.equals("")) {
            alertShow(err, "Du må velge en artist først");
            inputFieldNeed.setText("");
        } else {
            String aNeed = inputFieldNeed.getText();
            if (!aNeed.equals("")) {
                needsList.add(aNeed);
                popListView(needsList, needListAdded);
                inputFieldNeed.setText("");
            }
        }
    }

    @FXML
    private void updateScene(String sceneName) {
        this.scene.setText("Scene: " + sceneName);
        this.sceneNameString = sceneName;
    }

    @FXML
    private void updateArtistName(String artistName) {
        this.artist.setText("Artist: " + artistName);
        this.sceneNameString = artistName;
    }

    @FXML
    private void updateDate(String dato) {
        this.date.setText("Dato: " + dato);
        this.datoString = dato;
    }

    @FXML
    private void updateInfo(String artistName, String sceneName, String dato, ArrayList<String> artistsNeeds) {
        this.artistNameString = artistName;
        this.sceneNameString = sceneName;
        this.datoString = dato;
        this.needsList = artistsNeeds;
        if (!artistsNeeds.isEmpty()) {
            popListView(artistsNeeds, needListAdded);
        }
        updateArtistName(artistName);
        updateScene(sceneName);
        updateDate(dato);
    }
}
