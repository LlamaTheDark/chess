package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator extends PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        var possibleMoves = new HashSet<ChessMove>();

        PieceMovesCalculator.addPossibleMoves(new int[]{ 1,  1}, possibleMoves, chessBoard, position, ChessBoard.getBoardSize()-1, true, true, false);
        PieceMovesCalculator.addPossibleMoves(new int[]{-1,  1}, possibleMoves, chessBoard, position, ChessBoard.getBoardSize()-1, true, true, false);
        PieceMovesCalculator.addPossibleMoves(new int[]{-1, -1}, possibleMoves, chessBoard, position, ChessBoard.getBoardSize()-1, true, true, false);
        PieceMovesCalculator.addPossibleMoves(new int[]{ 1, -1}, possibleMoves, chessBoard, position, ChessBoard.getBoardSize()-1, true, true, false);

        return possibleMoves;
    }
}
