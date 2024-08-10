package ui.game.thread;

import ui.game.GameplayUI;

public
class UpdateGameThread extends Thread {
    private final GameplayUI ui;

    public
    UpdateGameThread(GameplayUI ui) {
        this.ui = ui;
    }

    public
    void run() {
        //        ui.getPrinter().printChessBoardBackground(ChessGame.TeamColor.WHITE);
    }
}
