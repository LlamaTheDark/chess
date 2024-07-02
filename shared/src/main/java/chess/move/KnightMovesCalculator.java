package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        var validMoves = new HashSet<ChessMove>();

        PieceMovesCalculator.validateMovesFromVector(new int[]{2, 1}, validMoves, chessBoard, position, 1, true, true, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{1, 2}, validMoves, chessBoard, position, 1, true, true, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{-1, 2}, validMoves, chessBoard, position, 1, true, true, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{-2, 1}, validMoves, chessBoard, position, 1, true, true, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{-2, -1}, validMoves, chessBoard, position, 1, true, true, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{-1, -2}, validMoves, chessBoard, position, 1, true, true, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{1, -2}, validMoves, chessBoard, position, 1, true, true, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{2, -1}, validMoves, chessBoard, position, 1, true, true, false);

        return validMoves;
    }
}
