package game.snake.core.action;

import game.snake.core.GameSettings;
import game.snake.core.elements.Field;
import game.snake.core.metaelements.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ActionController {
    private List<Action> actionsQueue = new CopyOnWriteArrayList<>();
    private List<Player> playersList = new CopyOnWriteArrayList<>();

    private Field mainField;

    private List<Action> previousActionStorage = new ArrayList<>();

    public ActionController(Field mainField) {
        this.mainField = mainField;
    }

    public void addPlayer(Player player){
        if(!playersList.contains(player)){
            player.id = playersList.size()+1;
            player.ISPLAYERIDINITIALEZEDBYACTIONCONTROLLER = true;
            playersList.add(player);
            actionsQueue.add(new Action(player,PlayerAction.moveUP,mainField,null));
        }

    }
    public void removePlayer(Player player){
        playersList.remove(player);
    }

    private Action findPreviousAction(Player player){
        for (Action previousAction:previousActionStorage
             ) {
            if(previousAction.player == player)
                return previousAction;
        }
        return null;
    }

    public void addActionInQueue(Player player, PlayerAction action){
        actionsQueue.add(new Action(player, action, mainField, findPreviousAction(player)));
    }

    public void updateState(GameSettings settings){
        mainField.clearMarks();
        mainField.update(playersList.size(),settings);

        ArrayList<Player> queue = new ArrayList<>(playersList);
        if(actionsQueue.isEmpty()){
            if(previousActionStorage.isEmpty()){
                for (Player player:playersList
                ) {
                    addActionInQueue(player,PlayerAction.moveUP);
                }
            }else{
                for (Player player:playersList
                ) {
                    for (Action previousAction:previousActionStorage
                    ) {
                        if(previousAction.player == player){
                            actionsQueue.add(previousAction);
                        }
                    }
                }
            }
        }

        List<Player> hasActions = new ArrayList<>();
        for (Action action:actionsQueue
             ) {
            if(!hasActions.contains(action.player)){
                hasActions.add(action.player);
            }
        }
        for (Player player:playersList
        ) {
            if(!hasActions.contains(player)){
                for (Action previousAction:previousActionStorage
                ) {
                    if(previousAction.player == player){
                        actionsQueue.add(previousAction);
                    }
                }
            }
        }

        Collections.reverse(actionsQueue);
        for (Action queuedAction:actionsQueue
        ) {
            if(queue.contains(queuedAction.player)){
                queue.remove(queuedAction.player);
                for (Action previousAction:previousActionStorage
                ) {
                    if(previousAction.player == queuedAction.player){
                        previousActionStorage.remove(previousAction);
                        break;
                    }
                }
                queuedAction.doAction();
                previousActionStorage.add(queuedAction);
            }
        }
        actionsQueue.clear();
    }
}

