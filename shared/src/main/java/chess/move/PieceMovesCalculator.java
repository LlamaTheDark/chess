package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public interface PieceMovesCalculator {
    // TODO: rename validateDirection to validateVector and give it another parameter (isLine: boolean)
    // ^ this should let the same basic function work for everything but the pawn
    // maybe turn validateVector into a static function and pass in VALID_MOVES as well?

    /**
     * Loops through each position along the line of the given direction vector from the initalPosition.
     * @param directionVector An integer array of size 2: {row offset, column offset}
     * @param validMoves A HashSet which will store the moves which have been validated.
     * @param board The board on which the pieces are arranged.
     * @param startPosition The position the testing will begin.
     * @param line A boolean value that tells whether to validate in a line in the direction of the given vector (true)
     *             or only once (false). True by default.
     */
    static void validateMovesFromVector(int[] directionVector, HashSet<ChessMove> validMoves, ChessBoard board,
                                        ChessPosition startPosition, boolean line){
        /*
        test each position in position + directionVector until ya hit something.
         */
        int rowOffset = 0, colOffset = 0;
        boolean validMove, stop = false;
        do{
            rowOffset+=directionVector[0];
            colOffset+=directionVector[1];
            var endPosition = new ChessPosition(startPosition.getRow()+rowOffset,
                    startPosition.getColumn()+colOffset);

            // if rows or columns are out of bounds, break the loop
            if(endPosition.getRow() > ChessBoard.BOARD_SIZE || endPosition.getColumn() > ChessBoard.BOARD_SIZE
                    || endPosition.getRow() < 1 || endPosition.getColumn() < 1) break;

            if(board.getPiece(endPosition) != null){
                // is a valid move if it's not null, but it is an enemy
                validMove = board.getPiece(endPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor();

                // if we hit something that's not null, we'll have to stop whether we add the move or not
                stop = true;

                // is a valid move if it's null
            }else validMove = true;

            if (validMove) validMoves.add(new ChessMove(startPosition, endPosition));
        } while(!stop && line);
    }
    static void validateMovesFromVector(int[] directionVector, HashSet<ChessMove> validMoves, ChessBoard board,
                                        ChessPosition startPosition){
        validateMovesFromVector(directionVector, validMoves, board, startPosition, true);
    }

    Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position);
}
