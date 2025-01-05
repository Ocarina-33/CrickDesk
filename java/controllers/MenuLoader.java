package controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import data.*;
import util.SocketWrapper;

public class MenuLoader {

    public void loadMenuScreen(Club loggedInClub, SocketWrapper socketWrapper,Stage stage,String clubName) throws IOException, ClassNotFoundException {
       // System.out.println("Nigga");
        // Load the menu screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Menu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        MenuController controller = loader.getController();
        controller.setSocketWrapper(socketWrapper);
        controller.setClub(loggedInClub,clubName);
        controller.SetStage(stage);

        stage.setScene(scene);
        stage.setTitle("Menu");
        stage.show();


          //  System.out.println("ClubName:"+clubName);
         //  controller.updateUI();
        //        Stage stage = new Stage();


    }

}
