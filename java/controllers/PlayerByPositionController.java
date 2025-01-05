package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.*;
import data.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerByPositionController {
    private Player selectedPlayer;
    private SocketWrapper socketWrapper;
    private Scene scene;
    private String LoggedInClubName;
    private Club Currentclub = new Club();
    public void setClub(Club club,String clubName){
        this.Currentclub = club;
        this.LoggedInClubName=clubName;
    }

    @FXML
    private Button BatsmanButton;
    @FXML
    private Button BowlerButton;
    @FXML
    private Button WicketKeeperButton;
    @FXML
    private Button AllRounderButton;
    @FXML
    private Button showDetailsButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField positionField;  // Required position input

    @FXML
    private ListView<String> playerListView = new ListView<>();
    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    private Stage stage;

    public void SetStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    public void handleSearchPlayersByPosition(ActionEvent event) {
        // Get the button that triggered the event
        Button clickedButton = (Button) event.getSource();
        String position = clickedButton.getText(); // Fetch the button's text (e.g., "Batsman", "AllRounder")

        if (position == null || position.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Position is required");
            alert.setContentText("Please select a position to search for players.");
            alert.showAndWait();
            return;
        }

        try {
            // Send search command to the server
            socketWrapper.write("searchPlayerByPosition");

            // Send position as part of the search request
            socketWrapper.write(position);

            // Read the response from the server
            String response = (String) socketWrapper.read();

            if (response.equals("Players found")) {
                // Read the list of players from the server
                ArrayList<Player> playerList = (ArrayList<Player>) socketWrapper.read();
                populatePlayerList(playerList);
            } else {
                // Show an alert if no players are found
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("No Players Found");
                alert.setHeaderText("No players match the search criteria.");
                alert.setContentText("Try searching with a different position.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Error while connecting to the server.");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }





    private void populatePlayerList(ArrayList<Player> players) {
        showDetailsButton.setDisable(true); // Disable the "Show Details" button initially

        // Create an ObservableList from the players' names
        ObservableList<String> playerNames = FXCollections.observableArrayList();
        for (Player player : players) {
            playerNames.add(player.getName());
        }

        // Set the items of the ListView
        playerListView.setItems(playerNames);

        // Add a listener to the ListView to handle selection changes
        playerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Find the selected player by name and set it to selectedPlayer
                selectedPlayer = players.stream()
                        .filter(player -> player.getName().equals(newValue))
                        .findFirst()
                        .orElse(null);

                // Enable the "Show Details" button if a player is selected
                showDetailsButton.setDisable(selectedPlayer == null);
            }
        });
    }


    @FXML
    private void showPlayerDetails() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/details.fxml"));
        Scene scene = new Scene(loader.load());

        DetailsController pc = loader.getController();
        pc.setPlayer(selectedPlayer);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }


    @FXML
    private void back(javafx.event.ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SearchPlayers.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        SearchPlayers pc = loader.getController();
        pc.setSocketWrapper(socketWrapper);
        pc.setClub(Currentclub,LoggedInClubName);
        stage.setScene(scene);
        pc.setStage(stage);
        stage.setTitle("Search Players By Category");
        stage.show();

    }

}
