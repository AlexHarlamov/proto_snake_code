package io.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import game.snake.controls.PlayerListener;
import game.snake.core.GameSettings;
import game.snake.core.metaelements.Player;
import io.MoveHandler;
import io.RegisterAble;
import io.network.ConnectionInitiator;
import io.network.NetGamesListController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnakeGUI implements PlayerListener, KeyListener {
    private JPanel panel1;
    private JPanel fieldHolder;
    public JButton settingsButton;
    public JButton connectButton;
    public JButton startButton;
    private JTextArea gameInfo;
    public JButton exitButton;
    public JButton addBotButton;

    private JFrame frame = new JFrame("SnakeGUI");

    public FieldUI fieldUI;

    public SnakeGUI() {

        startButton.setFocusable(false);
        connectButton.setFocusable(false);
        exitButton.setFocusable(false);
        settingsButton.setFocusable(false);
        addBotButton.setFocusable(false);

        gameInfo.setEditable(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonBinds.containsKey(startButton)) {
                    buttonBinds.get(startButton).runGame();
                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonBinds.containsKey(exitButton)) {
                    buttonBinds.get(exitButton).exitGame();
                }
            }
        });
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonBinds.containsKey(settingsButton)) {
                    buttonBinds.get(settingsButton).callSettings();
                }
            }
        });
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonBinds.containsKey(connectButton)) {
                    buttonBinds.get(connectButton).showGames();
                }
            }
        });
        addBotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonBinds.containsKey(addBotButton)) {
                    buttonBinds.get(addBotButton).addBot();
                }
            }
        });
    }

    private MoveHandler handler;

    public void registerMoveHandler(MoveHandler handler) {
        this.handler = handler;
    }

    public void close() {

        WindowEvent winClosingEvent = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(winClosingEvent);

    }

    public FieldUI getNewFieldUI(GameSettings settings) {
        fieldUI = new FieldUI(fieldHolder, settings);
        frame.add(fieldUI, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        return fieldUI;
    }

    @Override
    public void catchPUpdate(List<Player> players) {
        gameInfo.setText("");
        for (Player player : players) {
            gameInfo.append(constructRow(player));
        }
    }

    private String constructRow(Player playerInfo) {
        return "(" + playerInfo.getStatus() + ")" + playerInfo.name + " : " + playerInfo.score + " points and " + playerInfo.kills + " kills \n";
    }

    Map<JButton, RegisterAble> buttonBinds = new HashMap<>();

    public boolean bindActionToButton(JButton button, RegisterAble action) {
        if (buttonBinds.containsKey(button))
            return false;
        else
            buttonBinds.put(button, action);
        return true;
    }

    public void run(SnakeGUI guiHandler) {
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        frame.setResizable(false);
        frame.setContentPane(guiHandler.panel1);
        frame.pack();
        frame.setSize(900, 900);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.height / 2 + frame.getSize().height / 4, dim.height / 4);
        frame.setVisible(true);
    }

    public void callSettings(GameSettings settings) {
        SettingsUI setFrame = new SettingsUI();
        setFrame.showSettings(settings);
    }

    public void showGames(NetGamesListController listController, ConnectionInitiator initiator) {
        ConnectUI conFrame = new ConnectUI();
        conFrame.showConnectList(listController, initiator);
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D' || e.getKeyCode() == KeyEvent.VK_RIGHT)
            handler.moveRight();
        else if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W' || e.getKeyCode() == KeyEvent.VK_UP)
            handler.moveUP();
        else if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A' || e.getKeyCode() == KeyEvent.VK_LEFT)
            handler.moveLeft();
        else if (e.getKeyChar() == 's' || e.getKeyChar() == 'S' || e.getKeyCode() == KeyEvent.VK_DOWN)
            handler.moveDown();
    }

    public void keyTyped(KeyEvent e) {
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(10, 2, new Insets(0, 0, 0, 0), -1, -1));
        fieldHolder = new JPanel();
        fieldHolder.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(fieldHolder, new GridConstraints(0, 0, 5, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addBotButton = new JButton();
        addBotButton.setText("AddBot");
        panel1.add(addBotButton, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gameInfo = new JTextArea();
        panel1.add(gameInfo, new GridConstraints(5, 0, 5, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        startButton = new JButton();
        startButton.setText("Start");
        panel1.add(startButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        connectButton = new JButton();
        connectButton.setText("Connect");
        panel1.add(connectButton, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        settingsButton = new JButton();
        settingsButton.setText("Settings");
        panel1.add(settingsButton, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exitButton = new JButton();
        exitButton.setText("Exit");
        panel1.add(exitButton, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
