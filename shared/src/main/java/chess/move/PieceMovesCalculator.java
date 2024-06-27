package chess.move;

import chess.ChessBoard;
import chess.ChessPosition;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessPosition> pieceMoves(ChessBoard chessBoard, ChessPosition position);
}
