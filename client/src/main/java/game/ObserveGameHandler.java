package game;

import chess.ChessGame;
import game.command.GameplayCommandHandler;
import game.thread.CommandResponseThread;
import game.thread.NotificationThread;
import game.thread.PrepareGameplayThread;
import model.GameData;
import ui.EscapeSequences;
import ui.GameplayUI;
import ui.exception.UnknownCommandException;
import websocket.WebSocketFacade;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public
class ObserveGameHandler extends Thread implements GameHandler {
    private final GameplayUI gameplayUI;
    private       GameData   gameData;

    /**
     * Which team is this client playing or observing for? WHITE or BLACK.
     * <p>
     */
    private final ChessGame.TeamColor teamColor;
    private       WebSocketFacade     wsFacade;

    private final ExecutorService threadManager = Executors.newSingleThreadExecutor();

    private static final String GAMEPLAY_PROMPT = " [GAME COMMAND] >>> ";

    public
    ObserveGameHandler(ChessGame.TeamColor teamColor, GameData gameData) {
        this.gameData = gameData;
        this.teamColor = teamColor;
        gameplayUI = new GameplayUI(teamColor, gameData.gameName());
        threadManager.execute(new PrepareGameplayThread(gameData, teamColor, gameplayUI.getPrinter()));
    }

    public
    void setWebSocketFacade(WebSocketFacade wsFacade) {
        this.wsFacade = wsFacade;
    }

    enum ObserveGameCommand {
        HELP,
        REDRAW_CHESS_BOARD,
        LEAVE_GAME,
        HIGHLIGHT_LEGAL_MOVES,
        NONE,
    }

    @Override
    public
    void start() {
        assert wsFacade != null;

        Scanner in = new Scanner(System.in);
        GameplayCommandHandler handler =
                new GameplayCommandHandler(gameplayUI, threadManager, wsFacade, gameData, teamColor);
        ObserveGameCommand command = ObserveGameCommand.NONE;

        do {
            try {
                gameplayUI.getPrinter().printCommandPrompt(GAMEPLAY_PROMPT);

                System.out.print(EscapeSequences.SAVE_CURSOR_LOCATION);

                command = switch (in.next().toLowerCase()) {
                    case "help", "h" -> ObserveGameCommand.HELP;
                    case "redraw", "r" -> ObserveGameCommand.REDRAW_CHESS_BOARD;
                    case "leave", "l" -> ObserveGameCommand.LEAVE_GAME;
                    case "moves", "pmoves", "hlm" -> ObserveGameCommand.HIGHLIGHT_LEGAL_MOVES;
                    default -> throw new UnknownCommandException();
                };

                System.out.print(EscapeSequences.LOAD_CURSOR_LOCATION);

                if (gameData.game().isOver() && command != ObserveGameCommand.LEAVE_GAME) {
                    threadManager.execute(new CommandResponseThread(
                            "This game is over. Type 'leave' to leave the game.",
                            gameplayUI,
                            CommandResponseThread.MessageSeverity.STANDARD
                    ));

                } else {
                    switch (command) {
                        case HELP -> handler.handleHelp(true);
                        case REDRAW_CHESS_BOARD -> handler.handleRedrawChessBoard(gameData);
                        case LEAVE_GAME -> handler.handleLeaveGame(gameData);
                        case HIGHLIGHT_LEGAL_MOVES -> handler.handleHighlightLegalMoves(in.next(), gameData);
                    }
                }
            } catch (UnknownCommandException | IOException | NumberFormatException e) {
                in.nextLine();
                gameplayUI.getPrinter().printCommandResponse(
                        e.getMessage(),
                        EscapeSequences.SET_TEXT_COLOR_RED,
                        EscapeSequences.SET_TEXT_ITALIC
                );
            }
        } while (command != ObserveGameCommand.LEAVE_GAME);

        // shutdown the thread manager
        GameHandler.shutdownExecutors(threadManager);

        System.out.print(EscapeSequences.ERASE_SCREEN);
    }

    @Override
    public
    void updateGame(ChessGame game) {
        this.gameData = GameHandler.updateGame(game, gameData, gameplayUI, teamColor, threadManager);
    }

    @Override
    public
    void printMessage(String message, boolean error) {
        if (error) {
            System.out.print(EscapeSequences.SAVE_CURSOR_LOCATION);
            gameplayUI.getPrinter()
                      .printCommandResponse(
                              message,
                              EscapeSequences.SET_TEXT_COLOR_RED,
                              EscapeSequences.SET_TEXT_ITALIC
                      );
            System.out.print(EscapeSequences.LOAD_CURSOR_LOCATION);
        } else {
            threadManager.execute(new NotificationThread(message, this.gameplayUI));
        }
    }
}
