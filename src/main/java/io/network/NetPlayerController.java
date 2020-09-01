package io.network;

import company.Main;
import game.snake.core.metaelements.Player;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class NetPlayerController {
    private Map<String,HashMap<Integer ,Player> > playerStringAddressMap = new HashMap<>();
    private Map<Player,String> playerAddressStringMap = new HashMap<>();
    private Map<Player,Integer> playerPortMap = new HashMap<>();
    public void clear(){
        playerStringAddressMap.clear();
    }
    public void addPlayer(String address,Player player, int port){
        if(playerStringAddressMap.containsKey(address)){
            playerStringAddressMap.get(address).put(port,player);
        }else{
            HashMap<Integer,Player> map = new HashMap<>();
            map.put(port,player);
            playerStringAddressMap.put(address,map);
        }
        playerAddressStringMap.put(player,address);
        playerPortMap.put(player,port);
    }
    public Player getPlayerByAddressAndPort(String address, int port){
        if(playerStringAddressMap.get(address) != null){
            for (Map.Entry<Integer,Player> row:playerStringAddressMap.get(address).entrySet()
            ) {
                if(row.getKey() == port){
                    return row.getValue();
                }
            }
        }
        return null;
    }
    public String getAddressByPlayer(Player player){
        return playerAddressStringMap.getOrDefault(player,null);
    }
    public int getPortByPlayer(Player player){
        return playerPortMap.getOrDefault(player,null);
    }
}
