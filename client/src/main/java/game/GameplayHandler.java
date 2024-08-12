package game;

import chess.ChessGame;
import chess.InvalidMoveException;
import game.command.GameplayCommandHandler;
import game.thread.*;
import model.GameData;
import ui.EscapeSequences;
import ui.GameplayUI;
import ui.exception.UnknownCommandException;
import websocket.WebSocketFacade;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public
class GameplayHandler extends Thread implements GameHandler {
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
    GameplayHandler(ChessGame.TeamColor teamColor, GameData gameData) {
        this.gameData = gameData;
        this.teamColor = teamColor;
        gameplayUI = new GameplayUI(teamColor, gameData.gameName());
        threadManager.execute(new PrepareGameplayThread(gameData, teamColor, gameplayUI.getPrinter()));
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
        GameplayCommandHandler handler =
                new GameplayCommandHandler(gameplayUI, threadManager, wsFacade, gameData, teamColor);
        GameplayCommand command = GameplayCommand.NONE;

        do {
            try {
                gameplayUI.getPrinter().printCommandPrompt(GAMEPLAY_PROMPT);

                System.out.print(EscapeSequences.SAVE_CURSOR_LOCATION);

                command = switch (in.next().toLowerCase()) {
                    case "help", "h" -> GameplayCommand.HELP;
                    case "redraw", "r" -> GameplayCommand.REDRAW_CHESS_BOARD;
                    case "leave", "l" -> GameplayCommand.LEAVE_GAME;
                    case "move", "m" -> GameplayCommand.MAKE_MOVE;
                    case "resign", "rs" -> GameplayCommand.RESIGN_GAME;
                    case "moves", "pmoves", "hlm" -> GameplayCommand.HIGHLIGHT_LEGAL_MOVES;
                    default -> throw new UnknownCommandException();
                };

                System.out.print(EscapeSequences.LOAD_CURSOR_LOCATION);

                if (gameData.game().isOver() && command != GameplayCommand.LEAVE_GAME) {

                    threadManager.execute(new CommandResponseThread(
                            "This game is over. Type 'leave' to leave the game.",
                            gameplayUI,
                            CommandResponseThread.MessageSeverity.STANDARD
                    ));

                } else {
                    switch (command) {
                        case HELP -> handler.handleHelp(false);
                        case REDRAW_CHESS_BOARD -> handler.handleRedrawChessBoard(gameData);
                        case LEAVE_GAME -> handler.handleLeaveGame(gameData);
                        case MAKE_MOVE -> handler.handleMakeMove(in.next(), in.next(), gameData);
                        case RESIGN_GAME -> handler.handleResignGame(gameData);
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
                // TODO: change the message for the NUmberFOrmatException
            } catch (InvalidMoveException e) {
                in.nextLine();
                gameplayUI.getPrinter().printCommandResponse(
                        e.getMessage(),
                        EscapeSequences.SET_TEXT_COLOR_YELLOW,
                        EscapeSequences.SET_TEXT_ITALIC
                );
            } catch (NoSuchElementException e) {
                in.nextLine();
                gameplayUI.getPrinter().printCommandResponse(
                        "Unknown command. Please type 'help' for a list of commands.",
                        EscapeSequences.SET_TEXT_COLOR_YELLOW,
                        EscapeSequences.SET_TEXT_ITALIC
                );
            }
        } while (command != GameplayCommand.LEAVE_GAME);

        // shutdown the thread manager
        GameHandler.shutdownExecutors(threadManager);

        System.out.print(EscapeSequences.ERASE_SCREEN);
    }

    @Override
    public
    void updateGame(ChessGame game) {
        this.gameData = new GameData(
                gameData.gameID(),
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                game
        );
        threadManager.execute(new UpdateGameThread(this.gameplayUI, game.getBoard()));
        threadManager.execute(new UpdateGameInformationThread(teamColor, gameData, gameplayUI.getPrinter(), false));
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
