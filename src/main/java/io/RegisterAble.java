package io;

import game.snake.core.GameSettings;
import io.network.NetGamesListController;

public interface RegisterAble {
    public void runGame();
    public void exitGame();
    public void callSettings();
    public void showGames();
    public void addBot();
}
