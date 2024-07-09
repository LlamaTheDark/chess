package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator extends PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        var possibleMoves = new HashSet<ChessMove>();

        PieceMovesCalculator.addPossibleMoves(new int[]{0, 1}, possibleMoves, chessBoard, position, 1,true, true, false);
        PieceMovesCalculator.addPossibleMoves(new int[]{1, 0}, possibleMoves, chessBoard, position, 1,true, true, false);
        PieceMovesCalculator.addPossibleMoves(new int[]{0, -1}, possibleMoves, chessBoard, position, 1,true, true, false);
        PieceMovesCalculator.addPossibleMoves(new int[]{-1, 0}, possibleMoves, chessBoard, position, 1,true, true, false);
        PieceMovesCalculator.addPossibleMoves(new int[]{ 1,  1}, possibleMoves, chessBoard, position, 1,true, true, false);
        PieceMovesCalculator.addPossibleMoves(new int[]{-1,  1}, possibleMoves, chessBoard, position, 1,true, true, false);
        PieceMovesCalculator.addPossibleMoves(new int[]{-1, -1}, possibleMoves, chessBoard, position, 1,true, true, false);
        PieceMovesCalculator.addPossibleMoves(new int[]{ 1, -1}, possibleMoves, chessBoard, position, 1,true, true, false);

        return possibleMoves;
    }
}
