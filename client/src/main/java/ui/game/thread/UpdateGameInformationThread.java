package ui.game.thread;

import chess.ChessGame;
import model.GameData;
import ui.EscapeSequences;
import ui.game.GameplayUI;

public
class UpdateGameInformationThread extends Thread {
    private final GameData                gameData;
    private final GameplayUI.BoardPrinter printer;
    private final ChessGame.TeamColor     teamColor;

    public
    UpdateGameInformationThread(ChessGame.TeamColor teamColor, GameData gameData, GameplayUI.BoardPrinter printer) {
        this.gameData = gameData;
        this.printer = printer;
        this.teamColor = teamColor;
    }

    public
    void run() {
        System.out.print(EscapeSequences.SAVE_CURSOR_LOCATION);
        printer.printChessGameInformation(
                gameData.gameName(),
                teamColor,
                gameData.game().getTeamTurn()
        );
        System.out.print(EscapeSequences.LOAD_CURSOR_LOCATION);
    }


}

