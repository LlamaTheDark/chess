package chess.rule;

import chess.*;
import chess.move.PieceMovesCalculator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The internationally recognized standard rules of chess.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Rules_of_chess#:~:text=Today%2C%20the%20standard%20rules%20are%20set%20by
 * %20FIDE%20(F%C3%A9d%C3%A9ration%20Internationale%20des%20%C3%89checs)
 * %2C%20the%20international%20governing%20body%20for%20chess."> Wikipedia: Rules of Chess
 * </a>
 */
public
class FIDERuleBook implements ChessRuleBook {
    private static final int REGULATION_BOARD_SIZE = 8;

    public
    FIDERuleBook() {}

    /**
     * Determines whether any pieces on a team can make moves and returns the respective boolean.
     *
     * @param teamColor The team to search for moves from.
     *
     * @return Whether this team can make any valid moves.
     */
    private
    boolean moves(ChessGame.TeamColor teamColor, ChessBoard board) {
        var positions = board.positionIterator();
        while (positions.hasNext()) {
            var position = positions.next();
            var piece = board.getPiece(position);
            if (piece == null) {continue;}
            if (piece.getTeamColor() != teamColor) {continue;}

            if (!validMoves(position, board).isEmpty()) {return true;}
        }
        return false;
    }

    /**
     * Determines whether the team specified is in checkmate or stalemate.
     *
     * @param teamColor The team to determine state for.
     * @param board     The board on which the game is played.
     *
     * @return <ul>
     * <li>
     * {@link EndState#CHECKMATE} if the team can make no moves and the team's king is in check.
     * </li>
     * <li>
     * {@link EndState#STALEMATE} if the team can make no moves and the team's king is <b>not</b> in check.
     * </li>
     * </ul>
     */
    private
    EndState getEndState(ChessGame.TeamColor teamColor, ChessBoard board) {
        boolean moves = moves(teamColor, board), isInCheck = isInCheck(teamColor, board);

        if (!moves && isInCheck) {return EndState.CHECKMATE;} else if (!moves /* && !isInCheck */) {
            return EndState.STALEMATE;
        }
        return null;
    }

    /**
     * Gets a collection of valid moves for a piece at the given location. A valid move is one that meets the following
     * criteria:
     * <ol>
     *     <li> The move exists within the board. </li>
     *     <li> The move follows appropriate move patterns for each piece. </li>
     *     <li> The move does not leave the team's king in check. </li>
     * </ol>
     * Criteria <b>1.</b> and <b>2.</b> are provided for by {@link PieceMovesCalculator}. Thus, this function only
     * tests for
     * the same king's check state.
     *
     * @param start the piece to get valid moves for
     * @param board the board the game takes place on.
     *
     * @return Set of valid moves for requested piece, or <code>null</code> if there is no piece at
     * <code>startPosition</code>
     */
    @Override
    public
    Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board) {
        if (board.getPiece(start) == null) {return null;}

        var validMoves = new HashSet<ChessMove>();
        var moves = board.getPiece(start).pieceMoves(board, start);

        for (var move : moves) {
            var oldEndPiece = board.getPiece(move.getEndPosition());
            var startPiece = board.getPiece(move.getStartPosition());

            // make the move
            board.addPiece(
                    move.getEndPosition(),
                    move.getPromotionPiece() != null
                    ? new ChessPiece(startPiece.getTeamColor(), move.getPromotionPiece())
                    : startPiece
            );
            board.addPiece(move.getStartPosition(), null);

            if (!isInCheck(startPiece.getTeamColor(), board)) {validMoves.add(move);}

            // reverse the move
            board.addPiece(move.getStartPosition(), startPiece);
            board.addPiece(move.getEndPosition(), oldEndPiece);
        }
        return validMoves;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @param board     the board on which the game takes place
     *
     * @return True if the specified team is in check
     */
    @Override
    public
    boolean isInCheck(ChessGame.TeamColor teamColor, ChessBoard board) {
        var positions = board.positionIterator();
        while (positions.hasNext()) {
            var position = positions.next();
            var piece = board.getPiece(position);
            if (piece == null) {continue;}
            if (piece.getTeamColor() == teamColor) {continue;}

            for (var move : piece.pieceMoves(board, position)) {
                var endPiece = board.getPiece(move.getEndPosition());
                if (endPiece != null
                    && endPiece.getPieceType().equals(ChessPiece.PieceType.KING)
                    && endPiece.getTeamColor().equals(teamColor)) {return true;}
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @param board     the board upon which the game takes place
     *
     * @return True if the specified team is in checkmate
     */
    @Override
    public
    boolean isInCheckmate(ChessGame.TeamColor teamColor, ChessBoard board) {
        return getEndState(teamColor, board) == EndState.CHECKMATE;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @param board     the board upon which the game takes place
     *
     * @return True if the specified team is in stalemate, otherwise false
     */
    @Override
    public
    boolean isInStalemate(ChessGame.TeamColor teamColor, ChessBoard board) {
        return getEndState(teamColor, board) == EndState.STALEMATE;
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables such as those
     * provided by {@link HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     *     an execution of a Java application, the {@code hashCode} method
     *     must consistently return the same integer, provided no information
     *     used in {@code equals} comparisons on the object is modified.
     *     This integer need not remain consistent from one execution of an
     *     application to another execution of the same application.
     * <li>If two objects are equal according to the {@link
     *     #equals(Object) equals} method, then calling the {@code
     *     hashCode} method on each of the two objects must produce the
     *     same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     *     according to the {@link #equals(Object) equals} method, then
     *     calling the {@code hashCode} method on each of the two objects
     *     must produce distinct integer results.  However, the programmer
     *     should be aware that producing distinct integer results for
     *     unequal objects may improve the performance of hash tables.
     * </ul>
     *
     * @return a hash code value for this object.
     *
     * @implSpec As far as is reasonably practical, the {@code hashCode} method defined by class {@code Object} returns
     * distinct integers for distinct objects.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public
    int hashCode() {
        return REGULATION_BOARD_SIZE * 13 * 27 + REGULATION_BOARD_SIZE;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * An equivalence relation partitions the elements it operates on
     * into <i>equivalence classes</i>; all the members of an
     * equivalence class are equal to each other. Members of an
     * equivalence class are substitutable for each other, at least
     * for some purposes.
     *
     * @param obj the reference object with which to compare.
     *
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     *
     * @implSpec The {@code equals} method for class {@code Object} implements the most discriminating possible
     * equivalence relation on objects; that is, for any non-null reference values {@code x} and {@code y}, this method
     * returns {@code true} if and only if {@code x} and {@code y} refer to the same object ({@code x == y} has the
     * value {@code true}). In other words, under the reference equality equivalence relation, each equivalence class
     * only has a single element.
     * @apiNote It is generally necessary to override the {@link #hashCode() hashCode} method whenever this method is
     * overridden, to maintain the general contract for the {@code hashCode} method, which states that equal objects
     * must have equal hash codes.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public
    boolean equals(Object obj) {
        return obj.getClass() == this.getClass();
    }

    /**
     * Determines whether a board is valid according to FIDE regulation size (8).
     *
     * @param board The board in question.
     *
     * @return Whether the board is a valid board according to FIDE.
     */
    @Override
    public
    boolean isBoardValid(ChessBoard board) {
        return ChessBoard.getBoardSize() == 8;
    }
}
