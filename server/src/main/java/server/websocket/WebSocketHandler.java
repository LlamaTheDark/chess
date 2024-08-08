package server.websocket;

import exchange.fullduplex.websocket.commands.ConnectCommand;
import exchange.fullduplex.websocket.commands.LeaveGameCommand;
import exchange.fullduplex.websocket.commands.MakeMoveCommand;
import exchange.fullduplex.websocket.commands.ResignGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public
class WebSocketHandler {
    @OnWebSocketConnect
    public
    void onConnect(Session session) {
        System.out.println("Web socket connected! Session: " + session);
    }

    @OnWebSocketClose
    public
    void onClose(Session session) {}

    @OnWebSocketMessage
    public
    void onError(Throwable throwable) {}

    @OnWebSocketMessage
    public
    void onMessage(Session session, String command) {
        // 1. Determine message type
        // 2. Call one of the following methods to process the message
    }


    private
    void connect(ConnectCommand command) {}

    private
    void makeMove(MakeMoveCommand command) {}

    private
    void leaveGame(LeaveGameCommand command) {}

    private
    void resignGame(ResignGameCommand command) {}


}
