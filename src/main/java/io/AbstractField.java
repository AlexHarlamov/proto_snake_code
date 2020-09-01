package io;

import game.snake.controls.OutputListener;
import game.snake.core.elements.Field;
import game.snake.core.elements.cell.CellStatus;
import game.snake.core.elements.cell.FieldCell;
import io.fieldadaptorelements.FieldCoord;
import io.network.AbstractGameSettings;

import java.util.ArrayList;
import java.util.List;

public class AbstractField extends Field{

    public void initByAbstractGameSettings(AbstractGameSettings gameSettings){
        field = new ArrayList<>();
        sizeX = gameSettings.sizeX;
        sizeY = gameSettings.sizeY;

        fieldSquare = sizeX*sizeY;

        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                field.add(new FieldCell(j,i));
            }
        }
    }

    public void redraw(List<FieldCoord> food,List<List<FieldCoord>> snakes){
        for (FieldCoord cords:food
             ) {
            getCell(cords.pointX,cords.pointY).addStatus(CellStatus.FOOD,null);
        }
        parseSnakes(snakes);
        notifyListeners();
        clearMarks();
    }

    private void parseSnakes(List<List<FieldCoord>> snakes){
        for (List<FieldCoord> snake:snakes
             ) {
            int prevX = 0;
            int prevY = 0;
            int idx = 1;
            boolean head = true;
            for (FieldCoord coords:snake
                 ) {
                if(head){
                    getCell(coords.pointX,coords.pointY).addStatus(CellStatus.HEAD,null);
                    prevX = coords.pointX;
                    prevY = coords.pointY;
                    head = false;
                }else{
                    getCell(coords.pointX,coords.pointY).addStatus(CellStatus.TAIL,null);
                    /*if(prevX != coords.pointX){
                        for (int fpointer = prevX; fpointer <= coords.pointX; fpointer++){
                            getCell(fpointer,prevY).addStatus(CellStatus.TAIL,null);
                        }
                    }else{
                        for (int fpointer = prevY; fpointer <= coords.pointY; fpointer++){
                            getCell(prevX,fpointer).addStatus(CellStatus.TAIL,null);
                        }
                    }*/
                }
            }
        }
    }

    List<OutputListener> listeners = new ArrayList<>();
    public void registerListener(OutputListener listener){
        listeners.add(listener);
    }

    public void notifyListeners(){
        for (OutputListener listener: listeners) {
            listener.catchUpdate(getFieldMap());
        }
    }

    public List<CellStatus> getFieldMap(){
        List<CellStatus> map = new ArrayList<>();
        for (FieldCell cell:field
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
