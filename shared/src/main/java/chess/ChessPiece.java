package chess;

import chess.move.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() { return this.pieceColor; }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() { return this.type; }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch(board.getPiece(myPosition).getPieceType()){
            case KING -> new KingMovesCalculator().pieceMoves(board, myPosition);
            case QUEEN -> new QueenMovesCalculator().pieceMoves(board, myPosition);
            case BISHOP -> new BishopMovesCalculator().pieceMoves(board, myPosition);
            case KNIGHT -> new KnightMovesCalculator().pieceMoves(board, myPosition);
            case ROOK -> new RookMovesCalculator().pieceMoves(board, myPosition);
            case PAWN -> new PawnMovesCalculator().pieceMoves(board, myPosition);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }
}
