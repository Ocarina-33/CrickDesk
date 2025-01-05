package controllers;
import data.Club;
import data.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;


public class DetailsController {
    @FXML private Label nameLabel;
    @FXML private Label countryLabel;
    @FXML private Label clubLabel;
    @FXML private Label ageLabel;
    @FXML private Label heightLabel;
    @FXML private Label positionLabel;
    @FXML private Label jerseyLabel;
    @FXML private Label salaryLabel;
    @FXML private ImageView playerImage;
    @FXML private ImageView clubImage;


    public void setPlayer(Player selectedPlayer) {
        String jerseyNumber = selectedPlayer.getNumber()==-1?"N/A ":(String.valueOf(selectedPlayer.getNumber()));
        nameLabel.setText("Name : "+selectedPlayer.getName());
        countryLabel.setText("Country : "+selectedPlayer.getCountry());
        clubLabel.setText("Club : "+selectedPlayer.getClub());
        ageLabel.setText("Age : "+selectedPlayer.getAge());
        heightLabel.setText("Height : "+selectedPlayer.getHeight());
        positionLabel.setText("Position : "+selectedPlayer.getPosition());
        jerseyLabel.setText("Jersey Number : "+jerseyNumber);
        salaryLabel.setText("Weekly Salary : "+selectedPlayer.getWeekly_Salary());

        String playerImagePath = "playerImages/"+selectedPlayer.getName().replace(" ","")+".png";
        Image playerImageSrc = loadImage(playerImagePath,"playerImages");
        playerImage.setImage(playerImageSrc);


        String clubImagePath = "clubImages/"+selectedPlayer.getClub().replace(" ","")+".png";
        Image clubImageSrc = loadImage(clubImagePath,"clubImages");

        clubImage.setImage(clubImageSrc);



    }

    private Image loadImage(String imagePath, String directory) {
        try{
            Image image = new Image(getClass().getClassLoader().getResourceAsStream(imagePath));
            if(image.isError()){
                return new Image(getClass().getClassLoader().getResourceAsStream(directory+"/placeholder.png"));
            }
            return image;
        }
        catch (Exception e){
            return new Image(getClass().getClassLoader().getResourceAsStream(directory+"/placeholder.png"));
        }
    }
}
