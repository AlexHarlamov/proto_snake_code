package io.network.reciever;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import game.snake.network.SnakesProto;
import io.network.AbstractGameSettings;
import io.network.NetGamesListController;
import io.network.sender.MessageSender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class InputMulticastReceiver implements Runnable{

    NetGamesListController netGamesListController;

    public void setNetGamesListController(NetGamesListController netGamesListController) {
        this.netGamesListController = netGamesListController;
    }

    private byte[] buf = new byte[3072];
    @Override
    public void run() {

        MulticastSocket multicastSocket = MessageSender.getMulticastSocket();
        DatagramPacket packet = new DatagramPacket(buf,buf.length);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                multicastSocket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                SnakesProto.GameMessage gameMessage = SnakesProto.GameMessage.parseFrom(ByteString.copyFrom(packet.getData(), packet.getOffset(), packet.getLength()));
                SnakesProto.GameMessage.AnnouncementMsg announcementMsg = gameMessage.getAnnouncement();

                InetAddress hostAddress = null;
                int hostPort = 0;
                for (SnakesProto.GamePlayer player:announcementMsg.getPlayers().getPlayersList()
                     ) {
                    if(player.getRole() == SnakesProto.NodeRole.MASTER){
                        try {
                            hostAddress = InetAddress.getByName(packet.getAddress().toString().substring(1));
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        hostPort = player.getPort();
                    }
                }
                netGamesListController.update(new AbstractGameSettings(
                        announcementMsg.getConfig().getWidth(),
                        announcementMsg.getConfig().getHeight(),
                        null,
                        hostAddress,
                        hostPort,
                        announcementMsg.getPlayers().getPlayersCount()
                ));
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }

        }
    }
}
