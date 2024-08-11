package ui.game.thread;

import chess.ChessBoard;
import ui.EscapeSequences;
import ui.game.GameplayUI;

public
class UpdateGameThread extends Thread {
    private final GameplayUI ui;
    private final ChessBoard board;

    public
    UpdateGameThread(GameplayUI ui, ChessBoard board) {
        this.ui = ui;
        this.board = board;
    }

    public
    void run() {
        System.out.print(EscapeSequences.SAVE_CURSOR_LOCATION);
        ui.getPrinter().printChessBoardBackground();
        ui.getPrinter().printChessBoard(board);
        System.out.print(EscapeSequences.LOAD_CURSOR_LOCATION);
    }
}
