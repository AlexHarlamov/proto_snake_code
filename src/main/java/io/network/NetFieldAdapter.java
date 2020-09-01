package io.network;

import game.snake.controls.OutputListener;
import game.snake.controls.PlayerListener;
import game.snake.core.GameSettings;
import game.snake.core.action.PlayerAction;
import game.snake.core.elements.Snake;
import game.snake.core.elements.cell.CellStatus;
import game.snake.core.elements.cell.FieldCell;
import game.snake.core.metaelements.Player;
import game.snake.core.metaelements.PlayerStatus;
import io.fieldadaptorelements.FieldCoord;
import io.network.sender.MessageSender;
import io.network.sender.ProtoController;
import io.ui.SettingsUI;

import java.util.ArrayList;
import java.util.List;

public class NetFieldAdapter implements PlayerListener, OutputListener {

    ProtoController protoController;

    GameSettings gameSettings;

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public void setProtoController(ProtoController protoController) {
        this.protoController = protoController;
    }

    @Override
    public void catchPUpdate(List<Player> players) {
        List<List<FieldCoord>> snakes = new ArrayList<>();
        List<PlayerAction> directions = new ArrayList<>();
        for (Player player:players
             ) {
            directions.add(player.snake.lastDirection);
            if(player.getStatus() != PlayerStatus.VIEWER){
                List<FieldCoord> snakeCoords = new ArrayList<>();
                Snake snake = player.snake;
                int lastX = snake.head.posX;
                int lastY = snake.head.posY;
                snakeCoords.add(new FieldCoord(lastX,lastY));
                int count = 0;
                for (FieldCell cell:snake.body
                ) {
                    snakeCoords.add(new FieldCoord(cell.posX,cell.posY));
                /*count ++;
                if(count > 1){
                    if(lastX != cell.posX || lastY != cell.posY){
                        lastX = cell.posX;
                        lastY = cell.posY;
                        snakeCoords.add(new FieldCoord(lastX,lastY));
                    }
                    count = 0;
                }*/
                }
                snakes.add(snakeCoords);
            }
        }
        byte[] msg =  protoController.configureGameState(snakes,food,gameSettings,directions);
        for (Player sPlayer:players
        ) {
            MessageSender.sendBytesToPlayer(sPlayer,msg);
        }
    }

    List<FieldCoord> food = new ArrayList<>();

    @Override
    public void catchUpdate(List<CellStatus> map) {
        food = new ArrayList<>();
        int i = 0;
        for (CellStatus cellStatus: map) {
            if (cellStatus == CellStatus.FOOD) {
                food.add(new FieldCoord(i % gameSettings.sizeX, i / gameSettings.sizeX));
            }
            i++;
        }
    }
}
