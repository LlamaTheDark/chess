package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        //TODO: include castling
        var validMoves = new HashSet<ChessMove>();

        PieceMovesCalculator.validateMovesFromVector(new int[]{0, 1}, validMoves, chessBoard, position);
        PieceMovesCalculator.validateMovesFromVector(new int[]{1, 0}, validMoves, chessBoard, position);
        PieceMovesCalculator.validateMovesFromVector(new int[]{0, -1}, validMoves, chessBoard, position);
        PieceMovesCalculator.validateMovesFromVector(new int[]{-1, 0}, validMoves, chessBoard, position);

        return validMoves;
    }
}
