package controllers;

import data.Club;
import data.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.SocketWrapper;

import java.io.IOException;
import java.util.ArrayList;

public class Congratulations {
    private Player selectedPlayer;
    private SocketWrapper socketWrapper;
    private Scene scene;
    private String LoggedInClubName;

    private Club Currentclub = new Club();
    public void setClub(Club club,String clubName){
        this.Currentclub = club;
        this.LoggedInClubName=clubName;
    }
    public void setSocketWrapper(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ArrayList<Player> players;
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }


    public void back(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MarketPlace.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        socketWrapper.write("searchPlayerForMarketplace");
        socketWrapper.write(Currentclub.getName());
        ArrayList<Player> players = (ArrayList<Player>) socketWrapper.read();

        MarketPlaceController pc = loader.getController();
        pc.setSocketWrapper(socketWrapper);
        pc.setStage(stage);
        pc.setClub(Currentclub,LoggedInClubName);
        pc.setPlayers(players);
        stage.setScene(scene);
        stage.setTitle("Market Place");
        stage.show();
    }
}

