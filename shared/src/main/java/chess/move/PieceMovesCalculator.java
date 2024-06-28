package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public interface PieceMovesCalculator {
    enum PositionType {
        NULL,
        CAPTURE,
        FRIEND,
    }
    static PositionType getPositionType(ChessBoard board, ChessPosition start, ChessPosition end) {
        ChessPiece endPiece = board.getPiece(end);
        if(endPiece == null) return PositionType.NULL;
        else if(endPiece.getTeamColor() != board.getPiece(start).getTeamColor()) return PositionType.CAPTURE;
        else return PositionType.FRIEND;
    }
    Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position);
}
