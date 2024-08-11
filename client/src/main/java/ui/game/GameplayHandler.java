package ui.game;

import chess.ChessGame;
import model.GameData;
import server.SessionHandler;
import ui.EscapeSequences;
import ui.exception.UnknownCommandException;
import ui.game.thread.NotificationThread;
import ui.game.thread.PrepareGameplayThread;
import ui.game.thread.UpdateGameThread;
import websocket.WebSocketFacade;
import websocket.commands.LeaveGameCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public
class GameplayHandler extends Thread implements GameHandler {
    private       GameplayUI          gameplayUI;
    private final GameData            gameData;
    private final ChessGame.TeamColor teamColor;
    private       WebSocketFacade     wsFacade;

    private final ExecutorService threadManager = Executors.newSingleThreadExecutor();

    private static final String GAMEPLAY_PROMPT = " [GAME COMMAND] >>> ";

    public
    GameplayHandler(ChessGame.TeamColor teamColor, GameData gameData) {
        this.gameData = gameData;
        this.teamColor = teamColor;
        gameplayUI = new GameplayUI(teamColor, gameData.gameName());
        threadManager.execute(new PrepareGameplayThread(gameData.gameName(), teamColor, gameplayUI.getPrinter()));
    }

    public
    void setWebSocketFacade(WebSocketFacade wsFacade) {
        this.wsFacade = wsFacade;
    }

    enum GameplayCommand {
        HELP,
        REDRAW_CHESS_BOARD,
        LEAVE_GAME,
        MAKE_MOVE,
        RESIGN_GAME,
        HIGHLIGHT_LEGAL_MOVES,
        NONE,
    }

    @Override
    public
    void start() {
        assert wsFacade != null;

        Scanner in = new Scanner(System.in);
        GameplayCommandHandler handler = new GameplayCommandHandler();
        GameplayCommand command = GameplayCommand.NONE;

        do {
            try {
                gameplayUI.getPrinter().printCommandPrompt(GAMEPLAY_PROMPT);

                command = switch (in.next().toLowerCase()) {
                    case "help", "h" -> GameplayCommand.HELP;
                    case "redraw", "r" -> GameplayCommand.REDRAW_CHESS_BOARD;
                    case "leave", "l" -> GameplayCommand.LEAVE_GAME;
                    case "move", "m" -> GameplayCommand.MAKE_MOVE;
                    case "resign", "rs" -> GameplayCommand.RESIGN_GAME;
                    case "moves", "pmoves", "hlm" -> GameplayCommand.HIGHLIGHT_LEGAL_MOVES;
                    default -> throw new UnknownCommandException();
                };

                switch (command) {
                    case HELP -> handler.handleHelp();
                    case REDRAW_CHESS_BOARD -> handler.handleRedrawChessBoard();
                    case LEAVE_GAME -> handler.handleLeaveGame();
                    case MAKE_MOVE -> handler.handleMakeMove();
                    case RESIGN_GAME -> handler.handleResignGame();
                    case HIGHLIGHT_LEGAL_MOVES -> handler.handleHighlightLegalMoves();
                }
            } catch (UnknownCommandException | IOException e) {
                in.nextLine();
                gameplayUI.getPrinter().printCommandResponse(e.getMessage());
            }
        } while (command != GameplayCommand.LEAVE_GAME);


        System.out.print(EscapeSequences.ERASE_SCREEN);
    }

    @Override
    public
    void updateGame(ChessGame game) throws InterruptedException {
        this.gameData.game().setBoard(game.getBoard());
        var updateGameThread = new UpdateGameThread(this.gameplayUI, game.getBoard());
        threadManager.execute(updateGameThread);
    }

    @Override
    public
    void printMessage(String message) {
        threadManager.execute(new NotificationThread(message, this.gameplayUI));
    }

    private
    class GameplayCommandHandler {

        void handleHelp() {
            gameplayUI.getPrinter().printCommandResponse("""
                                                          help/h - display a list of possible commands.
                                                          redraw/r - redraw the board.
                                                          leave/l - leave the game.
                                                          move/m <STARTING POSITION> <ENDING POSITION> - move a piece from the first to the second position.
                                                          resign/rs - forfeit the game.
                                                          moves/hlm <POSITION> - highlight the legal moves for a given piece on the board.
                                                                                                                 \s
                                                          note: positions should be given in the form <letter>+<number>
                                                                e.g. 'a1', 'e6', etc.
                                                         \s""");
        }

        void handleRedrawChessBoard() {
            threadManager.execute(new UpdateGameThread(gameplayUI, gameData.game().getBoard()));
        }

        void handleLeaveGame() throws IOException {
            wsFacade.leaveGame(new LeaveGameCommand(
                    UserGameCommand.CommandType.LEAVE,
                    SessionHandler.authToken,
                    gameData.gameID(),
                    teamColor
            ));
        }

        void handleMakeMove() {

        }

        void handleResignGame() {

        }

        void handleHighlightLegalMoves() {

        }

    }
}
