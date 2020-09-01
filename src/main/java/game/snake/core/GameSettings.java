package game.snake.core;

public class GameSettings {

    public int foodPerPlayer = 1;
    public int constFood = 1;

    public boolean isOnlyView = false;

    public String name ;

    public int sizeX;
    public int sizeY;

    public int port = 9000;

    public GameSettings(int sizeX, int sizeY, String name, int port) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.name = name;
        this.port = port;
    }

    public void setSettings(int sizeX, int sizeY, String name, int port){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.name = name;
        this.port = port;
    }

    public int maxPlayersAvailable (){
        return 1;
    }
}
