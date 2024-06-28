package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        var validMoves = new HashSet<ChessMove>();

        PieceMovesCalculator.validateMovesFromVector(new int[]{0, 1}, validMoves, chessBoard, position, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{1, 0}, validMoves, chessBoard, position, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{0, -1}, validMoves, chessBoard, position, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{-1, 0}, validMoves, chessBoard, position, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{ 1,  1}, validMoves, chessBoard, position, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{-1,  1}, validMoves, chessBoard, position, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{-1, -1}, validMoves, chessBoard, position, false);
        PieceMovesCalculator.validateMovesFromVector(new int[]{ 1, -1}, validMoves, chessBoard, position, false);

        return validMoves;
    }
}
