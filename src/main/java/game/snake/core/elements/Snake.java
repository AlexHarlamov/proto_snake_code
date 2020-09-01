package game.snake.core.elements;

import game.snake.core.action.PlayerAction;
import game.snake.core.elements.cell.CellStatus;
import game.snake.core.elements.cell.FieldCell;
import game.snake.core.metaelements.Player;

import java.util.List;

public class Snake {
    private Player owner;
    public FieldCell head;
    public List<FieldCell> body;

    public PlayerAction lastDirection = PlayerAction.moveUP;

    Snake(Player owner, FieldCell head, List<FieldCell> body) {
        this.owner = owner;
        this.head = head;
        this.body = body;

        update(head,body);
    }

    public CellStatus partOfSnakeStatus(FieldCell cell){
        if(head == cell) return CellStatus.HEAD;
        if(body.contains(cell)) return CellStatus.TAIL;
        return CellStatus.FREE;
    }

    public void update(FieldCell head,List<FieldCell> body){
        this.head = head;
        this.body = body;

        head.addStatus(CellStatus.HEAD,owner);

        for (FieldCell nextCell:body
             ) {
            nextCell.addStatus(CellStatus.TAIL,owner);
        }
    }
}
