package game.snake.controls;

import game.snake.core.metaelements.Player;

import java.util.List;

public interface PlayerListener {
    public void catchPUpdate(List<Player> players);
}
