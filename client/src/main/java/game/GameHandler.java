package game;

import chess.ChessGame;
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
}
