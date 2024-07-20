package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter signature of the existing methods.
 */
public
class ChessMove {
    /*
    ########## INSTANCE ##########
     */
    private final ChessPosition        START_POSITION;
    private final ChessPosition        END_POSITION;
    private final ChessPiece.PieceType PROMOTION_PIECE;

    public
    ChessMove(ChessPosition startPosition, ChessPosition endPosition,
              ChessPiece.PieceType promotionPiece) {
        this.START_POSITION = startPosition;
        this.END_POSITION = endPosition;
        this.PROMOTION_PIECE = promotionPiece;
    }

    public
    ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        this(startPosition, endPosition, null);
    }

    /**
     * @return ChessPosition of starting location
     */
    public
    ChessPosition getStartPosition() {
        return START_POSITION;
    }

    /**
     * @return ChessPosition of ending location
     */
    public
    ChessPosition getEndPosition() {
        return END_POSITION;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public
    ChessPiece.PieceType getPromotionPiece() {
        return PROMOTION_PIECE;
    }

    @Override
    public
    int hashCode() {
        return Objects.hash(START_POSITION, END_POSITION, PROMOTION_PIECE);
    }

    @Override
    public
    boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(START_POSITION, chessMove.START_POSITION) &&
               Objects.equals(END_POSITION, chessMove.END_POSITION) && PROMOTION_PIECE == chessMove.PROMOTION_PIECE;
    }
}
