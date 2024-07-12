package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public abstract class PieceMovesCalculatorFIDE {
/*
########## STATIC ##########
 */
    /**
     * Loops through each position along the line of the given direction vector from the initial position and
     * adds valid moves to the move set.
     * <p>
     * While this function has no return value, it modifies the passed HashSet to include all possible moves for the
     * piece at the position specified. Possible moves are moves that meet the following requirements:
     * <ol>
     *     <li>The end position of the move exists in the provided <code>ChessBoard</code></li>
     *     <li>
     *         The move follows the movement behaviors specified by the passed arguments.
     *         <ul>
     *             <li>
     *                 e.g. a <i>Rook</i> moves orthogonally, up to the length of the board, can capture pieces,
     *                 and has no promotions.
 *                 </li>
     *         </ul>
     *     </li>
     * </ol>
     * @param vec An integer array of size 2: {row offset, column offset}
     * @param possibleMoves A HashSet which will store the moves which have been validated.
     * @param board The board on which the pieces are arranged.
     * @param start The position the testing will begin.
     * @param maxLength The length of the line to be tested for (default BOARD_SIZE)
     * @param capture A boolean value. True: allows movement into an enemy space. False: does not allow movement
     *                   into enemy space. True by default.
     * @param empty A boolean value. True: allows movement into an empty space. False: does not allow movement
     *                  into an empty space. True by default.
     * @param promotion A boolean value. True: if this is a valid move, this move will promote the piece in
     *                       question. False: This move cannot promote a piece.
     */
    protected static void addAttackRange(int[] vec, HashSet<ChessMove> possibleMoves, ChessBoard board,
                                         ChessPosition start, int maxLength, boolean capture,
                                         boolean empty, boolean promotion){
        int[] offset = new int[]{0, 0};
        int length = 1;

        boolean validMove, stop = false;

        while(!stop && length++ <= maxLength){
            offset[0]+=vec[0];
            offset[1]+=vec[1];
            var end = new ChessPosition(start.getRow()+offset[0], start.getColumn()+offset[1]);

            // if end position is not already in the board, it's invalid
            if(!board.hasPosition(end)) break;

            if(board.getPiece(end) != null){
                // is a valid move if it's not null, but it is an enemy (and this validation allows capturing)
                validMove = capture && board.getPiece(end).getTeamColor() != board.getPiece(start).getTeamColor();

                // if we hit something that's not null, we'll have to stop whether we add the move or not
                stop = true;

            // is a valid move if it's null, unless otherwise specified
            }else validMove = empty;

            if(validMove){
                // if the piece is a promotion piece, we need 4 separate moves added.
                if(promotion){
                    possibleMoves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));

                // otherwise, just add the one
                }else{
                    possibleMoves.add(new ChessMove(start, end));
                }
            }
        }
    }

    /**
     *
     * @param type The type of chess piece whose move calculator will be returned.
     * @return An instance of the correct move-calculating class for the given piece type.
     */
    public static PieceMovesCalculatorFIDE createInstance(ChessPiece.PieceType type){
        return switch(type){
            case KING -> new KingMovesCalculatorFIDE();
            case QUEEN -> new QueenMovesCalculatorFIDE();
            case BISHOP -> new BishopMovesCalculatorFIDE();
            case KNIGHT -> new KnightMovesCalculatorFIDE();
            case ROOK -> new RookMovesCalculatorFIDE();
            case PAWN -> new PawnMovesCalculatorFIDE();
        };
    }

/*
########## INSTANCE / NEED IMPLEMENTATION ##########
 */
    public abstract Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position);
}
