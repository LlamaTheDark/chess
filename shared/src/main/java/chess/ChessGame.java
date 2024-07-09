package chess;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;

/*
TODO: outline pre- and post-conditions for relevant functions
 */

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private class GameState {
        TeamColor teamTurn = TeamColor.WHITE;
        ChessBoard board;

        EnumMap<TeamColor, Boolean> isChecked = new EnumMap<>(TeamColor.class);
        {
            isChecked.put(TeamColor.BLACK, false);
            isChecked.put(TeamColor.WHITE, false);
        }

        public GameState(ChessBoard board) {
            this.board = board;
        }
    }

    ChessGame.GameState state;

    public ChessGame() {
        state = new GameState(new ChessBoard());
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() { return state.teamTurn; }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) { state.teamTurn = team; }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        var validMoves = new HashSet<ChessMove>();
        var moves = state.board.getPiece(startPosition).pieceMoves(state.board, startPosition);

        /*
        The following verifications are inherited from pieceMoves():
        (1) the moves are not out of bounds
        (2) the moves follow the move-patterns of each piece

        one approach to realize hypothetical moves is to create a copy of the board:
        for each move in moves:
            boardCopy = copyOf(board)
            make the move on the board copy
            not boardCopy.isInCheck(your team)?
                add move to validMoves

        seems like a huge hassle of a way to do it, maybe there's a better way
         */

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        /*
        VALIDATE MOVE
        The following should already be verified by validMoves():
        (1) invalid if the king is left in check
        (2) invalid if the piece cannot move there

        Checks left to make in this function:
        (1) is it your turn?

        Implementation:
        is gameTurn = yourColor?
        is move not in validMoves?
            throw exception

        MAKE MOVE
        board.addPiece(move.end, board.get(board.start))
        board.addPiece(move.start, null)
         */
        var validMoves = validMoves(move.getStartPosition());
        if(!(state.board.getPiece(move.getStartPosition()).getTeamColor() == state.teamTurn)
            || (validMoves != null && !validMoves.contains(move))) throw new InvalidMoveException();

        state.board.addPiece(move.getEndPosition(), state.board.getPiece(move.getStartPosition()));
        state.board.addPiece(move.getStartPosition(), null);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        /*
        for each position in the chess board:
            get the possible positions
            if the opposing king is in the set of those moves:
                that king is in check

         */

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        /*
        (validMoves is null if the piece cannot not leave the king in check, therefore)
        if validMoves(friendlyPosition) is null for ALL friendly pieces:
            return true

        ooor
        for all friendlyPos:
            if validMoves(friendlyPosition) != null:
                return false
        return true
         */
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        state.board = board;

    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() { return state.board; }
}
