package game.snake.controls;

import game.snake.core.elements.cell.CellStatus;

import java.util.List;

public interface OutputListener {
    void catchUpdate(List<CellStatus> map);
}
