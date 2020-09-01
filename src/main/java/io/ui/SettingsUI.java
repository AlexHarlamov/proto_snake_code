package io.ui;

import game.snake.core.GameSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsUI extends JFrame {

    JPanel panel;
    JLabel port_label,name_label, X_label,Y_label;
    JTextField port_text,name_text,X_text,Y_text;
    JButton submit = new JButton("Save");

    GameSettings gameSettings;

    public void showSettings(GameSettings gameSettings) {

        this.gameSettings = gameSettings;

        port_label = new JLabel();
        port_label.setText("Port :");
        port_text = new JTextField(""+gameSettings.port);

        name_label = new JLabel();
        name_label.setText("Name :");
        name_text = new JTextField(""+gameSettings.name);

        X_label = new JLabel();
        X_label.setText("X size :");
        X_text = new JTextField(""+gameSettings.sizeX);

        Y_label = new JLabel();
        Y_label.setText("Y size :");
        Y_text = new JTextField(""+gameSettings.sizeY);

        panel = new JPanel(new GridLayout(5, 1));

        panel.add(port_label);
        panel.add(port_text);
        panel.add(name_label);
        panel.add(name_text);
        panel.add(X_label);
        panel.add(X_text);
        panel.add(Y_label);
        panel.add(Y_text);
        panel.add(submit);

        setContentPane(panel);

        submit.setFocusable(false);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int port = Integer.parseInt(port_text.getText());
                int X = Integer.parseInt(X_text.getText());
                int Y = Integer.parseInt(Y_text.getText());
                String name = name_text.getText();

                gameSettings.setSettings(X,Y,name,port);
            }
        });

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setSize(400,250);
        setVisible(true);
    }
}
