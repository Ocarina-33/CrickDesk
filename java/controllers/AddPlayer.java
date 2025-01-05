package controllers;

import data.Club;
import data.*;
import data.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.SocketWrapper;

import java.io.IOException;
import java.util.ArrayList;
public class AddPlayer {

    private SocketWrapper socketWrapper;

    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    private String LoggedInClubName;
    private Club Currentclub = new Club();

    public void setClub(Club club,String clubName){
        this.Currentclub = club;
        this.LoggedInClubName=clubName;
    }

    private Stage stage;
    public void SetStage(Stage stage){
        this.stage=stage;
    }

    @FXML
    private Button backButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField positionField;
    @FXML
    private TextField jerseynumberField;
    @FXML
    private TextField weeklySalaryField;


    @FXML
    private void handleAddPlayer() throws IOException, ClassNotFoundException {
        String name = (String) nameField.getText();
        String country = (String) countryField.getText();
        String age = (String) ageField.getText();
        String height = (String) heightField.getText();
        String clubName = LoggedInClubName;
        // System.out.println(clubName);
        String position = (String) positionField.getText();
        String jerseynumber = (String) jerseynumberField.getText();
        String weeklySalary = (String) weeklySalaryField.getText();
        if(name.isEmpty() || country.isEmpty() || age.isEmpty() || height.isEmpty() || position.isEmpty() || jerseynumber.isEmpty() || weeklySalary.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("All fields are required");
            alert.setContentText("Please enter all the fields to add a player.");
            alert.showAndWait();
            return;
        }
        try {
            socketWrapper.write("addPlayer");
            socketWrapper.write(name);
            socketWrapper.write(country);
            socketWrapper.write(age);
            socketWrapper.write(height);
            socketWrapper.write(clubName);
            socketWrapper.write(position);
            socketWrapper.write(jerseynumber);
            socketWrapper.write(weeklySalary);
            socketWrapper.write(Currentclub);
            String response = (String) socketWrapper.read();
            System.out.println(response);
            if(response.equals("Player already exists")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Player With This Name Already Exists");
                alert.setHeaderText("Player With This Name Already Exists");
                alert.setContentText("Player With This Name Already Exists");
                alert.showAndWait();
                return;
            }
            if(response.equals("Player added successfully")){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Player Added Successfully");
                alert.setHeaderText("Player Added Successfully");
                alert.setContentText("Player Added Successfully");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Player Not Added");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void back(javafx.event.ActionEvent event) throws IOException, ClassNotFoundException {

        MenuLoader menuLoader = new MenuLoader();
        menuLoader.loadMenuScreen(Currentclub, socketWrapper, stage,LoggedInClubName);

    }

}

