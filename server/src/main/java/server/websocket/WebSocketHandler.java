package server.websocket;

import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import serial.Serializer;
import service.WebSocketService;
import service.exception.UnauthorizedException;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessage.ServerMessageType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@WebSocket
public
class WebSocketHandler {
    private final WebSocketConnectionManager wsSessionManager = new WebSocketConnectionManager();
    private final WebSocketService           wsService        = new WebSocketService();

    @OnWebSocketConnect
    public
    void onConnect(Session session) {
        System.out.println("Web socket connected! Session open at: " + session.getLocalAddress());
    }

    @OnWebSocketClose
    public
    void onClose(Session session, int i, String s) {
        System.out.println("Web socket closed! Session was at: " + session.getLocalAddress());
    }

    @OnWebSocketError
    public
    void onError(Throwable throwable) {}

    @OnWebSocketMessage
    public
    void onMessage(Session session, String command) {
        try {
            UserGameCommand commandObject = Serializer.deserialize(command, UserGameCommand.class);
            switch (commandObject.getCommandType()) {
                case CONNECT -> connect((ConnectCommand) commandObject, session);
                case MAKE_MOVE -> makeMove((MakeMoveCommand) commandObject, session);
                case LEAVE -> leaveGame((LeaveGameCommand) commandObject, session);
                case RESIGN -> resignGame((ResignGameCommand) commandObject, session);
            }
        } catch (UnauthorizedException |
                 NoSuchMethodException |
                 InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException |
                 DataAccessException e) {
            sendMessage(
                    session,
                    new ErrorMessage(
                            ServerMessageType.ERROR,
                            String.format(
                                    "Failed to process command:\n\treason:%s",
                                    e.getMessage()
                            )
                    )
            );
        }
    }


    private
    void connect(ConnectCommand command, Session session) throws UnauthorizedException, DataAccessException {
        // 1. add the session to our session manager
        var sessionUsername = wsService.getUsernameFromAuthToken(command.getAuthToken());

        wsSessionManager.addSessionToGame(
                command.getGameID(),
                session,
                sessionUsername
        );

        // 2. broadcast a join game notification to all in the same group
        broadcastMessage(
                command.getGameID(),
                new NotificationMessage(
                        ServerMessageType.NOTIFICATION,
                        String.format("%s has joined the game!", sessionUsername)
                )
        );

        // 3. create and send a server message load game response
        sendMessage(
                session,
                new LoadGameMessage(
                        ServerMessageType.LOAD_GAME,
                        wsService.getGameDataFromID(command.getGameID()).game()
                )
        );
    }

    private
    void makeMove(MakeMoveCommand command, Session session) throws DataAccessException {
        // 1. get game
        var gameData = wsService.getGameDataFromID(command.getGameID());

        // 2. check move validity
        // 3. if valid, make the change on the board
        // 3. if not valid, send back an error message
        try {
            wsService.makeMove(command.getMove(), gameData);
            // load game
            broadcastMessage(gameData.gameID(), new LoadGameMessage(ServerMessageType.LOAD_GAME, gameData.game()));

            // notification
            broadcastMessage(
                    gameData.gameID(),
                    new NotificationMessage(
                            ServerMessageType.NOTIFICATION,
                            String.format(
                                    "%s has made a move (%s)",
                                    wsSessionManager.getUsername(session),
                                    command.getMove() // TODO: implement nice toString method for ChessMove
                            )
                    )
            );
        } catch (InvalidMoveException e) {
            sendMessage(session, new ErrorMessage(ServerMessageType.ERROR, "Invalid move!"));
        }
    }

    private
    void leaveGame(LeaveGameCommand command, Session session) {}

    private
    void resignGame(ResignGameCommand command, Session session) {}

    private
    void sendMessage(Session session, ServerMessage message) {
        try {
            session.getRemote().sendString(Serializer.serialize(message));
        } catch (IOException e) {
            System.err.println("Failed to send message! " + e.getMessage());
        }
    }

    private
    void broadcastMessage(int gameID, ServerMessage message) {
        for (Session s : wsSessionManager.getSessionsForGame(gameID)) {
            sendMessage(s, message);
        }
    }
}
