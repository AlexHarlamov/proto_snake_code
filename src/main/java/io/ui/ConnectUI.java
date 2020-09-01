package io.ui;

import game.snake.core.GameSettings;
import io.network.AbstractGameSettings;
import io.network.ConnectionInitiator;
import io.network.NetGamesListController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectUI extends JFrame {

    JPanel panel;
    JButton button;

    public void showConnectList(NetGamesListController gamesListController, ConnectionInitiator initiator){

        if(gamesListController.length() == 0){
            panel = new JPanel(new GridLayout(1,1));
            panel.add(new JLabel("nothing to show here"));
        }else{
            panel = new JPanel(new GridLayout(gamesListController.length()+2,1));
            for (String row:gamesListController.getString()
            ) {
                panel.add(new JLabel(row));
            }
            button = new JButton("Connect");
            JTextField connectTo = new JTextField();
            panel.add(connectTo);
            panel.add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = Integer.parseInt(connectTo.getText());
                    if(gamesListController.length() >= index){
                        AbstractGameSettings link = gamesListController.getGamesAvailable().get(index);
                        initiator.askConnectTo(link.address,link.port, link.convertToGameSettings("nil",link.port));
                    }
                }
            });
        }


        setContentPane(panel);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setSize(400,250);
        setVisible(true);
    }

}
