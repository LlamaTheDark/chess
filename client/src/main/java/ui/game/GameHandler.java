package ui.game;

import chess.ChessGame;

public
interface GameHandler {

    void start();

    void updateGame(ChessGame game) throws InterruptedException;

    void printMessage(String message);
}
