package game.snake.core.metaelements;

import game.snake.controls.PlayerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerController {
    private List<PlayerListener> listeners = new ArrayList<>();

    public List<Player> players = new CopyOnWriteArrayList<>();

    public void addPlayer(Player player){
        players.add(player);
        notifyListeners();
    }

    public List<Player> getPlayersList(){
        return players;
    }

    public void activatePlayers(){
        for (Player player:players
             ) {
            player.setStatus(PlayerStatus.ALIVE);
        }
        notifyListeners();
    }

    public void notifyListeners(){
        for (PlayerListener listener:listeners
             ) {
            listener.catchPUpdate(players);
        }
    }

    public void addListener(PlayerListener listener){
        listeners.add(listener);
        for (Player player:players) {

        }
    }

}
