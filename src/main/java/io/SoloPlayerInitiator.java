package io;

import game.snake.core.GameSettings;
import game.snake.core.metaelements.Player;


public interface SoloPlayerInitiator {
    public void initiateSoloPlayer(GameSettings settings, IASaveAble hostHolder);
    public Player getPlayer();
}
