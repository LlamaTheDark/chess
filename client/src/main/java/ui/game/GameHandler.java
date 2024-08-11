package ui.game;

import chess.ChessGame;
import websocket.WebSocketFacade;

public
interface GameHandler {

    void start();

    void setWebSocketFacade(WebSocketFacade wsFacade);

    void updateGame(ChessGame game) throws InterruptedException;

    void printMessage(String message);
}
