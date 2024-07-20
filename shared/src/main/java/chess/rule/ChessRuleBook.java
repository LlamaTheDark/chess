package chess.rule;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public
interface ChessRuleBook {
    Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board);

    boolean isInCheck(ChessGame.TeamColor teamColor, ChessBoard board);

    boolean isInCheckmate(ChessGame.TeamColor teamColor, ChessBoard board);

    boolean isInStalemate(ChessGame.TeamColor teamColor, ChessBoard board);

    boolean isBoardValid(ChessBoard board);

    enum EndState {
        CHECKMATE,
        STALEMATE
    }
}