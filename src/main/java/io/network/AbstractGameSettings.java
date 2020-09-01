package io.network;

import game.snake.core.GameSettings;
import game.snake.core.metaelements.Player;

import java.net.InetAddress;
import java.util.List;

public class AbstractGameSettings {
    public int sizeX;
    public int sizeY;
    List<Player> players;
    public InetAddress address;
    public int port;
    int players_number;

    public AbstractGameSettings(int sizeX, int sizeY, List<Player> players, InetAddress address, int port, int players_number) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.players = players;
        this.address = address;
        this.port = port;
        this.players_number = players_number;
    }

    public boolean compare(AbstractGameSettings another){
        return this.address.equals(another.address) && this.port == another.port;
    }

    public GameSettings convertToGameSettings(String name, int port){
        return new GameSettings(sizeX,sizeY,name, port);
    }
}
