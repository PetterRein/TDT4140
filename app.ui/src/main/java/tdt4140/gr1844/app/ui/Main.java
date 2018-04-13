package tdt4140.gr1844.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONObject;
import tdt4140.gr1844.app.client.WebCalls;
import tdt4140.gr1844.app.core.QueryString;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    /**public static List<Festival> festivals;
    public static List<Offer> offers;**/
    public static String API_URL = "http://api.moholt.me?";

    public static void main(String[] args) {
        launch(args);
    }

    ArrayList<String> response = new ArrayList<>();

    String SessionCookie = "123";

    String userID = "-1";

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

        Map<String, String> params = new HashMap<>();
        params.put("action", "login");
        //Send.sendGET(params);
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
    
    public JSONObject createUser(String name, String email, String password, String doctorID) throws Exception {
        return  WebCalls.sendGET("action=createPatient&name=" + name + "&email=" + email + "&password=" + " &doctorID=" + doctorID);
    }

    public JSONObject delUser(int userID) throws Exception {
        return  WebCalls.sendGET("action=deleteUser&userID=" + userID);
    }

    public JSONObject sendFeedback(String feedback) throws Exception {
        return WebCalls.sendGET("action=createFeedback&message=" + feedback +"&cookie=" + SessionCookie);
    }
}
