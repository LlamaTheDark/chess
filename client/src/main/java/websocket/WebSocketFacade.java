package websocket;


import game.GameHandler;
import serial.Serializer;
import ui.exception.UIException;
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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public
class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String> {
    private Session            session;
    private GameHandler        gameHandler;
    private WebSocketContainer container;


    private final
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> task;

    public
    WebSocketFacade(String url, GameHandler gameHandler) throws UIException {
        try {
            URI uri = new URI(url.replace("http", "ws"));

            container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.gameHandler = gameHandler;

            this.session.addMessageHandler(this);
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new UIException(e.getMessage());
        }
    }

    @Override
    public
    void onOpen(Session session, EndpointConfig endpointConfig) {
        this.task = scheduler.scheduleAtFixedRate(() -> sendPing(session), 0, 4, TimeUnit.MINUTES);
    }

    @Override
    public
    void onClose(Session session, CloseReason closeReason) {
        this.task.cancel(true);
        scheduler.shutdownNow();
        scheduler.close();
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
    void resignGame(ResignGameCommand command) throws IOException {
        sendMessage(Serializer.serialize(command));
    }

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
            throw new RuntimeException(e);
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
