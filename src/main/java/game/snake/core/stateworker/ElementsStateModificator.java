package game.snake.core.stateworker;

import game.snake.core.action.ActionController;
import game.snake.core.elements.Field;
import game.snake.core.elements.FieldController;
import game.snake.core.elements.cell.FieldCell;
import game.snake.core.metaelements.Player;
import game.snake.core.metaelements.PlayerController;
import game.snake.core.metaelements.PlayerStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementsStateModificator {

    private Map<Player,List<Player>> addCoinLog = new HashMap<>();
    private List<Player> nekroLog = new ArrayList<>();

    private ActionController actionControllerLNK;

    private PlayerController playerController;

    private Field fieldLNK;

    public ElementsStateModificator(ActionController actionControllerLNK, Field fieldLNK, PlayerController playerController) {
        this.actionControllerLNK = actionControllerLNK;
        this.fieldLNK = fieldLNK;
        this.playerController = playerController;
    }

    void executeFieldFoodCleaning(List<FieldCell> list){
        for (FieldCell cell:list
             ) {
            fieldLNK.removeFood(cell);
        }
    }

    void executeModificationQueue(List<NewStatePair> modificationQueue){
        for (NewStatePair currentUpdate:modificationQueue
             ) {
            playerController.notifyListeners();
            Player target = currentUpdate.player;
            PlayerStatus targetStatus = target.getStatus();
            if(targetStatus == PlayerStatus.ALIVE || targetStatus == PlayerStatus.ZOMBIE)
            switch (currentUpdate.update){
                case KILL:
                    target.setStatus(PlayerStatus.VIEWER);
                    actionControllerLNK.removePlayer(target);
                    nekroLog.add(target);
                    fieldLNK.addFoodFromPlayer(target);
                    break;
                case ADDCOIN:
                    target.addCoin();
                    target.addKill();
                    if(!addCoinLog.containsKey(target)){
                        addCoinLog.put(target,new ArrayList<>());
                    }
                    if(!addCoinLog.get(target).contains(currentUpdate.coinFrom)){
                        addCoinLog.get(target).add(currentUpdate.coinFrom);
                    }else{
                          if(currentUpdate.coinFrom.getStatus() != PlayerStatus.VIEWER){
                              currentUpdate.coinFrom.setStatus(PlayerStatus.VIEWER);
                          }
                    }
                    if(!nekroLog.contains(currentUpdate.coinFrom)){
                        nekroLog.add(currentUpdate.coinFrom);
                    }
                    break;
                case ADDCOINFROMFOOD:
                    target.addCoin();
                    break;
            }
        }
    }
}
