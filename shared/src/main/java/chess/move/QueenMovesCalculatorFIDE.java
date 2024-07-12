package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;


public class QueenMovesCalculatorFIDE extends PieceMovesCalculatorFIDE {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        var possibleMoves = new HashSet<ChessMove>();

        possibleMoves.addAll(new RookMovesCalculatorFIDE().pieceMoves(chessBoard, position));
        possibleMoves.addAll(new BishopMovesCalculatorFIDE().pieceMoves(chessBoard, position));

        return possibleMoves;
    }
}
