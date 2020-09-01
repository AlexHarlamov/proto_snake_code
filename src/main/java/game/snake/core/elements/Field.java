package game.snake.core.elements;

import game.snake.core.GameSettings;
import game.snake.core.elements.cell.CellStatus;
import game.snake.core.elements.cell.FieldCell;
import game.snake.core.metaelements.Player;
import game.snake.core.metaelements.PlayerController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Field {
    protected List<FieldCell> field;
    protected int sizeX;
    protected int sizeY;
    protected int fieldSquare;

    private FoodController foodController;
    private PlayerController playerController;

    private class FoodController{
        Field fieldcp;

        List<FieldCell> containsFood = new ArrayList<>();

        public boolean isCellContainsFood(FieldCell cell){
            return containsFood.contains(cell);
        }

        public FoodController(Field fieldcp) {
            this.fieldcp = fieldcp;
        }

        void removeFood(FieldCell cell){
            containsFood.remove(cell);
        }

        void update(int numberOfPlayers, GameSettings settings){
            for (FieldCell cell:containsFood
                 ) {
                cell.addStatus(CellStatus.FOOD, null);
            }
            if(containsFood.size() < numberOfPlayers*settings.foodPerPlayer+settings.constFood){
                add();
            }
        }

        void addFromSnake(List<FieldCell> points){
            for (FieldCell cell:points
                 ) {
                cell.addStatus(CellStatus.FOOD,null);
                containsFood.add(cell);
            }
        }

        void add(){
            int x_max = fieldcp.fieldSquare;
            int x_min = 0;
            Random random = new Random();
            int pos = random.nextInt(x_max-x_min)+x_min;
            if(!fieldcp.field.get(pos).getStatuses().containsKey(CellStatus.FOOD) && !fieldcp.field.get(pos).getStatuses().containsKey(CellStatus.HEAD) && !fieldcp.field.get(pos).getStatuses().containsKey(CellStatus.TAIL)){
                fieldcp.field.get(pos).addStatus(CellStatus.FOOD, null);
                containsFood.add(fieldcp.field.get(pos));
            }
        }
    }

    public void removeFood(FieldCell cell){
        foodController.removeFood(cell);
    }

    public void addFoodFromPlayer(Player player){
        foodController.addFromSnake(player.snake.body);
    }

    public void update(int numberOfPlayers,GameSettings settings){
        foodController.update(numberOfPlayers,settings);
    }

    void initField(GameSettings settings,PlayerController playerController){
        field = new ArrayList<>();
        sizeX = settings.sizeX;
        sizeY = settings.sizeY;

        fieldSquare = sizeX*sizeY;

        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                field.add(new FieldCell(j,i));
            }
        }
        foodController = new FoodController(this);
        this.playerController = playerController;
    }

    public void clearMarks(){
        for (FieldCell cell:field
             ) {
            cell.resetStates();
        }
    }

    public boolean addSnake(Player player){
        int initPos = searchInitPlace();
        if(initPos < 0)
            return false;
        FieldCell head = field.get(initPos);
        List<FieldCell> body = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            body.add(field.get((initPos+sizeX*i)%(fieldSquare)));
        }
        player.snake = new Snake(player,head,body);
        return true;
    }

    private int searchInitPlace(){
        for (int i = 0; i < sizeX*sizeY; i++) {
            if(isSquareIsFree(i)){
                return (i+sizeX+1)%(fieldSquare);
            }
        }
        return -1;
    }

    private boolean isSquareIsFree(int beg){
        for (int i = 0; i < 5; i++) {
            if(!isStrokeOfFiveIsFree(beg+i*sizeX))
                return false;
        }
        return true;
    }
    private boolean isStrokeOfFiveIsFree(int beg){
        beg = beg%(fieldSquare);
        for (int i = 0; i < 5; i++) {
            int forCheck = beg+i;
            int decs = beg/10;
            int circle = decs*10;
            int diff = forCheck%10;
            forCheck = circle+diff;
            FieldCell currentCell = field.get(forCheck);
            for (Player player: playerController.getPlayersList()) {
                if(player.snake.partOfSnakeStatus(currentCell) != CellStatus.FREE)
                    return false;
                if(foodController.isCellContainsFood(currentCell))
                    return false;
            }
        }
        return true;
    }

    public Map<CellStatus, Player> getCellMarks(int posX,int posY){
        return findCell(posX, posY).getStatuses();
    }
    public FieldCell getCell(int posX,int posY){
        return findCell(posX, posY);
    }


    private FieldCell findCell(int posX,int posY){
        return field.get(posY*sizeY+posX);
    }

    public FieldCell calculateDown(FieldCell cell){
      if(cell.posY < sizeY-1){
          return findCell(cell.posX,cell.posY+1);
      }else{
          return findCell(cell.posX,0);
      }
    }
    public FieldCell calculateRight(FieldCell cell){
        if(cell.posX < sizeX-1){
            return findCell(cell.posX+1,cell.posY);
        }else{
            return findCell(0,cell.posY);
        }
    }
    public FieldCell calculateLeft(FieldCell cell){
        if(cell.posX > 0){
            return findCell(cell.posX-1,cell.posY);
        }else{
            return findCell(sizeX-1,cell.posY);
        }
    }
    public FieldCell calculateUP(FieldCell cell){
        if(cell.posY > 0){
            return findCell(cell.posX,cell.posY-1);
        }else{
            return findCell(cell.posX,sizeY-1);
        }
    }
}
