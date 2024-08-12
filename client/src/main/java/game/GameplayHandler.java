package game;

import chess.ChessGame;
import game.thread.PrepareGameplayThread;
import model.GameData;
import ui.GameplayUI;
import websocket.WebSocketFacade;

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
    void setWebSocketFacade(WebSocketFacade wsFacade) {this.wsFacade = wsFacade;}

    @Override
    public
    void start() {
        GameHandler.repl(gameplayUI, threadManager, wsFacade, gameData, teamColor, false, GAMEPLAY_PROMPT);
    }

    @Override
    public
    void updateGame(ChessGame game) {
        this.gameData = GameHandler.updateGame(game, gameData, gameplayUI, teamColor, threadManager);
    }

    @Override
    public
    void printMessage(String message, boolean error) {
        GameHandler.printMessage(message, error, gameplayUI, threadManager);
    }
}
