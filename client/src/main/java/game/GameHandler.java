package game;

import chess.ChessGame;
import game.thread.UpdateGameInformationThread;
import game.thread.UpdateGameThread;
import model.GameData;
import ui.GameplayUI;
import websocket.WebSocketFacade;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public
interface GameHandler {

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
                        ExecutorService threadManager) {
        var newGameData = new GameData(
                gameData.gameID(),
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                game
        );
        threadManager.execute(new UpdateGameThread(gameplayUI, game.getBoard()));
        threadManager.execute(new UpdateGameInformationThread(teamColor, newGameData, gameplayUI.getPrinter(), false));

        return newGameData;
    }
}
