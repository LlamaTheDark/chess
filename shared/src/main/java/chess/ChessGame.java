package chess;

import chess.rule.ChessRuleBook;
import chess.rule.FIDERuleBook;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter signature of the existing methods.
 */
public
class ChessGame {

    private final ChessGame.GameState STATE;
    private final ChessRuleBook       ruleBook;

    public
    ChessGame() {
        this(new FIDERuleBook());
    }

    public
    ChessGame(ChessRuleBook ruleBook) {
        STATE = new GameState(new ChessBoard(/* TODO: ruleBook.getRegulationBoardSize() */));
        this.ruleBook = ruleBook;
    }

    /**
     * @return Which team's turn it is
     */
    public
    TeamColor getTeamTurn() {return STATE.teamTurn;}

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public
    void setTeamTurn(TeamColor team) {STATE.teamTurn = team;}

    /**
     * @see ChessRuleBook#validMoves(ChessPosition, ChessBoard)
     */
    public
    Collection<ChessMove> validMoves(ChessPosition startPosition) {
        return getRuleBook().validMoves(startPosition, getBoard());
    }

    /**
     * Makes a move in a chess game.
     *
     * @param move chess move to preform
     *
     * @throws InvalidMoveException if move is invalid
     */
    public
    void makeMove(ChessMove move) throws InvalidMoveException {
        var validMoves = validMoves(move.getStartPosition());
        if (validMoves == null) throw new InvalidMoveException();

        var piece = STATE.board.getPiece(move.getStartPosition());

        if (!(piece.getTeamColor() == STATE.teamTurn)
            || !validMoves.contains(move)) throw new InvalidMoveException();

        STATE.board.addPiece(
                move.getEndPosition(),
                move.getPromotionPiece() != null
                ? new ChessPiece(STATE.teamTurn, move.getPromotionPiece())
                : piece
        );
        STATE.board.addPiece(move.getStartPosition(), null);

        // Switch turns
        this.setTeamTurn(STATE.teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     *
     * @return True if the specified team is in check
     */
    public
    boolean isInCheck(TeamColor teamColor) {
        return getRuleBook().isInCheck(teamColor, getBoard());
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     *
     * @return True if the specified team is in checkmate
     */
    public
    boolean isInCheckmate(TeamColor teamColor) {
        return getRuleBook().isInCheckmate(teamColor, getBoard());
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having no valid moves
     *
     * @param teamColor which team to check for stalemate
     *
     * @return True if the specified team is in stalemate, otherwise false
     */
    public
    boolean isInStalemate(TeamColor teamColor) {
        return getRuleBook().isInStalemate(teamColor, getBoard());
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public
    ChessBoard getBoard() {return STATE.board;}

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public
    void setBoard(ChessBoard board) {
        STATE.board = board;
    }

    public
    ChessRuleBook getRuleBook() {return ruleBook;}

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public
    enum TeamColor {
        WHITE,
        BLACK
    }

    private static
    class GameState {
        TeamColor  teamTurn = TeamColor.WHITE;
        ChessBoard board;

        public
        GameState(ChessBoard board) {
            this.board = board;
            this.board.resetBoard();
        }
    }
}
