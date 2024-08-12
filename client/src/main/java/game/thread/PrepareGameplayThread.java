package game.thread;

import chess.ChessGame;
import model.GameData;
import ui.EscapeSequences;
import ui.GameplayUI;

public
class PrepareGameplayThread extends Thread {
    private final String                  gameName;
    private final ChessGame.TeamColor     teamColor;
    private final GameplayUI.BoardPrinter printer;
    private final String                  opponentUsername;
    private final ChessGame.TeamColor     turnColor;

    public
    PrepareGameplayThread(GameData gameData, ChessGame.TeamColor teamColor, GameplayUI.BoardPrinter printer) {
        this.printer = printer;
        this.teamColor = teamColor;
        this.gameName = gameData.gameName();
        this.opponentUsername =
                (teamColor == ChessGame.TeamColor.WHITE) ? gameData.blackUsername() : gameData.whiteUsername();
        this.turnColor = gameData.game().getTeamTurn();
    }

    public
    void run() {
        printer.printNotificationsBackground();
        printer.printCommandResponse("type 'help' for a list of commands", EscapeSequences.SET_TEXT_ITALIC);
    }
}
