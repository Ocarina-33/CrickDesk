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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.SocketWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerBySalaryRangeController {
    private Player selectedPlayer;
    @FXML
    private Button showDetailsButton;
    private String LoggedInClubName;
    private Club Currentclub = new Club();
    public void setClub(Club club,String clubName){
        this.Currentclub = club;
        this.LoggedInClubName=clubName;
    }

    @FXML
    private ListView<String> playerListView = new ListView<>();
    @FXML
    private TextField minSalaryField;

    @FXML
    private TextField maxSalaryField;
    @FXML
    private Button backButton;
    @FXML
    private VBox resultVBox;

    private Scene scene;

    private SocketWrapper socketWrapper;
    private ArrayList<Player> playersInRange = new ArrayList<>();

    private Stage stage;
    public void SetStage(Stage stage){
        this.stage=stage;
    }
    @FXML
    private void handleSearchBySalaryRange() {
        // Get input values
        String minSalaryText = minSalaryField.getText();
        String maxSalaryText = maxSalaryField.getText();

        // Validate input
        if (minSalaryText.isEmpty() || maxSalaryText.isEmpty()) {
            showErrorAlert("Input Error", "Both salary fields are required.");
            return;
        }

        long minSalary;
        long maxSalary;
        try {
            minSalary = Long.parseLong(minSalaryText);
            maxSalary = Long.parseLong(maxSalaryText);
        } catch (NumberFormatException e) {
            showErrorAlert("Input Error", "Salary values must be numeric.");
            return;
        }

        // Ensure min <= max
        if (minSalary > maxSalary) {
            showErrorAlert("Input Error", "Minimum salary cannot be greater than maximum salary.");
            return;
        }

        try {
            // Communicate with server
            socketWrapper.write("searchPlayerBySalaryRange");
            socketWrapper.write(minSalaryText);
            socketWrapper.write(maxSalaryText);

            String response = (String) socketWrapper.read();
            if ("Players found".equals(response)) {
                playersInRange = (ArrayList<Player>) socketWrapper.read();
                populatePlayerList(playersInRange);
            } else {
                resultVBox.getChildren().clear();
                showInfoAlert("No Players Found", "No players found in the specified salary range.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Connection Error", "An error occurred while connecting to the server.");
        }
    }



    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
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

}

