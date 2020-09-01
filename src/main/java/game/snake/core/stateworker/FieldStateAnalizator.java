package game.snake.core.stateworker;

import game.snake.core.GameSettings;
import game.snake.core.elements.Field;
import game.snake.core.elements.cell.CellStatus;
import game.snake.core.elements.cell.FieldCell;
import game.snake.core.metaelements.Player;

import java.nio.channels.NonWritableChannelException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldStateAnalizator {

    private Field mainField;

    private List<NewStatePair> updateQueue = new ArrayList<>();
    private List<FieldCell> removeFoodQueue = new ArrayList<>();

    private ElementsStateModificator stateModificator;

    private GameSettings gameSettings;

    public FieldStateAnalizator(Field mainField, ElementsStateModificator stateModificator, GameSettings gameSettings) {
        this.mainField = mainField;
        this.stateModificator = stateModificator;
        this.gameSettings = gameSettings;
    }

    public void analyze(){
        Map<CellStatus, Player> currentCellStatuses = null;
        FieldCell currentCell = null;

        for (int i = 0; i < gameSettings.sizeY; i++) {
            for (int j = 0; j < gameSettings.sizeX; j++) {
                currentCell = mainField.getCell(j,i);
                currentCellStatuses = mainField.getCellMarks(j,i);

                if(currentCellStatuses.size() > 1){
                    analyzeCell(currentCellStatuses,currentCell);
                }
            }
        }

        stateModificator.executeFieldFoodCleaning(removeFoodQueue);
        stateModificator.executeModificationQueue(updateQueue);

        removeFoodQueue.clear();
        updateQueue.clear();
    }

    private void analyzeCell(Map<CellStatus, Player> otherStatuses, FieldCell cell){
        for (Map.Entry<CellStatus,Player> entry:otherStatuses.entrySet()
             ) {
            List<Player> kills = new ArrayList<>();
            switch (entry.getKey()){
                case HEAD:
                    for (Map.Entry<CellStatus,Player> entryHead:otherStatuses.entrySet()
                         ) {
                        if(entryHead.getValue() != null){
                            if(entryHead.getKey() == CellStatus.HEAD){
                                if(entry.getValue() != entryHead.getValue()){
                                    if(!kills.contains(entryHead.getValue())){
                                        updateQueue.add(new NewStatePair(entryHead.getValue(),PlayerUpdate.KILL));
                                        kills.add(entryHead.getValue());
                                    }
                                    if(!kills.contains(entry.getValue())){
                                        updateQueue.add(new NewStatePair(entry.getValue(),PlayerUpdate.KILL));
                                        kills.add(entry.getValue());
                                    }
                                }
                            }
                        }
                    }
                    break;
                case TAIL:
                    for (Map.Entry<CellStatus,Player> entryTail:otherStatuses.entrySet()
                    ) {
                        if(entryTail.getValue() != null){
                            if(entryTail.getKey() == CellStatus.HEAD){
                                if (!kills.contains(entryTail.getValue())) {
                                    updateQueue.add(new NewStatePair(entryTail.getValue(),PlayerUpdate.KILL));
                                    kills.add(entryTail.getValue());
                                }
                                if(entry.getValue() != entryTail.getValue()){
                                    NewStatePair statePair = new NewStatePair(entry.getValue(),PlayerUpdate.ADDCOIN);
                                    statePair.addCoinFrom(entryTail.getValue());
                                    updateQueue.add(statePair);
                                }
                            }
                        }
                    }
                    break;
                case FOOD:
                    for (Map.Entry<CellStatus,Player> entryFood:otherStatuses.entrySet()
                         ) {
                        if(entryFood.getValue() != null){
                            if(entryFood.getKey() == CellStatus.HEAD){
                                updateQueue.add(new NewStatePair(entryFood.getValue(),PlayerUpdate.ADDCOINFROMFOOD));
                            }
                        }
                    }
                    removeFoodQueue.add(cell);
                    break;
            }
            kills.clear();
        }
    }
}
