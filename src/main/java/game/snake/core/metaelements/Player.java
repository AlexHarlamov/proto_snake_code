package game.snake.core.metaelements;

import game.snake.core.elements.Snake;

public class Player {

    public int id;

    public String name;
    public Snake snake;

    public int score;

    public int kills;

    private PlayerStatus status;

    public void addKill(){
        kills++;
    }

    public void addCoin(){
        score++;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public PlayerStatus getStatus(){
        return status;
    }

    public boolean ISPLAYERIDINITIALEZEDBYACTIONCONTROLLER;
}
