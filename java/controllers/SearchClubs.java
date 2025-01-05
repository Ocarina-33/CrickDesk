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

public class SearchClubs {
    private SocketWrapper socketWrapper;
    private ArrayList<Player> playerList; // Holds the players fetched from the server
    private Player selectedPlayer; // Holds the currently selected player

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
    private ListView<String> playerListView = new ListView<>();
    @FXML
    private TextField clubNameField;

    @FXML
    private Button showDetailsButton;
    @FXML
    private Button backButton;

    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    @FXML
    private void handleMaxSalary() throws Exception {
        String clubName = clubNameField.getText();

        if (clubName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Club Name is required");
            alert.setContentText("Please enter the club name to search for players.");
            alert.showAndWait();
            return;
        }

        try {
            socketWrapper.write("searchMaxSalaryOfClub");
            socketWrapper.write(clubName);

            String response = (String) socketWrapper.read();
            playerListView.getItems().clear();

            if (response.equals("Players found")) {
                playerList = (ArrayList<Player>) socketWrapper.read();
                populatePlayerList(playerList);
            } else {
                playerListView.getItems().add("No players found with Maximum Salary for club: " + clubName);

            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Error");
            alert.setHeaderText("Error connecting to the server");
            alert.setContentText("An error occurred while retrieving player data.");
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
    private void handleMaxAge(){
        String clubName = clubNameField.getText();

        if (clubName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Club Name is required");
            alert.setContentText("Please enter the club name to search for players.");
            alert.showAndWait();
            return;
        }

        try {
            socketWrapper.write("searchMaxAgeOfClub");
            socketWrapper.write(clubName);

            String response = (String) socketWrapper.read();
            playerListView.getItems().clear();

            if (response.equals("Players found")) {
                playerList = (ArrayList<Player>) socketWrapper.read();
                populatePlayerList(playerList);
            } else {
                playerListView.getItems().add("No players found with Maximum Age for club: " + clubName);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Error");
            alert.setHeaderText("Error connecting to the server");
            alert.setContentText("An error occurred while retrieving player data.");
            alert.showAndWait();
        }

    }

    @FXML
    private void handleMaxHeight(){
        String clubName = clubNameField.getText();

        if (clubName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Club Name is required");
            alert.setContentText("Please enter the club name to search for players.");
            alert.showAndWait();
            return;
        }

        try {
            socketWrapper.write("searchMaxHeightOfClub");
            socketWrapper.write(clubName);

            String response = (String) socketWrapper.read();
            playerListView.getItems().clear();

            if (response.equals("Players found")) {
                playerList = (ArrayList<Player>) socketWrapper.read();
                populatePlayerList(playerList);
            } else {
                playerListView.getItems().add("No players found with Maximum Height for club: " + clubName);

            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Error");
            alert.setHeaderText("Error connecting to the server");
            alert.setContentText("An error occurred while retrieving player data.");
            alert.showAndWait();
        }



    }

    @FXML
    private void handleTotalYearlySalary() throws IOException, ClassNotFoundException {
        showDetailsButton.setDisable(true);
        String clubName = clubNameField.getText().trim(); // Get the club name from the input field

        if (clubName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Club Name is required");
            alert.setContentText("Please enter the club name to search for players.");
            alert.showAndWait();
            return;
        }

        try {
            // Send the request to the server
            socketWrapper.write("searchTotalYearlySalaryOfClub");
            socketWrapper.write(clubName);

            // Read the total yearly salary response from the server
            Long totalYearlySalary = (Long) socketWrapper.read(); // Receive the long value from the server

            // Clear the ListView and add the result
            playerListView.getItems().clear();
            if (totalYearlySalary > 0) {
                playerListView.getItems().add(clubName + ": $" + totalYearlySalary);
            } else {
                playerListView.getItems().add("No players found for club: " + clubName);
            }

        } catch (IOException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Error");
            alert.setHeaderText("Error connecting to the server");
            alert.setContentText("An error occurred while retrieving total yearly salary for the club.");
            alert.showAndWait();
        }
    }



//    @FXML
//    private void handleTotalYearlySalary() throws IOException, ClassNotFoundException {
//        String clubName = clubNameField.getText().trim(); // Get the club name from the input field
//
//        if (clubName.isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Input Error");
//            alert.setHeaderText("Club Name is required");
//            alert.setContentText("Please enter the club name to search for players.");
//            alert.showAndWait();
//            return;
//        }
//
//        try {
//            // Send the request to the server
//            socketWrapper.write("searchTotalYearlySalaryOfClub");
//            socketWrapper.write(clubName);
//
//            // Read the total yearly salary response from the server
//            Long totalYearlySalary = (Long) socketWrapper.read(); // Receive the long value from the server
//
//            // Display the result
//            resultVBox.getChildren().clear();
//            if (totalYearlySalary > 0) {
//                Label resultLabel = new Label("Total Yearly Salary for " + clubName + ": $" + totalYearlySalary);
//                resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #000000;");
//                resultVBox.getChildren().add(resultLabel);
//            } else {
//                Label resultLabel = new Label("No players found for club: " + clubName);
//                resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: red;");
//                resultVBox.getChildren().add(resultLabel);
//            }
//
//        } catch (IOException | ClassNotFoundException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Server Error");
//            alert.setHeaderText("Error connecting to the server");
//            alert.setContentText("An error occurred while retrieving total yearly salary for the club.");
//            alert.showAndWait();
//        }
//    }

    @FXML
    private void back(javafx.event.ActionEvent event) throws IOException, ClassNotFoundException {

        MenuLoader menuLoader = new MenuLoader();
        menuLoader.loadMenuScreen(Currentclub, socketWrapper, stage,LoggedInClubName);

    }



}
