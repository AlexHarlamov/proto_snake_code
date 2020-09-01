package game.snake.controls;

import game.snake.core.action.PlayerAction;
import game.snake.core.metaelements.Player;

public interface MessageController {
    byte[] configureJoinMessage(Player player);
    byte[] configureMoveMessage(PlayerAction action);
}
