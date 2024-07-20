package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;


public
class QueenMovesCalculator extends PieceMovesCalculator {
    @Override
    public
    Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position) {
        var possibleMoves = new HashSet<ChessMove>();

        possibleMoves.addAll(new RookMovesCalculator().pieceMoves(chessBoard, position));
        possibleMoves.addAll(new BishopMovesCalculator().pieceMoves(chessBoard, position));

        return possibleMoves;
    }
}
