package io.ui;

import game.snake.controls.OutputListener;
import game.snake.core.GameSettings;
import game.snake.core.elements.cell.CellStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ListIterator;

public class FieldUI extends JPanel implements OutputListener {

    Color free = Color.white;
    Color head = Color.red;
    Color tail = Color.blue;
    Color food = Color.yellow;

    java.util.List<CellStatus> statusBuffer = null;

    JPanel parentPanel;

    private int countX = 10;
    private int countY = 10;

    private int singleSquareX = 0;
    private int singleSquareY = 0;

    public FieldUI(JPanel parentPanel, GameSettings settings) {
        System.out.println("GUI: field constructed");
        this.parentPanel = parentPanel;
        countX = settings.sizeX;
        countY = settings.sizeY;
    }

    @Override
    public void paintComponent(Graphics g) {
        /*g.fillRect(0,0,5,5);
        super.paintComponent(g);*/
        if (statusBuffer == null){
            System.out.println("GUI: field pane is empty because not active game stage");
        }else{
            drawCells(g);
        }

    }

    private void drawCells(Graphics g){
        Dimension parentContainerSize = parentPanel.getSize();
        singleSquareX = (int) ((float) parentContainerSize.width) / countX -10;
        singleSquareY = (int) ((float) parentContainerSize.height) / countY -10;

        int borderSize = Math.min(singleSquareX, singleSquareY);

        ListIterator<CellStatus> statusListIterator = statusBuffer.listIterator();
        for (int i = 0; i < countY; i++) {
            for (int j = 0; j < countX; j++) {
                switch (statusListIterator.next()) {
                    case HEAD:
                        g.setColor(head);
                        break;
                    case TAIL:
                        g.setColor(tail);
                        break;
                    case FREE:
                        g.setColor(free);
                        break;
                    case FOOD:
                        g.setColor(food);
                        break;
                    default:
                        g.setColor(free);
                        break;
                }
                g.fillRect(borderSize * j, borderSize * i, borderSize, borderSize);
            }
        }
    }

    @Override
    public void catchUpdate(List<CellStatus> map) {
        statusBuffer = map;
        repaint();
    }
}

