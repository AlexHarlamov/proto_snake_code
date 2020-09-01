package io.network;

import game.snake.core.GameSettings;
import game.snake.core.metaelements.PlayerStatus;
import io.IASaveAble;
import io.SoloPlayerInitiator;
import game.snake.core.metaelements.Player;
import io.network.sender.MessageSender;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class soloConstructor implements SoloPlayerInitiator {

    Player player;

    @Override
    public void initiateSoloPlayer(GameSettings settings, IASaveAble hostHolder) {
        player = new Player();
        player.name = settings.name;
        player.setStatus(PlayerStatus.ALIVE);
        try {
            MessageSender.addPlayer(player,InetAddress.getByName("127.0.0.1"),settings.port);
            hostHolder.saveIAPair(new MessageSender.AddressPair(InetAddress.getByName("127.0.0.1"),settings.port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
