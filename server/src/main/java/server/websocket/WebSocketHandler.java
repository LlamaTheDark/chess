package server.websocket;

import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import serial.Serializer;
import service.WebSocketService;
import service.exception.BadRequestException;
import service.exception.ForbiddenException;
import service.exception.UnauthorizedException;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessage.ServerMessageType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

@WebSocket
public
class WebSocketHandler {
    private final WebSocketConnectionManager wsSessionManager;
    private final WebSocketService           wsService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public
    WebSocketHandler() {
        wsSessionManager = new WebSocketConnectionManager();
        wsService = new WebSocketService();
    }

    public
    WebSocketConnectionManager getWsSessionManager() {
        return wsSessionManager;
    }

    @OnWebSocketConnect
    public
    void onConnect(Session session) {
        System.out.println("Web socket connected! Session open at: " + session.getRemoteAddress());
    }

    @OnWebSocketClose
    public
    void onClose(Session session, int i, String s) {
        System.out.println("Web socket closed! Session was at: " + session.getRemoteAddress());
        wsSessionManager.removeSession(session);
    }

    @OnWebSocketError
    public
    void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

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
                 DataAccessException |
                 InvalidMoveException |
                 ForbiddenException e) {
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
        try {
            // 1. add the session to our session manager
            var sessionUsername = wsService.getUsernameFromAuthToken(command.getAuthToken());

            if (wsService.getGameDataFromID(command.getGameID()) == null) {
                throw new BadRequestException("Error: there is no game with this ID!");
            }

            wsSessionManager.addSessionToGame(
                    command.getGameID(),
                    session,
                    sessionUsername
            );

            // 2. broadcast a join game notification to all in the same group
            var gameData = wsService.getGameDataFromID(command.getGameID());
            boolean observing =
                    !Arrays.asList(gameData.whiteUsername(), gameData.blackUsername()).contains(sessionUsername);
            broadcastMessage(
                    command.getGameID(),
                    new NotificationMessage(
                            ServerMessageType.NOTIFICATION,
                            String.format("%s %s", sessionUsername,
                                          observing
                                          ? "is observing the game."
                                          : String.format(
                                                  "has joined the game as %s!",
                                                  sessionUsername.equals(gameData.whiteUsername()) ? "WHITE" : "BLACK"
                                          )
                            )
                    ),
                    session
            );

            // 3. create and send a server message load game response
            sendMessage(
                    session,
                    new LoadGameMessage(
                            ServerMessageType.LOAD_GAME,
                            wsService.getGameDataFromID(command.getGameID()).game()
                    )
            );
        } catch (BadRequestException e) {
            sendMessage(
                    session,
                    new ErrorMessage(
                            ServerMessageType.ERROR,
                            e.getMessage()
                    )
            );
        }
    }

    private
    void makeMove(MakeMoveCommand command, Session session) throws DataAccessException, UnauthorizedException {

        // 1. get game and authenticate
        var gameData = wsService.getGameDataFromID(command.getGameID());
        var username = wsService.getUsernameFromAuthToken(command.getAuthToken());

        // 2. check move validity
        // 3. if valid, make the change on the board
        // 3. if not valid, send back an error message
        try {
            if (gameData.game().isOver()) {
                throw new InvalidMoveException("Failed to make move: this game is over.");
            }

            var pieceAtPosition = gameData.game().getBoard().getPiece(command.getMove().getStartPosition());
            if (pieceAtPosition == null) {
                throw new InvalidMoveException("There is no piece at that starting location!");
            }

            if (pieceAtPosition.getTeamColor() != WHITE && username.equals(gameData.whiteUsername())
                || pieceAtPosition.getTeamColor() != BLACK && username.equals(gameData.blackUsername())) {
                throw new InvalidMoveException("That's not your piece!");
            }

            if (!Arrays.asList(gameData.whiteUsername(), gameData.blackUsername()).contains(username)) {
                throw new InvalidMoveException("Error, failed to make move: you're not a player in this game. ");
            }

            gameData.game().makeMove(command.getMove());

            // notification
            broadcastMessage(
                    gameData.gameID(),
                    new NotificationMessage(
                            ServerMessageType.NOTIFICATION,
                            String.format(
                                    "%s has made a move (%s)",
                                    wsSessionManager.getUsername(session),
                                    command.getMove()
                            )
                    ),
                    session
            );
            if (gameData.game().isInCheckmate(gameData.game().getTeamTurn())) {
                gameData.game().markAsOver();
                broadcastMessage(
                        gameData.gameID(),
                        new NotificationMessage(
                                ServerMessageType.NOTIFICATION,
                                String.format("Team %s is in checkmate!", gameData.game().getTeamTurn())
                        )
                );
            } else if (gameData.game().isInCheck(gameData.game().getTeamTurn())) {
                broadcastMessage(
                        gameData.gameID(),
                        new NotificationMessage(
                                ServerMessageType.NOTIFICATION,
                                String.format("Team %s is in check!", gameData.game().getTeamTurn())
                        )
                );
            } else if (gameData.game().isInStalemate(gameData.game().getTeamTurn())) {
                gameData.game().markAsOver();
                broadcastMessage(
                        gameData.gameID(),
                        new NotificationMessage(
                                ServerMessageType.NOTIFICATION,
                                "Game is in stalemate!"
                        )
                );
            }

            wsService.updateGame(gameData);

            // load game
            broadcastMessage(gameData.gameID(), new LoadGameMessage(ServerMessageType.LOAD_GAME, gameData.game()));
        } catch (InvalidMoveException e) {
            sendMessage(session, new ErrorMessage(ServerMessageType.ERROR, e.getMessage()));
        }
    }

    private
    void leaveGame(LeaveGameCommand command, Session session)
    throws DataAccessException, InvalidMoveException, UnauthorizedException {
        // 1. get game
        var gameData = wsService.getGameDataFromID(command.getGameID());
        var username = wsService.getUsernameFromAuthToken(command.getAuthToken());

        // 2. update game to not include user anymore
        GameData newGameData = gameData;
        if (username.equals(gameData.whiteUsername())) {
            newGameData = new GameData(
                    gameData.gameID(),
                    null,
                    gameData.blackUsername(),
                    gameData.gameName(),
                    gameData.game()
            );
        } else if (username.equals(gameData.blackUsername())) {
            newGameData = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    null,
                    gameData.gameName(),
                    gameData.game()
            );
        }
        wsService.updateGame(newGameData);

        // 3. remove session from game
        wsSessionManager.removeSessionFromGame(gameData.gameID(), session);

        // 4. broadcast message to all in the game
        broadcastMessage(
                gameData.gameID(),
                new NotificationMessage(
                        ServerMessageType.NOTIFICATION,
                        String.format("%s has left the game.", wsSessionManager.getUsername(session))
                )
        );

        session.close();
    }

    private
    void resignGame(ResignGameCommand command, Session session)
    throws DataAccessException, UnauthorizedException, ForbiddenException {
        GameData gameData = wsService.getGameDataFromID(command.getGameID());
        var username = wsService.getUsernameFromAuthToken(command.getAuthToken());
        try {

            if (!Arrays.asList(gameData.whiteUsername(), gameData.blackUsername()).contains(username)) {
                throw new ForbiddenException("Only players in this game can resign.");
            }

            if (gameData.game().isOver()) {
                throw new ForbiddenException("Failed to resign: this game is already over.");
            }

            gameData.game().markAsOver();
            wsService.updateGame(gameData);

            broadcastMessage(
                    gameData.gameID(),
                    new NotificationMessage(
                            ServerMessageType.NOTIFICATION,
                            String.format(
                                    "%s has resigned. This game is now over.",
                                    wsSessionManager.getUsername(session)
                            )
                    )
            );

            //        activateCloseGameCountdown(gameData, 1);
        } catch (ForbiddenException e) {
            sendMessage(
                    session,
                    new ErrorMessage(ServerMessageType.ERROR, e.getMessage())
            );
        }
    }

    private
    void sendMessage(Session session, ServerMessage message) {
        try {
            session.getRemote().sendString(Serializer.serialize(message));
        } catch (IOException e) {
            System.err.println("Failed to send message! " + e.getMessage());
        }
    }

    private
    void broadcastMessage(int gameID, ServerMessage message, Session session) {
        for (Session s : wsSessionManager.getSessionsForGame(gameID)) {
            if (s != session) {
                sendMessage(s, message);
            }
        }
    }

    private
    void broadcastMessage(int gameID, ServerMessage message) {
        broadcastMessage(gameID, message, null);
    }

    private
    void activateCloseGameCountdown(GameData gameData, int minutes) {
        scheduler.schedule(() -> {
            try {
                wsSessionManager.removeGame(gameData.gameID());
                wsService.closeGame(gameData.gameID());
            } catch (DataAccessException e) {
                System.err.printf(
                        "Failed to close game with id %d. Message: %s",
                        gameData.gameID(),
                        e.getMessage()
                );
            }
        }, minutes, TimeUnit.MINUTES);
    }
}
