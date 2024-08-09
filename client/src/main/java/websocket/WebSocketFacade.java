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

public
class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String> {
    Session     session;
    GameHandler gameHandler;

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
    void onOpen(Session session, EndpointConfig endpointConfig) {}

    @Override
    public
    void onClose(Session session, CloseReason closeReason) {}

    @Override
    public
    void onError(Session session, Throwable throwable) {}

    // outgoing messages
    public
    void connect(ConnectCommand command) throws IOException {
        this.session.getBasicRemote().sendText(Serializer.serialize(command));
    }

    public
    void makeMove(MakeMoveCommand command) {}

    public
    void leaveGame(LeaveGameCommand command) {}

    public
    void resignGame(ResignGameCommand command) {}

    private
    void sendMessage(String message) {
        // 1. Create command message
        // 2. Send message to server
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
                    System.err.println(errorMessage.getErrorMessage());
                }
                case NOTIFICATION -> {
                    NotificationMessage notificationMessage = (NotificationMessage) messageObject;
                    System.out.println(notificationMessage.getMessage());
                }
            }
        } catch (NoSuchMethodException |
                 InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e); // TODO: improve this
        }
        /*
        1. parse message
        2. call game handler to process the message
         */
    }
}
