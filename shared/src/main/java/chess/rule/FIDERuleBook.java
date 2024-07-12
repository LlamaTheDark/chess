package chess.rule;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * The internationally recognized standard rules of chess.
 * @see <a href="https://en.wikipedia.org/wiki/Rules_of_chess#:~:text=Today%2C%20the%20standard%20rules%20are%20set%20by%20FIDE%20(F%C3%A9d%C3%A9ration%20Internationale%20des%20%C3%89checs)%2C%20the%20international%20governing%20body%20for%20chess.">
 *     Wikipedia: Rules of Chess
 *     </a>
 */
public class FIDERuleBook implements ChessRuleBook {
    private static final int REGULATION_BOARD_SIZE = 8;

    public FIDERuleBook(){ }
    public int getRegulationBoardSize(){ return REGULATION_BOARD_SIZE; }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition start, ChessBoard board) {
        if (board.getPiece(start) == null) return null;

        var validMoves = new HashSet<ChessMove>();
        var moves = board.getPiece(start).pieceMoves(board, start);

        for(var move : moves) {
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

            if (!isInCheck(startPiece.getTeamColor(), board)) validMoves.add(move);

            // reverse the move
            board.addPiece(move.getStartPosition(), startPiece);
            board.addPiece(move.getEndPosition(), oldEndPiece);
        }
        return validMoves;
    }

    @Override
    public boolean isInCheck(ChessGame.TeamColor teamColor, ChessBoard board) {
        var positions = board.positionIterator();
        while(positions.hasNext()) {
            var position = positions.next();
            var piece = board.getPiece(position);
            if(piece == null) continue;
            if(piece.getTeamColor() == teamColor) continue;

            for(var move : piece.pieceMoves(board, position)) {
                var endPiece = board.getPiece(move.getEndPosition());
                if (endPiece != null
                        && endPiece.getPieceType().equals(ChessPiece.PieceType.KING)
                        && endPiece.getTeamColor().equals(teamColor)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInCheckmate(ChessGame.TeamColor teamColor, ChessBoard board) {
        return getEndState(teamColor, board) == EndState.CHECKMATE;
    }

    @Override
    public boolean isInStalemate(ChessGame.TeamColor teamColor, ChessBoard board) {
        return getEndState(teamColor, board) == EndState.STALEMATE;
    }

    @Override
    public boolean isBoardValid(ChessBoard board) {
        return ChessBoard.getBoardSize() == 8;
    }

    /**
     * Determines whether any pieces on a team can make moves and returns the respective boolean.
     * @param teamColor The team to search for moves from.
     * @return Whether this team can make any valid moves.
     */
    private boolean moves(ChessGame.TeamColor teamColor, ChessBoard board){
        var positions = board.positionIterator();
        while(positions.hasNext()) {
            var position = positions.next();
            var piece = board.getPiece(position);
            if(piece == null) continue;
            if(piece.getTeamColor() != teamColor) continue;

            if(!validMoves(position, board).isEmpty()) return true;
        }
        return false;
    }

    /**
     * Determines whether the team specified is in checkmate or stalemate.
     * @param teamColor The team to determine state for.
     * @param board The board on which the game is played.
     * @return <ul>
     *     <li>
     *         {@link EndState#CHECKMATE} if the team can make no moves and the team's king is in check.
     *     </li>
     *     <li>
     *          {@link EndState#STALEMATE} if the team can make no moves and the team's king is <b>not</b> in check.
     *     </li>
     *     </ul>
     */
    private EndState getEndState(ChessGame.TeamColor teamColor, ChessBoard board) {
        boolean moves = moves(teamColor, board), isInCheck = isInCheck(teamColor, board);

        if(!moves && isInCheck) return EndState.CHECKMATE;
        else if(!moves /* && !isInCheck */) return EndState.STALEMATE;
        return null;
    }
}
