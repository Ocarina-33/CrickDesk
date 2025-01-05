package server;
import data.*;
import controllers.*;

import server.*;
import client.*;
import util.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ClientService implements Runnable {
    private static final int PORT = 12345;
    private static final String PASSWORD_FILE = "E:\\Cricket project\\source\\main\\java\\data\\passwords.txt";
    private Map<String, String> credentials = Collections.synchronizedMap(new HashMap<>());
    private static final String INPUT_FILE_NAME = "E:\\Cricket project\\source\\main\\java\\data\\players.txt";

    private static PlayerDatabase players;
    SocketWrapper socketWrapper;


    public ClientService(PlayerDatabase players, Map<String, String> credentials, SocketWrapper socketWrapper) throws IOException {
        ClientService.players = players;
        this.credentials = credentials;
        this.socketWrapper = socketWrapper;
    }

    @Override
    public void run() {
        try {
            // Continuously handle client requests
            while (true) {
                // Read the command from the client (either "login", "search", etc.)
                String command = (String) socketWrapper.read();

                // Switch based on the command
                switch (command) {
                    case "login":
                        handleLogin();  // Call the login handler method
                        break;
                    case "UpdatePlayerList":
                          updatePlayerList();
                          break;
                    case "searchByName":
                        handleSearch();  // Call the search handler method
                        break;
                    case "searchPlayerByClub&Country":
                        handlePlayerByClubAndCountry();  // Call the search handler method
                        break;
                    case "searchPlayerByPosition":
                        handlePlayerByPosition();  // Call the search handler method
                        break;
                    case "searchPlayerBySalaryRange":
                        handlePlayerBySalaryRange();  // Call the search handler method
                        break;
                    case "searchCountryCount":
                        handleCountryCount();
                        break;
                    case "searchMaxSalaryOfClub":
                        handleSearchMaxSalaryOfClub();
                        break;
                    case "searchMaxAgeOfClub":
                        handleSearchMaxAgeOfClub();
                        break;
                    case "searchMaxHeightOfClub":
                        handleSearchMaxHeightOfClub();
                        break;
                    case "searchTotalYearlySalaryOfClub":
                        handleTotalYearlySalary();
                        break;
                    case "addPlayer":
                        handleAddPlayer();
                        break;
                    case "searchPlayerForMarketplace":
                         handleMarketPlace();
                         break;
                     case "BuyPlayer":
                         handleBuyPlayer();
                         break;
                     case "sellPlayer":
                         handleSellPlayer();
                         break;
                    case "SignUp":
                        handleSignUp();
                        break;
                    default:
                        socketWrapper.write("Invalid command.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle the login logic
    private void handleLogin() throws IOException, ClassNotFoundException {
        // Read login details from the client
        String name = (String) socketWrapper.read();
        String password = (String) socketWrapper.read();

        String response = "Unsuccessful, try again.";
        Club loggedInClub = null;

        // Check if credentials match
        if (credentials.containsKey(name) && credentials.get(name).equals(password)) {
            response = "Successful login";

            // Find the corresponding club for this user
            for (Club c : players.clubs) {
                if (c.getName().equalsIgnoreCase(name)) {
                    loggedInClub = c;
                    break;
                }
            }

            // Send the response and the club object to the client
            socketWrapper.write(response);
            socketWrapper.write(loggedInClub);  // Send the club object with players' data
        } else {
            socketWrapper.write(response);
        }
    }

    private void updatePlayerList() throws IOException, ClassNotFoundException {
        String clubName = (String) socketWrapper.read();
        ArrayList<Player> matchingPlayers = new ArrayList<>();
        for (Player player : PlayerDatabase.PlayerList) {
            if (player.getClub().equalsIgnoreCase(clubName)) {
                matchingPlayers.add(player);
            }
        }
        socketWrapper.write(matchingPlayers);
    }

    // Handle the search for player by name logic
    private void handleSearch() throws IOException, ClassNotFoundException {
        // Read the player name to search for
        String playerName = (String) socketWrapper.read();

        // Find the player by name in the players database
        ArrayList<Player> matchingPlayers = new ArrayList<>();

            for (Player player : PlayerDatabase.PlayerList) {
                if (player.getName().equalsIgnoreCase(playerName)) {
                    matchingPlayers.add(player);
                    break;
                }
            }

        if (!matchingPlayers.isEmpty()) {
            socketWrapper.write("Player found");
            socketWrapper.write(matchingPlayers);  // Send player details to the client
        } else {
            socketWrapper.write("Player not found");
        }
    }
    private void  handlePlayerByClubAndCountry() throws IOException, ClassNotFoundException {
        // Read club name and country name from the client
        String clubName = (String) socketWrapper.read();
        String countryName = (String) socketWrapper.read();

        // List to store all matching players
        ArrayList<Player> matchingPlayers = new ArrayList<>();


            if(clubName.isEmpty() && !countryName.isEmpty()){
                for(Player player : PlayerDatabase.PlayerList){
                    if(player.getCountry().equalsIgnoreCase(countryName)){
                        matchingPlayers.add(player);
                    }
                }
            }
            else if(!clubName.isEmpty() && countryName.isEmpty()){
                for(Player player : PlayerDatabase.PlayerList){
                    if(player.getClub().equalsIgnoreCase(clubName)){
                        matchingPlayers.add(player);
                    }
                }
            }
            else if(!clubName.isEmpty() && !countryName.isEmpty()){
                for(Player player : PlayerDatabase.PlayerList){
                    if(player.getClub().equalsIgnoreCase(clubName) && player.getCountry().equalsIgnoreCase(countryName)){
                        matchingPlayers.add(player);
                    }
                }
            }
            if (!matchingPlayers.isEmpty()) {
                socketWrapper.write("Players found");
                socketWrapper.write(matchingPlayers);  // Send the list of matching players to the client
            } else {
                socketWrapper.write("No players found");
            }

        }

    private void handlePlayerByPosition() throws IOException, ClassNotFoundException {
        // Read the position name to search for
        String positionName = (String) socketWrapper.read();

        // Find all players with the given position name
        ArrayList<Player> matchingPlayers = new ArrayList<>();

        for (Player player : PlayerDatabase.PlayerList) {
            if (player.getPosition().equalsIgnoreCase(positionName)) {
                matchingPlayers.add(player);
            }
        }

        if (!matchingPlayers.isEmpty()) {
            socketWrapper.write("Players found");
            socketWrapper.write(matchingPlayers);  // Send the list of matching players to the client
        } else {
            socketWrapper.write("No players found");
        }
    }

    private void handlePlayerBySalaryRange() throws IOException, ClassNotFoundException {
        String minSalary = (String) socketWrapper.read();
        String maxSalary = (String) socketWrapper.read();
        ArrayList<Player> matchingPlayers = new ArrayList<>();
        for (Player player : PlayerDatabase.PlayerList) {
            if (player.getWeekly_Salary() >= Long.parseLong(minSalary) && player.getWeekly_Salary() <= Long.parseLong(maxSalary)) {
                matchingPlayers.add(player);
            }
        }
        if (!matchingPlayers.isEmpty()) {
            socketWrapper.write("Players found");
            socketWrapper.write(matchingPlayers);
        }
        else {
            socketWrapper.write("No players found");
        }
    }


    private void handleCountryCount() throws IOException, ClassNotFoundException {
        HashMap<String, Integer> countryCount = new HashMap<>();

        for (Player player : PlayerDatabase.PlayerList) {
            countryCount.put(player.getCountry(), countryCount.getOrDefault(player.getCountry(), 0) + 1);
        }

        String[] countryNames = new String[countryCount.size()];
        Integer[] countryCounts = new Integer[countryCount.size()];
        {
            int i = 0;
            for (String country : countryCount.keySet()) {
            countryNames[i] = country;
            countryCounts[i] = countryCount.get(country);
            i++;
            }
        }

//        StringBuilder result = new StringBuilder();
//        result.append("Country Counts:\n\n");
//        for (int i = 0; i < countryNames.length; i++) {
//            result.append(countryNames[i])
//                    .append("  :         ")
//                    .append(countryCounts[i])
//                    .append("\n");
//        }
//        socketWrapper.write(result.toString());

        StringBuilder result = new StringBuilder();
        result.append("Country Counts:\n\n");

        // Calculate the maximum length of the country names
        int maxCountryLength = countryCount.keySet().stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        // Format and append each entry with proper alignment
        for (Map.Entry<String, Integer> entry : countryCount.entrySet()) {
            String country = entry.getKey();
            int count = entry.getValue();

            // Adjust the formatting to make sure the columns align properly
            result.append(String.format("%-" + (maxCountryLength + 5) + "s : %d\n", country, count));
        }

        // Send the formatted result
        socketWrapper.write(result.toString());

    }

    private void handleSearchMaxSalaryOfClub() throws IOException,ClassNotFoundException{
        String clubName = (String) socketWrapper.read();

        ArrayList<Player> matchingPlayers = new ArrayList<>();
        long mx = -1;

        for (Player player : PlayerDatabase.PlayerList) {
            if (player.getClub().equalsIgnoreCase(clubName)) {

                if (player.getWeekly_Salary() > mx) {
                    matchingPlayers.clear();
                    mx = player.getWeekly_Salary();
                    matchingPlayers.add(player);
                }

                else if (player.getWeekly_Salary() == mx) {
                    matchingPlayers.add(player);
                }

            }
        }
        if (!matchingPlayers.isEmpty()) {
            socketWrapper.write("Players found");
            socketWrapper.write(matchingPlayers);  // Send the list of matching players to the client
        } else {
            socketWrapper.write("No players found");
        }

    }

    private void handleSearchMaxAgeOfClub() throws IOException,ClassNotFoundException{
        String clubName = (String) socketWrapper.read();
        ArrayList<Player> matchingPlayers = new ArrayList<>();

        int mx = -1;
        for (Player player : PlayerDatabase.PlayerList) {
            if (player.getClub().equalsIgnoreCase(clubName)) {
                if (player.getAge() > mx) {
                    matchingPlayers.clear();
                    mx = player.getAge();
                    matchingPlayers.add(player);
                }

                else if (player.getAge() == mx) {
                    matchingPlayers.add(player);
                }
            }

        }

        if (!matchingPlayers.isEmpty()) {
            socketWrapper.write("Players found");
            socketWrapper.write(matchingPlayers);  // Send the list of matching players to the client
        } else {
            socketWrapper.write("No players found");
        }



    }

    private void handleSearchMaxHeightOfClub() throws IOException,ClassNotFoundException{
        String clubName = (String) socketWrapper.read();
        ArrayList<Player> matchingPlayers = new ArrayList<>();

        double mx = -1;
        for (Player player : PlayerDatabase.PlayerList) {
            if (player.getClub().equalsIgnoreCase(clubName)) {
                if (player.getHeight() > mx) {
                    matchingPlayers.clear();
                    mx = player.getHeight();
                    matchingPlayers.add(player);
                }
                else if (player.getHeight() == mx) {
                    matchingPlayers.add(player);
                }
            }

        }

        if (!matchingPlayers.isEmpty()) {
            socketWrapper.write("Players found");
            socketWrapper.write(matchingPlayers);  // Send the list of matching players to the client
        } else {
            socketWrapper.write("No players found");
        }
    }


    private void handleTotalYearlySalary() throws IOException,ClassNotFoundException{
        String clubName = (String) socketWrapper.read();
        long totalYearlySalary = 0;
        for (Player player : PlayerDatabase.PlayerList) {
            if (player.getClub().equalsIgnoreCase(clubName)) {
                totalYearlySalary += player.getWeekly_Salary() * 52;
            }
        }
        socketWrapper.write(totalYearlySalary);
    }

    private void handleAddPlayer() throws IOException,ClassNotFoundException{
        String name = (String) socketWrapper.read();
        String country = (String) socketWrapper.read();
        String a = (String) socketWrapper.read();
        int age = Integer.parseInt(a);
        String h = (String) socketWrapper.read();
        double height = Double.parseDouble(h);
        String club = (String) socketWrapper.read();
        String position = (String) socketWrapper.read();
        String n = (String) socketWrapper.read();
        int number = Integer.parseInt(n);
        String w = (String) socketWrapper.read();
        long weeklySalary = Long.parseLong(w);
        Club CurrentClub=(Club) socketWrapper.read();

        Player pr =new Player(name,country,age,height,club,position,number,weeklySalary,false);
        for(Player player : PlayerDatabase.PlayerList){
            if(player.getName().equalsIgnoreCase(name)){
                socketWrapper.write("Player already exists");
                return;
            }
        }
        PlayerDatabase.PlayerList.add(pr);
        players.addClub(CurrentClub.getName());


        socketWrapper.write("Player added successfully");

        try {
            players.write();
            CurrentClub.addPlayer(pr);
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void handleMarketPlace() throws IOException,ClassNotFoundException{
        String CurrentClubName = (String) socketWrapper.read();
        ArrayList<Player> matchingPlayers = new ArrayList<>();

        for(Player player : PlayerDatabase.PlayerList){
            if((!player.getClub().equalsIgnoreCase(CurrentClubName)) && player.getAvailable()){
                matchingPlayers.add(player);
            }
        }
//        for(Player player : PlayerDatabase.PlayerList){
//            System.out.println(player.getName() + "  " + player.getAvailable());
//        }
        socketWrapper.write(matchingPlayers);
    }

    private void handleBuyPlayer() throws Exception {
        String CurrentClubName = (String) socketWrapper.read();
        Player selectedPlayer = (Player) socketWrapper.read();
        Player storedPlayer = null;
        for(Player player : PlayerDatabase.PlayerList){
            if(selectedPlayer.getName().equalsIgnoreCase(player.getName())){
                storedPlayer = player;
                PlayerDatabase.PlayerList.remove(player);
                break;
            }
        }
        storedPlayer.setClub(CurrentClubName);
        storedPlayer.setAvailable(false);
        PlayerDatabase.PlayerList.add(storedPlayer);


    }

    private void handleSellPlayer() throws Exception {
        String selectedPlayer = (String) socketWrapper.read();
        for(Player player : PlayerDatabase.PlayerList){
            if(selectedPlayer.equalsIgnoreCase(player.getName())){
                player.setAvailable(true);
                System.out.println(player);
                break;
            }
        }

    }

    private void handleSignUp() throws IOException,ClassNotFoundException{
        String clubName = (String) socketWrapper.read();
        String password = (String) socketWrapper.read();
        String confirmPassword = (String) socketWrapper.read();

        if (credentials.containsKey(clubName)) {
            socketWrapper.write("Club already exists");
            return;
        }


        if(!password.equals(confirmPassword)){
            socketWrapper.write("Password does not match");
            return;
        }
        credentials.put(clubName, password);
        players.addClub(clubName);
        saveCredentialsToFile(clubName, password);
        socketWrapper.write("SignUp successful");



    }

    private void saveCredentialsToFile(String clubName, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PASSWORD_FILE, true))) {
            writer.write(clubName + "," + password);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to save credentials to file: " + e.getMessage());
        }
    }

}







