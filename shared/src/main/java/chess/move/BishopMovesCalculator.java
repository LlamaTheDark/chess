package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        var validMoves = new HashSet<ChessMove>();

        PieceMovesCalculator.validateMovesFromVector(new int[]{ 1,  1}, validMoves, chessBoard, position);
        PieceMovesCalculator.validateMovesFromVector(new int[]{-1,  1}, validMoves, chessBoard, position);
        PieceMovesCalculator.validateMovesFromVector(new int[]{-1, -1}, validMoves, chessBoard, position);
        PieceMovesCalculator.validateMovesFromVector(new int[]{ 1, -1}, validMoves, chessBoard, position);

        return validMoves;
    }
}
