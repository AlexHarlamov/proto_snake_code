package game.snake.core.stateworker;

import game.snake.core.metaelements.Player;

public class NewStatePair {
    public Player player;
    public PlayerUpdate update;

    public Player coinFrom;

    public NewStatePair(Player player, PlayerUpdate update) {
        this.player = player;
        this.update = update;
    }

    public NewStatePair addCoinFrom(Player coinFrom){
        this.coinFrom = coinFrom;
        return this;
    }
}
