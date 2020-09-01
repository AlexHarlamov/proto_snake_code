package io;

import game.snake.core.GameController;
import game.snake.core.GameSettings;
import game.snake.core.metaelements.Player;
import game.snake.core.metaelements.PlayerController;
import io.network.*;
import io.network.reciever.InputMassageHandler;
import io.network.reciever.InputMulticastReceiver;
import io.network.sender.MessageSender;
import io.ui.SnakeGUI;

import java.net.InetAddress;

import static java.lang.System.exit;

public class LocalUIController implements RegisterAble,IASaveAble,IOAble{

    String defaultName;

    GameController gameController ;
    public GameSettings gameSettings ;
    SnakeGUI userInterface ;
    InputMassageHandler receiver = new InputMassageHandler();

    MessageSender.AddressPair host;
    NetPlayerController playerController = new NetPlayerController();

    NetworkActionAdaptor actionAdaptor = new NetworkActionAdaptor();
    PlayerConnectionAccepter playerConnectionAccepter;
    ConnectionInitiator connectionInitiator = new ConnectionInitiator();
    InputMulticastReceiver multicastReceiver = new InputMulticastReceiver();

    NetGamesListController gamesListController = new NetGamesListController();

    NetFieldAdapter fieldAdapter = new NetFieldAdapter();

    boolean connected = false;

    int botCounter = 2;

    public void registerSoloConstructor(SoloPlayerInitiator initiator){
        soloConstructorHandler = initiator;
    }

    private SoloPlayerInitiator soloConstructorHandler;

    public LocalUIController(String[] args) {

        receiver.setPlayerController(playerController);
        registerSoloConstructor(new soloConstructor());
        userInterface = new SnakeGUI();
        bindButtons();
        userInterface.run(userInterface);

        if(args.length == 0){
            defaultName = "SnakePlayer0";
        }
        else{
            defaultName = args[0];
        }

        gameSettings = new GameSettings(10,10, defaultName, Integer.parseInt(args[1]));

        MessageSender.initSocket(gameSettings);

        connectionInitiator.setSrc(this);

        receiver.setSettings(gameSettings);
        receiver.setConnectionInitiator(connectionInitiator);

        MessageSender.setMulticastPort(9192);
        MessageSender.initMulticast();

        multicastReceiver.setNetGamesListController(gamesListController);

        Thread rec = new Thread(receiver);
        Thread mrec = new Thread(multicastReceiver);

        rec.start();
        mrec.start();
    }

    private void bindButtons(){
        userInterface.bindActionToButton(userInterface.startButton,this);
        userInterface.bindActionToButton(userInterface.exitButton,this);
        userInterface.registerMoveHandler(actionAdaptor);
        userInterface.bindActionToButton(userInterface.settingsButton,this);
        userInterface.bindActionToButton(userInterface.connectButton,this);
        userInterface.bindActionToButton(userInterface.addBotButton, this);
    }

    public void runGame() {
        if(gameController != null){
            exitGame();
        }

        gameController = new GameController();

        receiver.setGameController(gameController);

        gameController.init(gameSettings);
        soloConstructorHandler.initiateSoloPlayer(gameSettings,this);
        gameController.addPlayer(soloConstructorHandler.getPlayer());

        gameController.registerListener(userInterface.getNewFieldUI(gameSettings));
        gameController.registerPlayerControllerListener(userInterface);

        actionAdaptor.setHost(host);

        playerConnectionAccepter = new PlayerConnectionAccepter(gameController,playerController,actionAdaptor.getProtoController());
        fieldAdapter.setProtoController(actionAdaptor.getProtoController());
        fieldAdapter.setGameSettings(gameSettings);

        receiver.setPlayerConnectionAccepter(playerConnectionAccepter);
        connectionInitiator.startAnnouncePlayers(actionAdaptor.getProtoController(),gameSettings);

        gameController.registerPlayerControllerListener(fieldAdapter);
        gameController.registerListener(fieldAdapter);

        gameController.run();
    }

    public void exitGame(){
        userInterface.close();
        if((gameController == null || gameController.currentStatus == GameController.GameStatus.SCRATCH) && !connected){
            exit(0);
        }

        if(gameController == null || gameController.currentStatus == GameController.GameStatus.SCRATCH){

        }
        if(!connected){
            connectionInitiator.stopAnnouncePlayers();
            gameController.stop();
        }

        MessageSender.clearSendList();
        playerController.clear();

        actionAdaptor.getProtoController().resetCounter();

        actionAdaptor.setHost(null);

        gameController = null;
        userInterface = new SnakeGUI();
        bindButtons();
        userInterface.run(userInterface);

        connected = false;
    }

    public void callSettings(){
        userInterface.callSettings(gameSettings);
    }

    @Override
    public void addBot() {
        Player player = new Player();
        player.name = "Bot"+botCounter;
        gameController.addPlayer(player);
        playerController.addPlayer("127.0.0."+botCounter,player,9000);
        botCounter++;
    }

    @Override
    public void showGames() {
        userInterface.showGames(gamesListController, connectionInitiator);
    }

    public void connectTo(InetAddress address, int port){
        if(gameController != null){
            exitGame();
        }

        AbstractField abstractField = new AbstractField();
        abstractField.registerListener(userInterface.getNewFieldUI(gamesListController.getByAddress(address).convertToGameSettings(gameSettings.name, gameSettings.port)));
        actionAdaptor.setHost(new MessageSender.AddressPair(address,port));

        AbstractPlayersController abstractPlayersController = new AbstractPlayersController();
        abstractPlayersController.addListener(userInterface);

        receiver.setPlayersController(abstractPlayersController);

        abstractField.initByAbstractGameSettings(gamesListController.getByAddress(address));

        receiver.setAbstractField(abstractField);

        connected = true;
    }

    @Override
    public void saveIAPair(MessageSender.AddressPair hostInfo) {
        this.host = hostInfo;
        playerController.addPlayer(hostInfo.getAddress().toString(),soloConstructorHandler.getPlayer(),gameSettings.port);
    }

    @Override
    public MessageSender.AddressPair getHostAI() {
        return host;
    }
}
