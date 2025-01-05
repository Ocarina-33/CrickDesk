package data;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import data.*;
import controllers.*;

import server.*;
import client.*;

public class PlayerDatabase {
    
    public static ArrayList<Player> PlayerList = new ArrayList<>();
    ArrayList<String> CountryName = new ArrayList<>();
    HashMap<String, Integer> CountryCount = new HashMap<>();
    HashMap<String,Club> ClubMap = new HashMap<>();
    public ArrayList<Club> clubs = new ArrayList<>();

    private static final String INPUT_FILE_NAME = "E:\\Cricket project\\source\\main\\java\\data\\players.txt";



    public Club addClub(String ClubName) {
        if(ClubMap.containsKey(ClubName)){
            return ClubMap.get(ClubName);
        }

        Club club = new Club(ClubName);
        ClubMap.put(ClubName, club);
        clubs.add(club);
        return club;
    }



    public void ReadFromFile() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));
        PlayerList.clear();
        
        CountryCount.clear();

        String line;

        while (true) {
            line = br.readLine();
            if (line == null) {
                break;
            }

            String[] info = line.split(",");
            int s = -1;
            int jersey = -1;

            if (info[6].isEmpty()) {
                s = 0;
            }

            if (s == 0) {
                jersey = -1;
            } else {
                jersey = Integer.parseInt(info[6]);
            }

            Player player = new Player(
                    info[0],
                    info[1],
                    Integer.parseInt(info[2]),
                    Double.parseDouble(info[3]),
                    info[4],
                    info[5],
                    jersey,
                    Long.parseLong(info[7]),
                    Boolean.parseBoolean(info[8])
            );

            Club club = addClub(info[4]);
            club.addPlayer(player);

            PlayerList.add(player);
            CountryName.add(player.getCountry());
            CountryCount.put(player.getCountry(), CountryCount.getOrDefault(player.getCountry(), 0) + 1);

        }
        br.close();

    }

    public boolean isPlayerExists(String name) {
        for (Player player : PlayerList) {
            if (player.getName().equalsIgnoreCase(name)) {
                return true;                       // Player with this name already exists
            }
        }
        return false;
    }

    public void write() throws Exception {

        BufferedWriter writer = new BufferedWriter(new FileWriter(INPUT_FILE_NAME));
        try {
            for(Player player : PlayerList){

                String playerData = String.join(",",
                        player.getName(),
                        player.getCountry(),
                        String.valueOf(player.getAge()),
                        String.valueOf(player.getHeight()),
                        player.getClub(),
                        player.getPosition(),
                        (player.getNumber() == -1) ? "" : String.valueOf(player.getNumber()),
                        String.valueOf(player.getWeekly_Salary()),
                        String.valueOf(player.getAvailable())
                );

                writer.write(playerData);
                writer.write(System.lineSeparator()); 
            }


        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();

        }
    }



    
// 1) By Player Name

Player SearchByName(String player_name){                    // return type done
            
    for(Player player : PlayerList){
        if(player.getName().equalsIgnoreCase(player_name)){
            return player;
        }    
}

return null;
        
}


// 2) By Club and Country

ArrayList<Player> Search_Country_And_Club(String country, String club ){              // return type done
       
    ArrayList<Player> CountryandClub = new ArrayList<>();

    for(Player pr : PlayerList){
        if(club.equalsIgnoreCase("any") && pr.getCountry().equalsIgnoreCase(country)){
            CountryandClub.add(pr);
        }

        else if(pr.getClub().equalsIgnoreCase(club) && pr.getCountry().equalsIgnoreCase(country)){
            CountryandClub.add(pr);
        }
    }
    
    return CountryandClub;

}


//3) By position

ArrayList<Player> SearchPosition(String pos){
    ArrayList<Player> posArr = new ArrayList<>();
    for(Player pr : PlayerList){
        if(pr.getPosition().equalsIgnoreCase(pos)){
            posArr.add(pr);
    }
    
}
return posArr;

}


//4) By Salary Range

ArrayList<Player> SalaryRange(long start, long end){
 ArrayList<Player> salaryRange = new ArrayList<>();
    for(Player player : PlayerList){
            long salary =player.getWeekly_Salary();
            if( salary >=start && salary<=end){
                salaryRange.add(player);
            }
    }
    return salaryRange;

}



    ArrayList<Player> MaximumSalary(String Club, PlayerDatabase pd) {
        ArrayList<Player> maxSal = new ArrayList<>();
        long mx = -1;

        for (Player player : pd.PlayerList) {
            if (player.getClub().equalsIgnoreCase(Club)) {

                if (player.getWeekly_Salary() > mx) {
                    maxSal.clear();
                    mx = player.getWeekly_Salary();
                    maxSal.add(player);
                }

                else if (player.getWeekly_Salary() == mx) {
                    maxSal.add(player);
                }

            }
        }

        return maxSal;
    }

    ArrayList<Player> MaximumAge(String Club, PlayerDatabase pd) {

        ArrayList<Player> maxAge = new ArrayList<>();
        int mx = -1;
        int f = 0;
        for (Player player : pd.PlayerList) {
            if (player.getClub().equalsIgnoreCase(Club)) {
                f=1;
                if (player.getAge() > mx) {
                    maxAge.clear();
                    mx = player.getAge();
                    maxAge.add(player);
                }

                else if (player.getAge() == mx) {
                    maxAge.add(player);
                }
            }

        }

        return maxAge;

    }

    ArrayList<Player> MaximumHeight(String Club, PlayerDatabase pd) {

        ArrayList<Player> maxHeight = new ArrayList<>();
        double mx = -1;
        for (Player player : pd.PlayerList) {
            if (player.getClub().equalsIgnoreCase(Club)) {

                if (player.getHeight() > mx) {
                    maxHeight.clear();
                    mx = player.getHeight();
                    maxHeight.add(player);
                }

                else if (player.getHeight() == mx) {
                    maxHeight.add(player);
                }
            }
        }

        return maxHeight;

    }

    long totalYearlySalary(String Club, PlayerDatabase pd) {

        long total=0;
        for (Player player : pd.PlayerList) {
            if (player.getClub().equalsIgnoreCase(Club)) {
                total+=player.getWeekly_Salary() * 52;
            }
        }

        return total;

    }

}





