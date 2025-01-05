package controllers;

import data.Club;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import util.SocketWrapper;

import java.io.IOException;

public class CountryWisePlayerCountController {
    private String LoggedInClubName;
    private Club Currentclub = new Club();
    public void setClub(Club club,String clubName){
        this.Currentclub = club;
        this.LoggedInClubName=clubName;
    }
    private SocketWrapper socketWrapper;

    @FXML
    private TextArea countryCountTextArea;
    @FXML
    private Button backButton;
    private Scene scene;


    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
        getCountryWisePlayerCount();

    }
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage=stage;
    }

    public void getCountryWisePlayerCount() {
        try {
            if(socketWrapper!=null) {
                socketWrapper.write("searchCountryCount");

                String countryCount = (String) socketWrapper.read();
                countryCountTextArea.setText(countryCount);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            countryCountTextArea.setText("Error occurred while fetching data.");
        }
    }

    @FXML
    private void back(javafx.event.ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SearchPlayers.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        SearchPlayers pc = loader.getController();
        pc.setSocketWrapper(socketWrapper);
        pc.setStage(stage);
        pc.setClub(Currentclub,LoggedInClubName);
        stage.setScene(scene);
        stage.setTitle("Search Players By Category");
        stage.show();

    }


}
