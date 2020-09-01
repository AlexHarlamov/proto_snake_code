package io.network;

import game.snake.core.GameController;
import game.snake.core.metaelements.Player;
import game.snake.core.metaelements.PlayerStatus;
import io.network.sender.MessageSender;
import io.network.sender.ProtoController;

import java.net.InetAddress;

public class PlayerConnectionAccepter {

    GameController gameController;
    NetPlayerController netPlayerController;

    ProtoController protoController;

    public PlayerConnectionAccepter(GameController gameController, NetPlayerController netPlayerController, ProtoController protoController) {
        this.gameController = gameController;
        this.netPlayerController = netPlayerController;
        this.protoController = protoController;
    }

    public Player addViewer(String name, InetAddress address, int port){
        Player player = new Player();
        player.name = name;
        player.setStatus(PlayerStatus.VIEWER);
        netPlayerController.addPlayer(address.toString(),player,port);
        MessageSender.addPlayer(player,address,port);
        return player;
    }

    public Player addPlayer(String name, InetAddress address, int port){
        Player player = new Player();
        player.setStatus(PlayerStatus.ALIVE);
        player.name = name;
       if(gameController.addPlayer(player)){
           netPlayerController.addPlayer(address.toString(),player,port);
           MessageSender.addPlayer(player,address,port);
           return player;
       }else{
           return null;
       }
    }
}
