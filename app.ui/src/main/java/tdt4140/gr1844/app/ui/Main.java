package tdt4140.gr1844.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Paths;

public class Main extends Application {
    /**public static List<Festival> festivals;
    public static List<Offer> offers;**/

    public static void main(String[] args) {
        launch(args);
    }

    ArrayList<String> response = new ArrayList<>();

    String SessionCookie = "123";

    @Override
    public void start(Stage primaryStage) throws Exception {
        java.nio.file.Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        //"/app.client/src/main/java/tdt4140/gr1844/app/client/"
        System.out.println("Current relative path is: " + s);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource( "Main.fxml"));
        primaryStage.setTitle("Hvordan føler du deg?");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public void changeView(AnchorPane rootPane, String fxmlFile) {
        fxmlFile = fxmlFile.replace("ø", "o");
        fxmlFile = fxmlFile.replace("-", "");
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlFile + ".fxml"));
            rootPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateCookie(CloseableHttpResponse response){
        Header[] ws = response.getAllHeaders();
        String cookie1 = "non";
        for (Header header: ws){
            if (header.getName().equals("cookie")){
                cookie1 = header.getValue();
            }
            System.out.println(header);
        }
        SessionCookie = cookie1;
    }
}
