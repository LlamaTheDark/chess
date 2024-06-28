package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;


public class QueenMovesCalculator implements PieceMovesCalculator {
    final HashSet<ChessMove> VALID_MOVES = new HashSet<>();

    /**
     * Loops through each position along the line of the given direction vector from the initalPosition.
     * @param directionVector An integer array of size 2: {row offset, column offset}
     * @param board The board on which the pieces are arranged.
     * @param startPosition The position the testing will begin.
     */
    private void validateDirection(int[] directionVector, ChessBoard board, ChessPosition startPosition){
        /*
        test each position in position + directionVector until ya hit something.
         */
        int rowOffset = 0, colOffset = 0;
        boolean validMove, stop = false;
        while(!stop){
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

            if (validMove) this.VALID_MOVES.add(new ChessMove(startPosition, endPosition));
        }
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        validateDirection(new int[]{0, 1}, chessBoard, position);
        validateDirection(new int[]{1, 0}, chessBoard, position);
        validateDirection(new int[]{0, -1}, chessBoard, position);
        validateDirection(new int[]{-1, 0}, chessBoard, position);
        validateDirection(new int[]{ 1,  1}, chessBoard, position);
        validateDirection(new int[]{-1,  1}, chessBoard, position);
        validateDirection(new int[]{-1, -1}, chessBoard, position);
        validateDirection(new int[]{ 1, -1}, chessBoard, position);

        return VALID_MOVES;
    }
}
