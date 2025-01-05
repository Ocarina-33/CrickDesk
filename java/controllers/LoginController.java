package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.*;
import data.*;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

public class LoginController {
    public Text label;
    public Text label1;
    private SocketWrapper socketWrapper;
    private Stage stage;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button SignUpButton;

    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    @FXML
    public void handleLogin(javafx.event.ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            if (!username.isEmpty() && !password.isEmpty()) {
                // Send login command
                socketWrapper.write("login");  // Command to indicate it's a login request
                socketWrapper.write(username);  // Send username
                socketWrapper.write(password);  // Send password

                // Read the response from the server
                String response = (String) socketWrapper.read();

                if (response.equals("Successful login")) {
                    // Read the Club object from the server after successful login
                    Club loggedInClub = (Club) socketWrapper.read();

                    // After successful login, load the menu screen
                    stage = (Stage) loginButton.getScene().getWindow();
                    MenuLoader menuLoader = new MenuLoader();
                    menuLoader.loadMenuScreen(loggedInClub, socketWrapper,stage,username);
                } else {
                    // Show an error alert if login fails
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Login Error");
                    alert.setHeaderText("Invalid login credentials");
                    alert.setContentText("Please check your username and password.");
                    alert.showAndWait();
                }
            } else {
                // Alert if username or password is empty
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Input Error");
                alert.setHeaderText("Username or Password Missing");
                alert.setContentText("Please enter both username and password.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Show error message if something goes wrong with the connection
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("An error occurred while communicating with the server.");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }

    @FXML
    private void SignUp(javafx.event.ActionEvent event) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SignUp.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);

        SignUpController pc = loader.getController();
        stage = (Stage) loginButton.getScene().getWindow();

        pc.setSocketWrapper(socketWrapper);
        pc.setStage(stage);
        stage.setScene(scene);


        stage.setTitle("Sign Up!");
        stage.show();

    }


}
