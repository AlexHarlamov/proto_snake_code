package io.network;

import game.snake.core.GameSettings;
import io.IOAble;
import io.LocalUIController;
import io.network.sender.MessageSender;
import io.network.sender.ProtoController;

import javax.swing.*;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectionInitiator {

    private IOAble src;

    public void setSrc(IOAble src) {
        this.src = src;
    }

    private ProtoController protoController = new ProtoController();
    private GameSettings gameSettings;
    private Timer timer;

    private int state = 0;

    private int timerTickTime = 1000;

    private TimerTask timerTask;

    public void askConnectTo(InetAddress address,int port,GameSettings gameSettings){
        MessageSender.sendBytesToAddress(address,port,protoController.configureJoinMessage(gameSettings));
    }

    public void abortConnection(InetAddress address,int port){
        MessageSender.sendBytesToAddress(address,port,protoController.configureErrMsg("CORE: Game connection failed"));
    }

    public void acceptConnection(InetAddress address,int port){
        MessageSender.sendBytesToAddress(address,port,protoController.configureAckMsg());
    }

    public void connectTo(InetAddress address,int port){
        src.connectTo(address,port);
    }

    public void startAnnouncePlayers(ProtoController protoController,GameSettings gameSettings){
        this.protoController = protoController;
        this.gameSettings = gameSettings;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                MessageSender.sendMulticast(protoController.configureAnnouncementMessage(gameSettings));
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,timerTickTime);
        state = 1;
    }

    public void stopAnnouncePlayers(){
        if(state == 1){
            timer.cancel();
            timer.purge();
            state = 0;
        }
    }
}
