
package client;
import controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.SocketWrapper;

public class Client extends Application {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    private SocketWrapper socketWrapper;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Show the login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load());
        LoginController controller = loader.getController();
        controller.setSocketWrapper(new SocketWrapper(SERVER_ADDRESS, SERVER_PORT));

        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}