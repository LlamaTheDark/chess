package ui.game;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import model.GameData;
import server.SessionHandler;
import ui.EscapeSequences;
import ui.exception.UnknownCommandException;
import ui.game.thread.*;
import websocket.WebSocketFacade;
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public
class GameplayHandler extends Thread implements GameHandler {
    private final GameplayUI gameplayUI;
    private       GameData gameData;
    private final ChessGame.TeamColor teamColor;
    private       WebSocketFacade wsFacade;

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
        GameplayCommandHandler handler = new GameplayCommandHandler();
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

                switch (command) {
                    case HELP -> handler.handleHelp();
                    case REDRAW_CHESS_BOARD -> handler.handleRedrawChessBoard();
                    case LEAVE_GAME -> handler.handleLeaveGame();
                    case MAKE_MOVE -> handler.handleMakeMove(in.next(), in.next());
                    case RESIGN_GAME -> handler.handleResignGame();
                    case HIGHLIGHT_LEGAL_MOVES -> handler.handleHighlightLegalMoves(in.next());
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
            }
        } while (command != GameplayCommand.LEAVE_GAME);


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
        threadManager.execute(new UpdateGameInformationThread(teamColor, gameData, gameplayUI.getPrinter()));
    }

    @Override
    public
    void printMessage(String message, boolean error) {
        if (error) {
            gameplayUI.getPrinter()
                      .printCommandResponse(
                              message,
                              EscapeSequences.SET_TEXT_COLOR_RED,
                              EscapeSequences.SET_TEXT_ITALIC
                      );
        } else {
            threadManager.execute(new NotificationThread(message, this.gameplayUI));
        }
    }

    private
    class GameplayCommandHandler {

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

        void handleMakeMove(String startPosition, String endPosition)
        throws UnknownCommandException, InvalidMoveException, IOException {

            var parsedStartPosition = parsePosition(startPosition);
            if (gameData.game().getTeamTurn() != teamColor) {
                throw new InvalidMoveException("It's not your turn!");
            }
            if (gameData.game().getBoard().getPiece(parsedStartPosition).getTeamColor() != teamColor) {
                throw new InvalidMoveException("That piece doesn't belong to you!");
            }

            var parsedEndPosition = parsePosition(endPosition);

            ChessMove move = new ChessMove(parsedStartPosition, parsedEndPosition);

            wsFacade.makeMove(new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, SessionHandler.authToken,
                                                  gameData.gameID(), move
            ));

            threadManager.execute(new UpdateGameInformationThread(teamColor, gameData, gameplayUI.getPrinter()));
        }

        void handleResignGame() {

        }

        void handleHighlightLegalMoves(String position) throws UnknownCommandException {
            ChessPosition parsedPosition = parsePosition(position);
            var task = threadManager.submit(new HighlightMovesThread(gameData.game().validMoves(parsedPosition),
                                                                     gameplayUI.getPrinter(), gameData.game().getBoard()
            ));
            while (true) {if (task.isDone()) {break;}}
        }

    }
}
