package io.network;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NetGamesListController {
    List<AbstractGameSettings> gamesAvailable = new CopyOnWriteArrayList<>();

    public List<AbstractGameSettings> getGamesAvailable() {
        return gamesAvailable;
    }

    public void update(AbstractGameSettings abstractGameSettings){
        boolean contains = false;
        for(AbstractGameSettings gameSettings: gamesAvailable){
            if (gameSettings.compare(abstractGameSettings)){
                contains = true;
                break;
            }
        }
        if(!contains){
            System.out.println("NET: New game available");
            gamesAvailable.add(abstractGameSettings);
        }else{
            for(AbstractGameSettings gameSettings: gamesAvailable){
                if (gameSettings.compare(abstractGameSettings)){
                    gamesAvailable.remove(gameSettings);
                    gamesAvailable.add(abstractGameSettings);
                }
            }
        }
    }
    public List<String> getString(){
        List<String> list = new ArrayList<>();
        int index = 0;
        for(AbstractGameSettings gameSettings: gamesAvailable){
            String str = "("+index+") game with "+gameSettings.players_number+" available on "+gameSettings.address.toString();
            list.add(str);
        }
        return list;
    }

    public AbstractGameSettings getByAddress(InetAddress address){
        AbstractGameSettings tmp = null;
        for(AbstractGameSettings gameSettings: gamesAvailable){
            if(gameSettings.address.equals(address)){
                tmp = gameSettings;
            }
        }
        return tmp;
    }

    public int length(){
        return gamesAvailable.size();
    }
}
