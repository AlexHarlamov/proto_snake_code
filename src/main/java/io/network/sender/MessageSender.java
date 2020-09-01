package io.network.sender;

import game.snake.core.GameSettings;
import game.snake.core.metaelements.Player;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class MessageSender {

    public static GameSettings settings;

    public static DatagramSocket socket;

    public static void initSocket(GameSettings settings){
        MessageSender.settings = settings;
        if(socket != null){
            if(!socket.isClosed()){
                socket.close();
            }
        }
        try {
            MessageSender.socket = new DatagramSocket(settings.port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private static Map<Player, AddressPair> sendList = new HashMap<>();

    public static Map<Player, AddressPair> getSendList() {
        return sendList;
    }

    static InetAddress multicastIPGroup;

    static {
        try {
            multicastIPGroup = InetAddress.getByName("239.192.0.4");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    static MulticastSocket multicastSocket;

    public static void initMulticast(){
        try {
            multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(multicastIPGroup);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MulticastSocket getMulticastSocket() {
        return multicastSocket;
    }

    static int multicastPort = 9192;

    public static void setMulticastPort(int multicastPort) {
        MessageSender.multicastPort = multicastPort;
    }

    public static void clearSendList(){
        sendList.clear();
    }

    public static void addPlayer(Player player, InetAddress inetAddress, int port){
        sendList.put(player,new AddressPair(inetAddress,port));
    }

    public static void sendMulticast(byte[] buf){
        try {
            DatagramPacket packet = new DatagramPacket(buf,buf.length,multicastIPGroup,multicastPort);
            multicastSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendBytesToHost(MessageSender.AddressPair host, byte[] buf){
        if(host == null){
            System.out.println("NET: host not configured");
        }else {
            try {
                DatagramPacket packet = new DatagramPacket(buf,buf.length,host.address,host.port);

                socket.send(packet);

            } catch (IOException ignored) {
            }
        }
    }

    public static void sendBytesToAddress(InetAddress address, int port, byte[] buf ) {

            try {
                DatagramPacket packet = new DatagramPacket(buf,buf.length,address,port);

                socket.send(packet);

            } catch (IOException ignored) {
                ignored.printStackTrace();
            }

    }

    public static void sendBytesToPlayer(Player player, byte[] buf ) {

        if (sendList.containsKey(player)) {
            try {
                DatagramPacket packet = new DatagramPacket(buf,buf.length,sendList.get(player).address,sendList.get(player).port);

                socket.send(packet);
            } catch (IOException ignored) {
            }
        }
    }

    public static class AddressPair{
        InetAddress address;
        int port;

        public AddressPair(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }

        public InetAddress getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }

        public void setAddress(InetAddress address) {
            this.address = address;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
