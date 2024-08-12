package game.thread;

import chess.ChessBoard;
import chess.ChessMove;
import ui.EscapeSequences;
import ui.GameplayUI;

import java.util.Collection;

public
class HighlightMovesThread extends Thread {
    Collection<ChessMove> validMoves;
    GameplayUI.BoardPrinter printer;
    ChessBoard board;

    public
    HighlightMovesThread(Collection<ChessMove> validMoves, GameplayUI.BoardPrinter printer, ChessBoard board) {
        this.validMoves = validMoves;
        this.printer = printer;
        this.board = board;
    }

    // todo: every time you run "r" after this, it puts your cursor to the start of the line
    public
    void run() {
        printer.printChessBoard(board);
        if (validMoves.isEmpty()) {
            printer.printCommandResponse(
                    " There are not available moves for this piece",
                    EscapeSequences.SET_TEXT_ITALIC, EscapeSequences.SET_TEXT_COLOR_YELLOW
            );
        } else {
            for (var move : validMoves) {
                printer.highlightChessSquare(move.getEndPosition(), board, false);
            }
            printer.highlightChessSquare(validMoves.stream().toList().getFirst().getStartPosition(), board, true);
        }
    }
}
