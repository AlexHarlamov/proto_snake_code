package io;

import game.snake.core.action.PlayerAction;
import io.network.sender.MessageSender;
import io.network.sender.ProtoController;

public class NetworkActionAdaptor implements MoveHandler {

    private MessageSender.AddressPair host;

    public void setHost(MessageSender.AddressPair host) {
        this.host = host;
    }

    private ProtoController protoController = new ProtoController();

    public ProtoController getProtoController() {
        return protoController;
    }

    @Override
    public void moveUP() {
        MessageSender.sendBytesToHost(host,protoController.configureMoveMessage(PlayerAction.moveUP));
        System.out.println("NET: move up message sent");
    }

    @Override
    public void moveDown() {
        MessageSender.sendBytesToHost(host,protoController.configureMoveMessage(PlayerAction.moveDown));
        System.out.println("NET: move down message sent");
    }

    @Override
    public void moveLeft() {
        MessageSender.sendBytesToHost(host,protoController.configureMoveMessage(PlayerAction.moveLeft));
        System.out.println("NET: move left message sent");
    }

    @Override
    public void moveRight() {
        MessageSender.sendBytesToHost(host,protoController.configureMoveMessage(PlayerAction.moveRight));
        System.out.println("NET: move right message sent");
    }
}
