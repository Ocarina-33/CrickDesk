package controllers;
import com.sun.javafx.stage.EmbeddedWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.*;
import data.*;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerByNameController {

    @FXML
    private ListView<String> playerListView = new ListView<>();

    private Player selectedPlayer;
    private SocketWrapper socketWrapper;
    private String LoggedInClubName;
    private Club Currentclub = new Club();
    @FXML
    private Button backButton;
    @FXML
    private TextField playerNameField;  // The field where user enters the player name

    @FXML
    private VBox resultVBox; // The VBox where player details will be displayed

    @FXML
    private Button showDetailsButton;

    public void setSocketWrapper(SocketWrapper socketWrapper) {

        this.socketWrapper = socketWrapper;
    }

    public void setClub(Club club,String clubName){
        this.Currentclub = club;
        this.LoggedInClubName=clubName;
    }
    private Stage stage;
    public void SetStage(Stage stage){
        this.stage=stage;
    }

    @FXML
    public void handleFetchPlayerDetails() {
        String playerName = playerNameField.getText();

        try {
            if (!playerName.isEmpty()) {
                if (socketWrapper == null) {
                    // Handle the error gracefully (e.g., show an alert or log an error)
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Socket Error");
                    alert.setHeaderText("SocketWrapper is not initialized.");
                    alert.setContentText("Please make sure the connection is established.");
                    alert.showAndWait();
                    return;  // Exit the method if socketWrapper is null
                }
                // Send "search" command followed by player name to the server
                socketWrapper.write("searchByName");  // Command to search for player
                socketWrapper.write(playerName);  // Send player name for the search

                // Wait for the server response with player details
                String response = (String) socketWrapper.read();

                if (response.equals("Player found")) {
                    // Read the player details object
                    ArrayList<Player> playerList = (ArrayList<Player>) socketWrapper.read();
                    populatePlayerList(playerList);
                } else {
                    // Show error alert if player is not found
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Player Not Found");
                    alert.setContentText("The player name you entered does not exist.");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any connection or I/O errors
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
        pc.setStage(stage);
        pc.setClub(Currentclub,LoggedInClubName);
        stage.setScene(scene);
        stage.setTitle("Search Players By Category");
        stage.show();

    }
}

