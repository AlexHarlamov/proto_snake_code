package game.snake.core.action;

import game.snake.core.elements.Field;
import game.snake.core.elements.cell.CellStatus;
import game.snake.core.elements.cell.FieldCell;
import game.snake.core.metaelements.Player;

import java.util.ArrayList;
import java.util.List;

class Action {

    public Player player;
    private PlayerAction action;

    private Field fieldLink;

    private Action previousAction;

    Action(Player player, PlayerAction action, Field fieldLink, Action previousAction) {
        this.player = player;
        this.action = action;
        this.fieldLink = fieldLink;
        this.previousAction = previousAction;
    }

    void doAction(){

        if(previousAction != null){
            if(action.getValue()*previousAction.action.getValue() == 2 || action.getValue()*previousAction.action.getValue() == 12)
                action = previousAction.action;
        }


        FieldCell next = calculateNext(action);

        List<FieldCell> previousBody = player.snake.body;
        FieldCell previousHead = player.snake.head;

        List<FieldCell> newBody = new ArrayList<>();

        newBody.add(0,previousHead);

        int isGrowing = 0;

        if(next.getStatuses().size() >= 1){
            if(next.getStatuses().containsKey(CellStatus.FOOD)){
                isGrowing++;
            }
        }

        for (int i = 0; i < previousBody.size() - (1-isGrowing); i++) {
            newBody.add(i+1,previousBody.get(i));
        }

        player.snake.lastDirection = action;
        player.snake.update(next,newBody);
    }

    private FieldCell calculateNext(PlayerAction action){
        FieldCell next = null;
        switch (action){
            case moveUP:
                next = fieldLink.calculateUP(player.snake.head);
                break;
            case moveDown:
                next = fieldLink.calculateDown(player.snake.head);
                break;
            case moveLeft:
                next = fieldLink.calculateLeft(player.snake.head);
                break;
            case moveRight:
                next = fieldLink.calculateRight(player.snake.head);
        }
        return next;
    }
}

