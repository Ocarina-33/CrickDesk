package controllers;
import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import util.SocketWrapper;

import java.io.IOException;
import java.util.ArrayList;

public class MenuController {


    private String clubName;

    private SocketWrapper socketWrapper;

    @FXML
    private Text welcomeText;

    @FXML
    private ListView<String> playerListView = new ListView<>();

    @FXML
    private Button showDetailsButton;

    @FXML
    private Button searchPlayerButton;
    @FXML
    private Button searchClubButton;
    @FXML
    private Button addPlayerButton;
    @FXML
    private Button marketplaceButton;
    @FXML
    private Button refreshButton;

    public Player selectedPlayer;

    private Club currentClub = new Club();

    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    private Stage stage;
    public void SetStage(Stage stage){
        this.stage=stage;
    }



    public void setClub(Club club, String clubName) throws IOException, ClassNotFoundException {
        this.currentClub = club;
        this.clubName = clubName;
        updateUI();
    }

    public void updateUI() throws IOException, ClassNotFoundException {
        loadPlayers();

        // Disable the show details button initially
        showDetailsButton.setDisable(true);

        // Set the listener for player selection
        playerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showDetailsButton.setDisable(newValue == null);
        });
    }

    private void loadPlayers() throws IOException, ClassNotFoundException {
        ObservableList<String> playerNames = FXCollections.observableArrayList();
        socketWrapper.write("UpdatePlayerList");
        socketWrapper.write(currentClub.getName());
        ArrayList<Player> CurrentClubPLayers =(ArrayList<Player>) socketWrapper.read();
        currentClub.setPlayers(CurrentClubPLayers);

        // Check if currentClub is null
        if (currentClub == null) {
            //System.out.println("Warning: currentClub is null in loadPlayers()!");
            playerListView.setItems(playerNames);  // Show an empty list
            return; // Return early to prevent NullPointerException
        }

        // Check if currentClub.getPlayers() is null
        if (currentClub.getPlayers() == null) {
            //System.out.println("Warning: getPlayers() returned null!");
            playerListView.setItems(playerNames);  // Show an empty list
            return;
        }

        for (Player p : currentClub.getPlayers()) {
            playerNames.add(p.getName());
        }

        playerListView.setItems(playerNames);
    }

    @FXML
    private void showPlayerDetails() throws IOException {
       String selectedPlayerName = playerListView.getSelectionModel().getSelectedItem();

        for(Player p : currentClub.getPlayers()) {
            if (p.getName().equals(selectedPlayerName)) {
                selectedPlayer = p;
            }
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/details.fxml"));
        Scene scene = new Scene(loader.load());

        DetailsController pc = loader.getController();
        pc.setPlayer(selectedPlayer);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }



    @FXML
    private void goToSearchPlayers() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SearchPlayers.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        SearchPlayers sp = loader.getController();
        sp.setSocketWrapper(socketWrapper);
        sp.setClub(currentClub,clubName);
        sp.setStage(stage);

        stage.setScene(scene);
        stage.setTitle("Search Player By Category");
        stage.show();
    }

    @FXML
    private void goToSearchClubs() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SearchClubs.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        SearchClubs sc = loader.getController();
        sc.setSocketWrapper(socketWrapper);
        sc.setClub(currentClub,clubName);
        sc.setStage(stage);


        stage.setScene(scene);
        stage.setTitle("Search Player For Specific Club");
        stage.show();
    }


    @FXML
    private void goToAddPlayer() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddPlayer.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        AddPlayer pc = loader.getController();
        pc.setSocketWrapper(socketWrapper);
        pc.SetStage(stage);
        pc.setClub(currentClub,clubName);

        stage.setScene(scene);
        stage.setTitle("Add Player To Your Own Club!");
        stage.show();
    }

    @FXML
    private void goToMarketplace() throws IOException, ClassNotFoundException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MarketPlace.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        socketWrapper.write("searchPlayerForMarketplace");
        socketWrapper.write(currentClub.getName());
        ArrayList<Player> players = (ArrayList<Player>) socketWrapper.read();

        MarketPlaceController pc = loader.getController();
        pc.setSocketWrapper(socketWrapper);
        pc.setStage(stage);
        pc.setClub(currentClub,clubName);
        pc.setPlayers(players);
        stage.setScene(scene);
        stage.setTitle("Market Place");
        stage.show();
    }

    @FXML
    private void sellPlayer() throws IOException, ClassNotFoundException {
//        String selectedPlayerName = playerListView.getSelectionModel().getSelectedItem();
//        socketWrapper.write("sellPlayer");
//        socketWrapper.write(selectedPlayerName);
        String selectedPlayerName = playerListView.getSelectionModel().getSelectedItem();

        // Check if a player is selected
        if (selectedPlayerName == null) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("No Player Selected");
            noSelectionAlert.setHeaderText(null);
            noSelectionAlert.setContentText("Please select a player to sell.");
            noSelectionAlert.showAndWait();
            return;
        }

        // Send the sell request to the server
        socketWrapper.write("sellPlayer");
        socketWrapper.write(selectedPlayerName);

        // Show an alert to confirm the request was sent
        Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
        confirmationAlert.setTitle("Request Sent");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Your request to sell " + selectedPlayerName + " has been sent to the server.");
        confirmationAlert.showAndWait();
    }


//    private void loadNewScene(String fxmlFile) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/"+fxmlFile));
//        Parent root = loader.load();
//        Scene scene = new Scene(root);
//        Stage stage = new Stage();
//        stage.setScene(scene);
//        stage.show();
//    }


}

