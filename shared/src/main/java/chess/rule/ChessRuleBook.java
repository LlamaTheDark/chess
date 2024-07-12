package chess.rule;

import chess.*;

import java.util.Collection;

public interface ChessRuleBook {
    enum EndState {
        CHECKMATE,
        STALEMATE
    }

    Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board);

    boolean isInCheck(ChessGame.TeamColor teamColor, ChessBoard board);

    boolean isInCheckmate(ChessGame.TeamColor teamColor, ChessBoard board);

    boolean isInStalemate(ChessGame.TeamColor teamColor, ChessBoard board);

    boolean isBoardValid(ChessBoard board);
}