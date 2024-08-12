package game.command;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import game.thread.CommandResponseThread;
import game.thread.HighlightMovesThread;
import game.thread.UpdateGameInformationThread;
import game.thread.UpdateGameThread;
import model.GameData;
import server.SessionHandler;
import ui.GameplayUI;
import ui.exception.UnknownCommandException;
import websocket.WebSocketFacade;
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignGameCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

public
class GameplayCommandHandler {
    GameplayUI          gameplayUI;
    ExecutorService     threadManager;
    WebSocketFacade     wsFacade;
    ChessGame.TeamColor teamColor;

    public
    GameplayCommandHandler(GameplayUI gameplayUI,
                           ExecutorService threadManager,
                           WebSocketFacade wsFacade,
                           GameData gameData,
                           ChessGame.TeamColor teamColor) {
        this.gameplayUI = gameplayUI;
        this.threadManager = threadManager;
        this.wsFacade = wsFacade;
        this.teamColor = teamColor;
    }

    private
    ChessPosition parsePosition(String position) throws UnknownCommandException {
        var e = new UnknownCommandException(
                " Incorrect format provided for chess position.\n Please provide positions in the form " +
                "<letter>+<number>");
        if (position.length() != 2) {
            throw e;
        }
        var input = position.substring(0, 2);
        int col = switch (input.charAt(0)) {
            case 'a' -> 1;
            case 'b' -> 2;
            case 'c' -> 3;
            case 'd' -> 4;
            case 'e' -> 5;
            case 'f' -> 6;
            case 'g' -> 7;
            case 'h' -> 8;
            default -> throw e;
        };
        int row = Integer.parseInt(input.substring(1));

        return new ChessPosition(row, col);
    }

    public
    void handleHelp(boolean observing) {
        if (!observing) {
            gameplayUI.getPrinter().printCommandResponse(
                    """
                     help/h - display a list of possible commands.
                    redraw/r - redraw the board.
                    leave/l - leave the game.
                    move/m <STARTING POSITION> <ENDING POSITION> - move a piece from the first to the second position.
                    resign/rs - forfeit the game.
                    moves/hlm <POSITION> - highlight the legal moves for a given piece on the board.
                                                                           \s
                    note: positions should be given in the form <letter>+<number>
                          e.g. 'a1', 'e6', etc.
                    \s"""
            );
        } else {
            gameplayUI.getPrinter().printCommandResponse(
                    """
                     help/h - display a list of possible commands.
                    redraw/r - redraw the board.
                    leave/l - leave the game.
                    moves/hlm <POSITION> - highlight the legal moves for a given piece on the board.
                                                                           \s
                    note: positions should be given in the form <letter>+<number>
                          e.g. 'a1', 'e6', etc.
                    \s"""
            );
        }
    }

    public
    void handleRedrawChessBoard(GameData gameData) {
        threadManager.execute(new UpdateGameThread(gameplayUI, gameData.game().getBoard()));
    }

    public
    void handleLeaveGame(GameData gameData) throws IOException {
        wsFacade.leaveGame(new LeaveGameCommand(
                UserGameCommand.CommandType.LEAVE,
                SessionHandler.authToken,
                gameData.gameID()
        ));
    }

    public
    void handleMakeMove(String startPosition, String endPosition, GameData gameData)
    throws UnknownCommandException, InvalidMoveException, IOException {

        var parsedStartPosition = parsePosition(startPosition);
        var parsedEndPosition = parsePosition(endPosition);

        ChessMove move = new ChessMove(parsedStartPosition, parsedEndPosition);

        wsFacade.makeMove(new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, SessionHandler.authToken,
                                              gameData.gameID(), move
        ));

        threadManager.execute(new UpdateGameInformationThread(teamColor, gameData, gameplayUI.getPrinter(), false));
    }

    public
    void handleResignGame(GameData gameData) throws IOException {
        wsFacade.resignGame(new ResignGameCommand(
                UserGameCommand.CommandType.RESIGN,
                SessionHandler.authToken,
                gameData.gameID()
        ));
    }

    public
    void handleHighlightLegalMoves(String position, GameData gameData) throws UnknownCommandException {
        ChessPosition parsedPosition = parsePosition(position);
        var pieceAtPosition = gameData.game().getBoard().getPiece(parsedPosition);
        if (pieceAtPosition == null) {
            threadManager.submit(new CommandResponseThread(
                    "There is no piece at that location!",
                    gameplayUI,
                    CommandResponseThread.MessageSeverity.WARNING
            ));
        } else if (pieceAtPosition.getTeamColor() != gameData.game().getTeamTurn()) {
            threadManager.submit(new CommandResponseThread(
                    "This piece has no legal moves because it's not that team's turn.",
                    gameplayUI,
                    CommandResponseThread.MessageSeverity.WARNING
            ));
        } else {
            var task = threadManager.submit(new HighlightMovesThread(
                    gameData.game().validMoves(parsedPosition),
                    gameplayUI.getPrinter(),
                    gameData.game().getBoard()
            ));
            while (true) {if (task.isDone()) {break;}}
        }
    }

}
