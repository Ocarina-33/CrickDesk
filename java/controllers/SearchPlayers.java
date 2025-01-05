package controllers;

import data.Club;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import util.SocketWrapper;

import java.io.IOException;

public class SearchPlayers {

    private SocketWrapper socketWrapper;
    private Parent originalRoot;

    private String LoggedInClubName;
    private Club Currentclub = new Club();
    public void setClub(Club club,String clubName){
        this.Currentclub = club;
        this.LoggedInClubName=clubName;
    }

    private Stage stage;
    public void setStage(Stage stage) {
        this.stage=stage;
    }

    @FXML
    private Button searchName;
    @FXML
    private Button searchClub;
    @FXML
    private Button searchPosition;
    @FXML
    private Button searchSalaryRange;
    @FXML
    private Button countryWiseCount;
    @FXML
    private Button backButton;


    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }
    @FXML
    private void searchPlayerbyName() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlayerByName.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        PlayerByNameController pc = loader.getController();

        pc.setSocketWrapper(socketWrapper);
        pc.setClub(Currentclub,LoggedInClubName);
        pc.SetStage(stage);
        stage.setScene(scene);

        stage.setTitle("Search Player by Name");
        stage.show();
    }

    @FXML
    private void searchPlayerbyClubAndCountry() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlayerByClubAndCountry.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        PlayerByClubAndCountryController pc = loader.getController();

        pc.setSocketWrapper(socketWrapper);
        pc.SetStage(stage);
        pc.setClub(Currentclub,LoggedInClubName);

        stage.setScene(scene);
        stage.setTitle("Search Player by Club");
        stage.show();

    }


    @FXML
    private void searchPlayerbyPosition() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlayerByPosition.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        PlayerByPositionController pc = loader.getController();
        pc.setSocketWrapper(socketWrapper);
        pc.setClub(Currentclub,LoggedInClubName);
        pc.setClub(Currentclub,LoggedInClubName);

        stage.setScene(scene);
        pc.SetStage(stage);
        stage.setTitle("Search Player by Position");
        stage.show();

    }

    @FXML
    private void searchPlayerBySalaryRange() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlayerBySalaryRange.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        PlayerBySalaryRangeController pc = loader.getController();
        pc.setSocketWrapper(socketWrapper);
        pc.setClub(Currentclub,LoggedInClubName);
        pc.SetStage(stage);

        stage.setScene(scene);
        stage.setTitle("Search Player by Salary Range");
        stage.show();
    }

    @FXML
    private void CountryWiseCount() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CountryWisePlayerCount.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        CountryWisePlayerCountController pc = loader.getController();
        pc.setSocketWrapper(socketWrapper);
        pc.setStage(stage);
        pc.setClub(Currentclub,LoggedInClubName);

        stage.setScene(scene);
        stage.setTitle("Country Wise Player Count");
        stage.show();

    }

    @FXML
    private void back(javafx.event.ActionEvent event) throws IOException, ClassNotFoundException {
        MenuLoader menuLoader = new MenuLoader();
        menuLoader.loadMenuScreen(Currentclub, socketWrapper, stage,LoggedInClubName);
    }



//    private void loadNewScene(String fxmlFile) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/"+fxmlFile));
//        Parent root = loader.load();
//        Scene scene = new Scene(root);
//        PlayerByNameController pc = loader.getController();
//        pc.setSocketWrapper(socketWrapper);
//        Stage stage = new Stage();
//        stage.setScene(scene);
//        stage.show();
    }

