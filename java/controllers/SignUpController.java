package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import util.*;
import data.*;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class SignUpController {
    private Stage stage;
    public void setStage(Stage stage) {
        this.stage=stage;
    }
    private SocketWrapper socketWrapper;
    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Button DoneButton;
    @FXML
    private Button backButton;

    @FXML
    private void Done() throws IOException, ClassNotFoundException {
        if (username.getText().isEmpty() || password.getText().isEmpty() || confirmPassword.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Missing Fields");
            alert.setContentText("Please fill in all the fields.");
            alert.showAndWait();
            return;
        }
        socketWrapper.write("SignUp");
        socketWrapper.write(username.getText());
        socketWrapper.write(password.getText());
        socketWrapper.write(confirmPassword.getText());

        String response = (String) socketWrapper.read();

        if(response.equals("Club already exists")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Club Already Exists!");
            alert.setContentText("Please Enter a Valid New Club Name!");
            alert.showAndWait();
            return;

        }

        if (response.equals("Password does not match")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Incorrect Password!");
            alert.setContentText("Please Enter the correct Password!.");
            alert.showAndWait();
            return;
        }

        if(response.equals("SignUp successful")) {
            Club newClub = new Club(username.getText());
            MenuLoader menuLoader = new MenuLoader();
            menuLoader.loadMenuScreen(newClub, socketWrapper,stage, username.getText());
        }

    }

    @FXML
    private void back() throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);

        LoginController controller = loader.getController();
        controller.setSocketWrapper(socketWrapper);

        stage.setScene(scene);
        stage.show();

    }

}
