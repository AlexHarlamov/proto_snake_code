package game.snake.core;

import game.snake.controls.OutputListener;
import game.snake.controls.PlayerListener;
import game.snake.core.action.ActionController;
import game.snake.core.elements.FieldController;
import game.snake.core.elements.Snake;
import game.snake.core.metaelements.Player;
import game.snake.core.metaelements.PlayerController;
import game.snake.core.metaelements.PlayerStatus;
import game.snake.core.stateworker.ElementsStateModificator;
import game.snake.core.stateworker.FieldStateAnalizator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {

    public enum GameStatus {
        SCRATCH,
        PENDING,
        RUNNING
    }

    private int timerTickTime = 200;

    private List<OutputListener> listeners = new ArrayList<>();

    private FieldStateAnalizator fieldStateAnalizator;

    private ElementsStateModificator elementsStateModificator;

    public GameStatus currentStatus = GameStatus.SCRATCH;

    private ActionController actionController;

    private FieldController fieldController;

    private PlayerController playerController;

    private TimerTask updateState;

    private Timer ticker = new Timer();

    public ActionController getActionController(){
        return actionController;
    }

    public  boolean validatePlayer(Player player){
        return true;
    }

    public GameSettings gameSettings;

    public void init(GameSettings settings) {

        gameSettings = settings;

        playerController = new PlayerController();
        fieldController = new FieldController(playerController, settings);
        actionController = new ActionController(fieldController.getField());
        elementsStateModificator = new ElementsStateModificator(actionController, fieldController.getField(),playerController);
        fieldStateAnalizator = new FieldStateAnalizator(fieldController.getField(), elementsStateModificator, settings);

        currentStatus = GameStatus.PENDING;

        updateState = new TimerTask() {
            @Override
            public void run() {
                actionController.updateState(gameSettings);
                fieldStateAnalizator.analyze();
                for (OutputListener listener:listeners
                     ) {
                    listener.catchUpdate(fieldController.getFieldMap());
                }
                playerController.notifyListeners();
            }
        };
    }

    public void registerPlayerControllerListener(PlayerListener listener){
        playerController.addListener(listener);
    }

    public void registerListener(OutputListener listener){
        listeners.add(listener);
    }

    public boolean addPlayer(Player player) {
        if (currentStatus != GameStatus.SCRATCH) {
            player.setStatus(PlayerStatus.VIEWER);
            if(fieldController.addPlayer(player)){
                playerController.addPlayer(player);
                actionController.addPlayer(player);
                player.setStatus(PlayerStatus.ALIVE);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void stop(){
        ticker.cancel();
        ticker.purge();
        System.out.println("CORE: game stopped");
    }

    public void run() {
        if (currentStatus != GameStatus.PENDING)
            return;
        currentStatus = GameStatus.RUNNING;
        System.out.println("CORE: game started");

        playerController.activatePlayers();

        ticker.schedule(updateState, 0,timerTickTime);
    }
}
