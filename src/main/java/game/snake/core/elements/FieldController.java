package game.snake.core.elements;

import game.snake.core.GameSettings;
import game.snake.core.elements.cell.CellStatus;
import game.snake.core.elements.cell.FieldCell;
import game.snake.core.metaelements.Player;
import game.snake.core.metaelements.PlayerController;

import java.util.ArrayList;
import java.util.List;

public class FieldController {
    private Field field = new Field();

    public FieldController(PlayerController playerController, GameSettings settings) {
        field.initField(settings, playerController);
    }

    public Field getField(){
        return field;
    }

    public boolean addPlayer(Player player){
        return field.addSnake(player);
    }

    public List<CellStatus> getFieldMap(){
        List<CellStatus> map = new ArrayList<>();
        for (FieldCell cell:field.field
             ) {
            if(cell.getStatuses().containsKey(CellStatus.HEAD)){
                map.add(CellStatus.HEAD);
            }else {
                if(cell.getStatuses().containsKey(CellStatus.TAIL)){
                    map.add(CellStatus.TAIL);
                }else {
                    if(cell.getStatuses().containsKey(CellStatus.FOOD)){
                        map.add(CellStatus.FOOD);
                    }else{
                        map.add(CellStatus.FREE);
                    }
                }
            }
        }
        return map;
    }

}
