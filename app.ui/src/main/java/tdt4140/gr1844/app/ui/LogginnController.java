package tdt4140.gr1844.app.ui;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class LogginnController {
    @FXML
    private CheckBox remove2017;

    @FXML
    private AnchorPane rootPane;

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

    public Button createButtonTab(String buttonInput, String tab) {
        // Denne lager og returnerer en Button.
        final Button button = new Button(buttonInput);
        button.setId("arrScenes");
        button.setPrefSize(200,20);
        button.setOnMouseClicked(event -> {
            if (tab.equals("tab1")) {
                // Når du trykker på knappen så kjøres putBandInfoInLists med bandets navn som argument.
                //putBandInfoInLists(buttonInput);
            } else if (tab.equals("tab2")) {
                //textFieldArtist.setText(buttonInput);
            }
            else if (tab.equals("tab3")) {
                //putGenreInfoInLists(buttonInput);
            } else {
                //textFieldArtist.setText(buttonInput);
            }
        });
        return button;
    }

    private void howToDeleteOldButtonsInvBox() {
        //vBoxBands.getChildren().clear(); // fjerner de gamle knappene før vi legger til de nye.
    }


    @FXML
    private void goHome() throws Exception {
        //client.logoutUser();
        Main main = new Main();
        main.changeView(rootPane, "Main");
    }
}