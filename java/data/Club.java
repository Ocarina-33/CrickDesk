package data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Club implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;
    List<Player> players = new ArrayList();

    public Club(){

    }

    public Club(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Player> getPlayers() {
        if (this.players == null) {
            return new ArrayList<>();
        }
        return this.players;
    }
    public void setPlayers(List<Player> players) { this.players = players; }

    public void addPlayer(Player player) {
        players.add(player);
    }
    public void removePlayer(Player player) {
        players.remove(player);
    }


    public Player getPlayerByName(String playerName) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(playerName)) { // Compare player name
                return player;
            }
        }
        return null; // If no player with that name is found
    }

}


