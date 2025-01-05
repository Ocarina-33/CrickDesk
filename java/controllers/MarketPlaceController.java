package controllers;

import data.Club;
import data.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.SocketWrapper;

import java.io.IOException;
import java.util.ArrayList;

public class MarketPlaceController {
    public Button buyButton;
    private Player selectedPlayer;
    private SocketWrapper socketWrapper;
    private Scene scene;
    private String LoggedInClubName;

    private Club Currentclub = new Club();
    public void setClub(Club club,String clubName){
        this.Currentclub = club;
        this.LoggedInClubName=clubName;
    }

    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    @FXML
    private ListView<String> playerListView = new ListView<>();
    @FXML
    private Button showDetailsButton;

    @FXML
    private Button backButton;

    @FXML
    private VBox playerListBox;  // Box to display the list of players

    private ArrayList<Player> players;

    @FXML
    public void setPlayers(ArrayList<Player> player) {
        this.players = player;
        populatePlayerList(players);
    }


    public void buyPlayer() throws IOException {
            socketWrapper.write("BuyPlayer");
            socketWrapper.write(Currentclub.getName());
            socketWrapper.write(selectedPlayer);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Congratulations.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Scene scene = new Scene(root);

            Congratulations pc = fxmlLoader.getController();
             pc.setSocketWrapper(socketWrapper);
             pc.setClub(Currentclub,LoggedInClubName);
             pc.setStage(stage);
             pc.setPlayers(players);

              stage.setScene(scene);
              stage.setTitle("Congratulations!");
              stage.show();


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
    private void back(javafx.event.ActionEvent event) throws IOException, ClassNotFoundException {
        MenuLoader menuLoader = new MenuLoader();
        menuLoader.loadMenuScreen(Currentclub, socketWrapper, stage,LoggedInClubName);
    }



}
