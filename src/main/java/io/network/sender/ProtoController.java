package io.network.sender;

import game.snake.controls.MessageController;
import game.snake.core.GameSettings;
import game.snake.core.action.PlayerAction;
import game.snake.core.elements.Snake;
import game.snake.core.metaelements.Player;
import game.snake.core.metaelements.PlayerStatus;
import game.snake.network.SnakesProto;
import io.fieldadaptorelements.FieldCoord;

import java.util.List;
import java.util.Map;


public class ProtoController {
    private int msg_seq = 0;

    public void resetCounter(){
        msg_seq = 0;
    }

    public byte[] configureGameState(List<List<FieldCoord>> snakes,List<FieldCoord> food, GameSettings settings, List<PlayerAction> directions){
        SnakesProto.GameMessage.Builder gameMessage = SnakesProto.GameMessage.newBuilder();
        SnakesProto.GameState.Builder gameStateBuilder = SnakesProto.GameState.newBuilder();

        int foodIdx = 0;
        for (FieldCoord foodCoord:food
             ) {
            gameStateBuilder.addFoods(foodIdx, SnakesProto.GameState.Coord.newBuilder().setX(foodCoord.pointX).setY(foodCoord.pointY).build());
            foodIdx++;
        }

        int snakeIdx = 0;
        for (List<FieldCoord>snake:snakes
             ) {
            SnakesProto.GameState.Snake.Builder snakeBuilder = SnakesProto.GameState.Snake.newBuilder();
            for (FieldCoord coord:snake
                 ) {
                snakeBuilder.addPoints(SnakesProto.GameState.Coord.newBuilder().setX(coord.pointX).setY(coord.pointY).build());
            }
            snakeBuilder.setPlayerId(0);
            snakeBuilder.setState(SnakesProto.GameState.Snake.SnakeState.ALIVE);
            switch (directions.get(snakeIdx)){
                case moveUP:
                    snakeBuilder.setHeadDirection(SnakesProto.Direction.UP);
                    break;
                case moveRight:
                    snakeBuilder.setHeadDirection(SnakesProto.Direction.RIGHT);
                    break;
                case moveLeft:
                    snakeBuilder.setHeadDirection(SnakesProto.Direction.LEFT);
                    break;
                case moveDown:
                    snakeBuilder.setHeadDirection(SnakesProto.Direction.DOWN);
                    break;
                default:
                    snakeBuilder.setHeadDirection(SnakesProto.Direction.UP);
                    break;
            }
            gameStateBuilder.addSnakes(snakeIdx,snakeBuilder);
            snakeIdx++;
        }

        gameStateBuilder.setStateOrder(msg_seq);

        SnakesProto.GameConfig.Builder gameConfigBuilder = SnakesProto.GameConfig.newBuilder();
        gameConfigBuilder.setHeight(settings.sizeY);
        gameConfigBuilder.setWidth(settings.sizeX);
        gameStateBuilder.setConfig(gameConfigBuilder);
        SnakesProto.GamePlayers.Builder players = SnakesProto.GamePlayers.newBuilder();
        for(Map.Entry<Player, MessageSender.AddressPair> entry : MessageSender.getSendList().entrySet()){
            SnakesProto.NodeRole role;
            if(entry.getKey().getStatus() == PlayerStatus.VIEWER){
                role = SnakesProto.NodeRole.VIEWER;
            }
            else{
                if(entry.getValue().port == settings.port){
                    role = SnakesProto.NodeRole.MASTER;
                }else{
                    role = SnakesProto.NodeRole.NORMAL;
                }
            }
            players.addPlayers(SnakesProto.GamePlayer.newBuilder()
                    .setId(entry.getKey().id)
                    .setPort(entry.getValue().port)
                    .setScore(entry.getKey().score)
                    .setIpAddress(entry.getValue().address.toString())
                    .setName(entry.getKey().name)
                    .setRole(role)
                    .build());
        }
        gameStateBuilder.setPlayers(players);

        gameMessage.setState(SnakesProto.GameMessage.StateMsg.newBuilder().setState(gameStateBuilder).build());

        gameMessage.setMsgSeq(msg_seq);
        msg_seq++;
        return gameMessage.build().toByteArray();
    }

    public byte[] configureAckMsg(){
        msg_seq++;
        return SnakesProto.GameMessage.newBuilder().setAck(SnakesProto.GameMessage.AckMsg.newBuilder()).setMsgSeq(msg_seq--).build().toByteArray();
    }

    public byte[] configureErrMsg(String error){
        SnakesProto.GameMessage.Builder gameMessage = SnakesProto.GameMessage.newBuilder();
        SnakesProto.GameMessage.ErrorMsg.Builder err = SnakesProto.GameMessage.ErrorMsg.newBuilder();
        err.setErrorMessage(error);
        gameMessage.setError(err);
        gameMessage.setMsgSeq(msg_seq);
        msg_seq++;
        return gameMessage.build().toByteArray();
    }

    public byte[] configureJoinMessage(GameSettings settings){
        SnakesProto.GameMessage.Builder gameMessage = SnakesProto.GameMessage.newBuilder();
        SnakesProto.GameMessage.JoinMsg.Builder joinMsg = SnakesProto.GameMessage.JoinMsg.newBuilder();
        joinMsg.setName(settings.name);
        joinMsg.setOnlyView(settings.isOnlyView);
        gameMessage.setJoin(joinMsg);
        gameMessage.setMsgSeq(msg_seq);
        msg_seq++;
        return gameMessage.build().toByteArray();
    }

    public byte[] configureAnnouncementMessage(GameSettings settings){
        SnakesProto.GameMessage.Builder gameMessage = SnakesProto.GameMessage.newBuilder();
        SnakesProto.GameMessage.AnnouncementMsg.Builder announcementMessage = SnakesProto.GameMessage.AnnouncementMsg.newBuilder();
        SnakesProto.GameConfig.Builder gameConfigBuilder = SnakesProto.GameConfig.newBuilder();
        gameConfigBuilder.setHeight(settings.sizeY);
        gameConfigBuilder.setWidth(settings.sizeX);
        announcementMessage.setConfig(gameConfigBuilder);
        SnakesProto.GamePlayers.Builder players = SnakesProto.GamePlayers.newBuilder();
        for(Map.Entry<Player, MessageSender.AddressPair> entry : MessageSender.getSendList().entrySet()){
            SnakesProto.NodeRole role;
            if(entry.getKey().getStatus() == PlayerStatus.VIEWER){
                role = SnakesProto.NodeRole.VIEWER;
            }
            else{
                if(entry.getValue().port == settings.port){
                    role = SnakesProto.NodeRole.MASTER;
                }else{
                    role = SnakesProto.NodeRole.NORMAL;
                }
            }
            players.addPlayers(SnakesProto.GamePlayer.newBuilder()
                    .setId(entry.getKey().id)
                    .setPort(entry.getValue().port)
                    .setScore(entry.getKey().score)
                    .setIpAddress(entry.getValue().address.toString())
                    .setName(entry.getKey().name)
                    .setRole(role)
                    .build());
        }
        announcementMessage.setPlayers(players);
        gameMessage.setAnnouncement(announcementMessage);
        gameMessage.setMsgSeq(msg_seq);
        msg_seq++;
        return gameMessage.build().toByteArray();
    }

    public byte[] configureMoveMessage(PlayerAction action){
        SnakesProto.GameMessage.Builder gameMessage = SnakesProto.GameMessage.newBuilder();
        SnakesProto.GameMessage.SteerMsg.Builder moveMessage = SnakesProto.GameMessage.SteerMsg.newBuilder();
        SnakesProto.Direction direction = null;
        switch (action){
            case moveUP:
                direction = SnakesProto.Direction.UP;
                break;
            case moveDown:
                direction = SnakesProto.Direction.DOWN;
                break;
            case moveLeft:
                direction = SnakesProto.Direction.LEFT;
                break;
            case moveRight:
                direction = SnakesProto.Direction.RIGHT;
                break;
        }
        moveMessage.setDirection(direction);
        gameMessage.setMsgSeq(msg_seq);
        msg_seq++;
        gameMessage.setSteer(moveMessage);
        return gameMessage.build().toByteArray();
    }
}
