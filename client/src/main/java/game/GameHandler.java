package game;

import chess.ChessGame;
import chess.InvalidMoveException;
import game.command.GameplayCommandHandler;
import game.thread.CommandResponseThread;
import game.thread.NotificationThread;
import game.thread.UpdateGameInformationThread;
import game.thread.UpdateGameThread;
import model.GameData;
import ui.EscapeSequences;
import ui.GameplayUI;
import ui.exception.UnknownCommandException;
import websocket.WebSocketFacade;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public
interface GameHandler {
    enum GameplayCommand {
        HELP,
        REDRAW_CHESS_BOARD,
        LEAVE_GAME,
        MAKE_MOVE,
        RESIGN_GAME,
        HIGHLIGHT_LEGAL_MOVES,
        NONE,
    }

    void start();

    void setWebSocketFacade(WebSocketFacade wsFacade);

    void updateGame(ChessGame game) throws InterruptedException;

    void printMessage(String message, boolean error);

    static
    void shutdownExecutors(ExecutorService... services) {
        for (ExecutorService service : services) {
            // shutdown the thread manager
            service.shutdown();
            try {
                if (service.awaitTermination(5, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                service.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    static
    GameData updateGame(ChessGame game,
                        GameData gameData,
                        GameplayUI gameplayUI,
                        ChessGame.TeamColor teamColor,
                        ExecutorService threadManager,
                        boolean observing) {
        var newGameData = new GameData(
                gameData.gameID(),
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                game
        );
        threadManager.execute(new UpdateGameThread(gameplayUI, game.getBoard()));
        threadManager.execute(new UpdateGameInformationThread(
                teamColor,
                newGameData,
                gameplayUI.getPrinter(),
                observing
        ));

        return newGameData;
    }

    static
    void repl(GameplayUI gameplayUI,
              ExecutorService threadManager,
              WebSocketFacade wsFacade,
              GameData gameData,
              ChessGame.TeamColor teamColor,
              boolean observing,
              String prompt) {

        Scanner in = new Scanner(System.in);
        GameplayCommandHandler handler =
                new GameplayCommandHandler(gameplayUI, threadManager, wsFacade, gameData, teamColor);
        GameplayCommand command = GameHandler.GameplayCommand.NONE;

        do {
            try {
                gameplayUI.getPrinter().printCommandPrompt(prompt);

                System.out.print(EscapeSequences.SAVE_CURSOR_LOCATION);

                command = switch (in.next().toLowerCase()) {
                    case "help", "h" -> GameHandler.GameplayCommand.HELP;
                    case "redraw", "r" -> GameHandler.GameplayCommand.REDRAW_CHESS_BOARD;
                    case "leave", "l" -> GameHandler.GameplayCommand.LEAVE_GAME;
                    case "move", "m" -> GameHandler.GameplayCommand.MAKE_MOVE;
                    case "resign", "rs" -> GameHandler.GameplayCommand.RESIGN_GAME;
                    case "moves", "pmoves", "hlm" -> GameHandler.GameplayCommand.HIGHLIGHT_LEGAL_MOVES;
                    default -> throw new UnknownCommandException();
                };

                System.out.print(EscapeSequences.LOAD_CURSOR_LOCATION);

                if (gameData.game().isOver() && command != GameHandler.GameplayCommand.LEAVE_GAME) {
                    threadManager.execute(new CommandResponseThread(
                            "This game is over. Type 'leave' to leave the game.",
                            gameplayUI,
                            CommandResponseThread.MessageSeverity.STANDARD
                    ));

                } else {
                    switch (command) {
                        case HELP -> handler.handleHelp(observing);
                        case REDRAW_CHESS_BOARD -> handler.handleRedrawChessBoard(gameData);
                        case LEAVE_GAME -> handler.handleLeaveGame(gameData);
                        case MAKE_MOVE -> handler.handleMakeMove(in.next(), in.next(), gameData, observing);
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
        } while (command != GameHandler.GameplayCommand.LEAVE_GAME);

        // shutdown the thread manager
        GameHandler.shutdownExecutors(threadManager);

        System.out.print(EscapeSequences.ERASE_SCREEN);
    }

    static
    void printMessage(String message, boolean error, GameplayUI gameplayUI, ExecutorService threadManager) {
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
            threadManager.execute(new NotificationThread(message, gameplayUI));
        }
    }
}
