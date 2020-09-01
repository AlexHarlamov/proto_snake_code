package io;

import game.snake.core.metaelements.Player;
import game.snake.core.metaelements.PlayerController;

import java.util.List;

public class AbstractPlayersController extends PlayerController {

    public void refresh(){
        players.clear();
    }

}
