package io.network.reciever;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import game.snake.core.GameController;
import game.snake.core.GameSettings;
import game.snake.core.action.PlayerAction;
import game.snake.core.metaelements.Player;
import game.snake.core.metaelements.PlayerStatus;
import game.snake.network.SnakesProto;
import io.AbstractField;
import io.AbstractPlayersController;
import io.fieldadaptorelements.FieldCoord;
import io.network.ConnectionInitiator;
import io.network.NetPlayerController;
import io.network.PlayerConnectionAccepter;
import io.network.sender.MessageSender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class InputMassageHandler implements Runnable{

    private byte[] buf = new byte[3072];

    GameSettings settings;

    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }

    PlayerConnectionAccepter playerConnectionAccepter;

    public void setPlayerConnectionAccepter(PlayerConnectionAccepter playerConnectionAccepter) {
        this.playerConnectionAccepter = playerConnectionAccepter;
    }

    GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    NetPlayerController playerController;

    public void setPlayerController(NetPlayerController playerController) {
        this.playerController = playerController;
    }

    ConnectionInitiator connectionInitiator;

    public void setConnectionInitiator(ConnectionInitiator connectionInitiator) {
        this.connectionInitiator = connectionInitiator;
    }

    AbstractField abstractField;

    public void setAbstractField(AbstractField abstractField) {
        this.abstractField = abstractField;
    }

    AbstractPlayersController playersController;

    public void setPlayersController(AbstractPlayersController playersController) {
        this.playersController = playersController;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            String notification = "";
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                MessageSender.socket.receive(packet);
                notification = "NET: message received : ";
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                SnakesProto.GameMessage gameMessage = SnakesProto.GameMessage.parseFrom(ByteString.copyFrom(packet.getData(), packet.getOffset(), packet.getLength()));
                Player player = null;
                if(playerController.getPlayerByAddressAndPort(packet.getAddress().toString(),packet.getPort()) != null){
                    player = playerController.getPlayerByAddressAndPort(packet.getAddress().toString(),packet.getPort());
                }
                switch (gameMessage.getTypeCase()) {
                    case STEER:
                        notification += "move (STEER)";
                        SnakesProto.Direction steer = gameMessage.getSteer().getDirection();
                        PlayerAction action = null;
                        if(gameController != null){
                            switch (steer){
                                case UP:
                                    action = PlayerAction.moveUP;
                                    break;
                                case DOWN:
                                    action = PlayerAction.moveDown;
                                    break;
                                case LEFT:
                                    action = PlayerAction.moveLeft;
                                    break;
                                case RIGHT:
                                    action = PlayerAction.moveRight;
                                    break;
                            }
                            gameController.getActionController().addActionInQueue(player,action);
                        }
                        break;
                    case JOIN:
                        notification += "join (JOIN)";
                        Player result = null;
                        if(gameMessage.getJoin().hasOnlyView())
                            if (gameMessage.getJoin().getOnlyView()) {
                                playerConnectionAccepter.addViewer(gameMessage.getJoin().getName(),packet.getAddress(),packet.getPort());
                            }else{
                                result = playerConnectionAccepter.addPlayer(gameMessage.getJoin().getName(),packet.getAddress(),packet.getPort());
                            }
                        else
                            result = playerConnectionAccepter.addPlayer(gameMessage.getJoin().getName(),packet.getAddress(),packet.getPort());

                        if(result == null){
                            connectionInitiator.abortConnection(packet.getAddress(),packet.getPort());
                        }else {
                            connectionInitiator.acceptConnection(packet.getAddress(),packet.getPort());
                            MessageSender.addPlayer(result,packet.getAddress(),packet.getPort());
                        }
                        break;
                    case ACK:
                        notification += "success connection (ACK)";
                        connectionInitiator.connectTo(packet.getAddress(),packet.getPort());
                        break;
                    case ERROR:
                        notification += gameMessage.getError().getErrorMessage()+" (ERROR)";
                        break;
                    case STATE:
                        notification += "field state (STATE)";
                        if(abstractField != null){

                            for (SnakesProto.GamePlayer aPlayer:gameMessage.getState().getState().getPlayers().getPlayersList()
                                 ) {
                                PlayerStatus status;
                                switch (aPlayer.getRole()){
                                    case VIEWER:
                                        status = PlayerStatus.VIEWER;
                                        break;
                                    default:
                                        status = PlayerStatus.ALIVE;
                                }
                                Player aIPlayer = new Player();
                                aIPlayer.name = aPlayer.getName();
                                aIPlayer.setStatus(status);
                                playersController.addPlayer(aIPlayer);
                            }

                            List<FieldCoord> coords = new ArrayList<>();
                            for (SnakesProto.GameState.Coord coord:gameMessage.getState().getState().getFoodsList()
                                 ) {
                                coords.add(new FieldCoord(coord.getX(),coord.getY()));
                            }

                            List<List<FieldCoord>> snakes = new ArrayList<>();
                            for (SnakesProto.GameState.Snake aSnake:gameMessage.getState().getState().getSnakesList()
                                 ) {
                                List<FieldCoord> snake = new ArrayList<>();
                                for (SnakesProto.GameState.Coord snakePoint:aSnake.getPointsList()
                                     ) {
                                    snake.add(new FieldCoord(snakePoint.getX(),snakePoint.getY()));
                                }
                                snakes.add(snake);
                            }
                            if(abstractField != null){
                                abstractField.redraw(coords,snakes);
                            }
                            playersController.refresh();

                        }
                        break;
                    default:
                        notification += "undefined type";
                        break;
                }
                //System.out.println(notification);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }
    }
}
