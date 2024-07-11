package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

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

        HashMap<TeamColor, GameEndState> endStates = new HashMap<>(2);
        HashMap<TeamColor, ChessPosition> kingLocation = new HashMap<>(2);
        {
            kingLocation.put(TeamColor.WHITE, null);
            kingLocation.put(TeamColor.BLACK, null);
        }

        private enum GameEndState {
            CHECKMATE,
            STALEMATE
        }

        public GameState(ChessBoard board) {
            this.resetEndStates();
            this.board = board;
            this.board.resetBoard();
            this.refreshKingPositions();
        }

        /**
         * Determines whether any pieces on a team can make moves and returns the respective boolean.
         * @param teamColor The team to search for moves from.
         * @return Whether this team can make any valid moves.
         */
        private boolean moves(TeamColor teamColor){
            var positions = STATE.board.positionIterator();
            while(positions.hasNext()) {
                var position = positions.next();
                var piece = STATE.board.getPiece(position);
                if(piece == null) continue;
                if(piece.getTeamColor() != teamColor) continue;

                if(!validMoves(position).isEmpty()) return true;
            }
            return false;
        }
        private void resetEndStates(){
            endStates.put(TeamColor.WHITE, null);
            endStates.put(TeamColor.BLACK, null);
        }
        public void refreshGameEndStates(TeamColor teamColor){
            if(endStates.get(teamColor) != null) return;

            boolean moves = moves(teamColor), isInCheck = isInCheck(teamColor);

            if(!moves && isInCheck) {
                endStates.put(teamColor, GameEndState.CHECKMATE);
            }
            else if(!moves /* && !isInCheck */) {
                endStates.put(teamColor, GameEndState.STALEMATE);
            }else{
                resetEndStates();
            }
        }
        public void refreshKingPositions(){
            kingLocation.put(TeamColor.BLACK, null);
            kingLocation.put(TeamColor.WHITE, null);

            var positions = this.board.positionIterator();
            while(positions.hasNext()) {
                var position = positions.next();
                var piece = this.board.getPiece(position);
                if(piece == null) continue;

                if(piece.getPieceType() == ChessPiece.PieceType.KING) this.kingLocation.put(piece.getTeamColor(), position);
            }
        }
    }

    private final ChessGame.GameState STATE;

    public ChessGame() {
        STATE = new GameState(new ChessBoard());
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() { return STATE.teamTurn; }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) { STATE.teamTurn = team; }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a collection of valid moves for a piece at the given location. A valid move is one that meets the
     * following criteria:
     * <ol>
     *     <li> The move exists within the board. </li>
     *     <li> The move follows appropriate move patterns for each piece. </li>
     *     <li> The move does not leave the team's king in check. </li>
     * </ol>
     *
     * Criteria <b>1.</b> and <b>2.</b> are provided for by {@link chess.move.PieceMovesCalculator}. Thus, this function only tests for
     * the same king's check state.
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or <code>null</code> if there is no piece
     * at <code>startPosition</code>
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (STATE.board.getPiece(startPosition) == null) return null;

        var validMoves = new HashSet<ChessMove>();
        var moves = STATE.board.getPiece(startPosition).pieceMoves(STATE.board, startPosition);

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
        here's another approach:
        for each move in moves:
            make the move in the current game
            are we not in check?
                add to the validMoves list
            *reverse the move lol*
         */

        for(var move : moves) {
            var oldEndPiece = STATE.board.getPiece(move.getEndPosition());
            var startPiece = STATE.board.getPiece(move.getStartPosition());

            var isKing = startPiece.getPieceType() == ChessPiece.PieceType.KING;

            // make the move
            STATE.board.addPiece(
                    move.getEndPosition(),
                    move.getPromotionPiece() != null
                            ? new ChessPiece(STATE.teamTurn, move.getPromotionPiece())
                            : startPiece
            );
            STATE.board.addPiece(move.getStartPosition(), null);
            if(isKing) STATE.kingLocation.put(startPiece.getTeamColor(), move.getEndPosition());

            if(!isInCheck(startPiece.getTeamColor())) validMoves.add(move);

            // reverse the move
            STATE.board.addPiece(move.getStartPosition(), startPiece);
            STATE.board.addPiece(move.getEndPosition(), oldEndPiece);
            if(isKing) STATE.kingLocation.put(startPiece.getTeamColor(), move.getStartPosition());
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game.
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
        if(validMoves == null) throw new InvalidMoveException();

        var piece = STATE.board.getPiece(move.getStartPosition());

        if(!(piece.getTeamColor() == STATE.teamTurn)
            || !validMoves.contains(move)) throw new InvalidMoveException();

        STATE.board.addPiece(
                move.getEndPosition(),
                move.getPromotionPiece() != null
                        ? new ChessPiece(STATE.teamTurn, move.getPromotionPiece())
                        : piece
        );
        STATE.board.addPiece(move.getStartPosition(), null);

        if(piece.getPieceType() == ChessPiece.PieceType.KING) STATE.kingLocation.put(STATE.teamTurn, move.getEndPosition());

        // Switch turns
        this.setTeamTurn(STATE.teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
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
            get the possible moves
            if the opposing king is in the set of those moves:
                that king is in check

         */
        var positions = STATE.board.positionIterator();
        while(positions.hasNext()) {
            var position = positions.next();
            var piece = STATE.board.getPiece(position);
            if(piece == null) continue;
            if(piece.getTeamColor() == teamColor) continue;

            var possibleMoves = piece.pieceMoves(STATE.board, position);
            for(var move : possibleMoves) {
                if (move.getEndPosition().equals(STATE.kingLocation.get(teamColor))) return true;
            }
        }
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
        STATE.refreshGameEndStates(teamColor);
        return STATE.endStates.get(teamColor) == GameState.GameEndState.CHECKMATE;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        STATE.refreshGameEndStates(teamColor);
        return STATE.endStates.get(teamColor) == GameState.GameEndState.STALEMATE;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        STATE.board = board;
        STATE.refreshKingPositions();
        STATE.resetEndStates();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() { return STATE.board; }
}
