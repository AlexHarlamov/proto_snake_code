package game.snake.core.elements.cell;

import game.snake.core.metaelements.Player;

import java.util.HashMap;
import java.util.Map;

public class FieldCell {

    private Map<CellStatus, Player> statuses = new HashMap<>();

    public int posX;

    public FieldCell(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public int posY;

    public void resetStates(){
        statuses.clear();
    }

    public void addStatus(CellStatus status,Player player){
        statuses.put(status,player);
    }

    public Map<CellStatus, Player> getStatuses() {
        return statuses;
    }
}
