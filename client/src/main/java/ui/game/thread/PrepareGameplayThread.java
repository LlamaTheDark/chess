package ui.game.thread;

import chess.ChessGame;
import ui.EscapeSequences;
import ui.game.GameplayUI;

public
class PrepareGameplayThread extends Thread {
    private final String                  gameName;
    private final ChessGame.TeamColor     teamColor;
    private final GameplayUI.BoardPrinter printer;

    public
    PrepareGameplayThread(String gameName, ChessGame.TeamColor teamColor, GameplayUI.BoardPrinter printer) {
        this.printer = printer;
        this.teamColor = teamColor;
        this.gameName = gameName;
    }

    public
    void run() {
        printer.printNotificationsBackground();
        printer.printChessGameInformation(gameName, teamColor);
        printer.printCommandResponse(" type 'help' for a list of commands", EscapeSequences.SET_TEXT_ITALIC);
    }
}
