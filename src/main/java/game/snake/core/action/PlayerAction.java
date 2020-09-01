package game.snake.core.action;

public enum PlayerAction{
    moveUP(1),
    moveDown(2),
    moveLeft(3),
    moveRight(4);
    private final int value;
    private PlayerAction(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
