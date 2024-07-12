package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculatorFIDE extends PieceMovesCalculatorFIDE {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        var possibleMoves = new HashSet<ChessMove>();

        PieceMovesCalculatorFIDE.addAttackRange(new int[]{0, 1}, possibleMoves, chessBoard, position, ChessBoard.getBoardSize()-1, true, true, false);
        PieceMovesCalculatorFIDE.addAttackRange(new int[]{1, 0}, possibleMoves, chessBoard, position, ChessBoard.getBoardSize()-1, true, true, false);
        PieceMovesCalculatorFIDE.addAttackRange(new int[]{0, -1}, possibleMoves, chessBoard, position, ChessBoard.getBoardSize()-1, true, true, false);
        PieceMovesCalculatorFIDE.addAttackRange(new int[]{-1, 0}, possibleMoves, chessBoard, position, ChessBoard.getBoardSize()-1, true, true, false);

        return possibleMoves;
    }
}
