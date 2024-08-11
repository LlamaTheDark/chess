package websocket;


import serial.Serializer;
import ui.exception.UIException;
import ui.game.GameHandler;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public
class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String> {
    Session     session;
    GameHandler gameHandler;

    private final
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public
    WebSocketFacade(String url, GameHandler gameHandler) throws UIException {
        try {
            URI uri = new URI(url.replace("http", "ws"));

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.gameHandler = gameHandler;

            this.session.addMessageHandler(this);
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new UIException(e.getMessage());
            /*
            TODO: this is a terrible exception to throw, don't leave it like this.
             */
        }
    }

    @Override
    public
    void onOpen(Session session, EndpointConfig endpointConfig) {
        scheduler.scheduleAtFixedRate(() -> sendPing(session), 0, 4, TimeUnit.MINUTES);
    }

    @Override
    public
    void onClose(Session session, CloseReason closeReason) {
        scheduler.shutdown();
    }

    @Override
    public
    void onError(Session session, Throwable throwable) {}

    // outgoing messages
    public
    void connect(ConnectCommand command) throws IOException {
        sendMessage(Serializer.serialize(command));
    }

    public
    void makeMove(MakeMoveCommand command) throws IOException {
        sendMessage(Serializer.serialize(command));
    }

    public
    void leaveGame(LeaveGameCommand command) throws IOException {
        sendMessage(Serializer.serialize(command));
    }

    public
    void resignGame(ResignGameCommand command) {}

    private
    void sendMessage(String message) throws IOException {
        // 1. Create command message
        // 2. Send message to server
        this.session.getBasicRemote().sendText(message);
    }

    @Override
    public
    void onMessage(String message) {
        try {
            ServerMessage messageObject = Serializer.deserialize(message, ServerMessage.class);
            switch (messageObject.getServerMessageType()) {
                case LOAD_GAME -> {
                    LoadGameMessage loadGameMessage = (LoadGameMessage) messageObject;
                    gameHandler.updateGame(loadGameMessage.getGame());
                }
                case ERROR -> {
                    ErrorMessage errorMessage = (ErrorMessage) messageObject;
                    gameHandler.printMessage(errorMessage.getErrorMessage(), true);
                }
                case NOTIFICATION -> {
                    NotificationMessage notificationMessage = (NotificationMessage) messageObject;
                    gameHandler.printMessage(notificationMessage.getMessage(), false);
                }
            }
        } catch (NoSuchMethodException |
                 InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException |
                 InterruptedException e) {
            throw new RuntimeException(e); // TODO: improve this
        }
        /*
        1. parse message
        2. call game handler to process the message
         */
    }

    private
    void sendPing(Session session) {
        try {
            if (session.isOpen()) {
                session.getAsyncRemote().sendPing(ByteBuffer.wrap(new byte[]{4, 2}));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
