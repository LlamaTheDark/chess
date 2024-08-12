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
    private final boolean                 observing;

    public
    UpdateGameInformationThread(ChessGame.TeamColor teamColor,
                                GameData gameData,
                                GameplayUI.BoardPrinter printer,
                                boolean observing) {
        this.gameData = gameData;
        this.printer = printer;
        this.teamColor = teamColor;
        this.observing = observing;
    }

    public
    void run() {
        System.out.print(EscapeSequences.SAVE_CURSOR_LOCATION);
        printer.printChessGameInformation(
                gameData.gameName(),
                teamColor,
                gameData.game().getTeamTurn(),
                observing
        );
        System.out.print(EscapeSequences.LOAD_CURSOR_LOCATION);
    }


}

