package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public interface PieceMovesCalculator {
/*
########## STATIC ##########
 */
    /**
     * Loops through each position along the line of the given direction vector from the initial position and
     * adds valid moves to the move set.
     * @param directionVector An integer array of size 2: {row offset, column offset}
     * @param validMoves A HashSet which will store the moves which have been validated.
     * @param board The board on which the pieces are arranged.
     * @param startPosition The position the testing will begin.
     * @param maxLineLength The length of the line to be tested for (default BOARD_SIZE)
     * @param canCapture A boolean value. True: allows movement into an enemy space. False: does not allow movement
     *                   into enemy space. True by default.
     * @param allowNull A boolean value. True: allows movement into an empty space. False: does not allow movement
     *                  into an empty space. True by default.
     * @param promotionPiece A boolean value. True: if this is a valid move, this move will promote the piece in
     *                       question. False: This move cannot promote a piece.
     */
    static void validateMovesFromVector(int[] directionVector, HashSet<ChessMove> validMoves, ChessBoard board,
                                        ChessPosition startPosition, int maxLineLength, boolean canCapture,
                                        boolean allowNull, boolean promotionPiece){
        /*
        test each position in position + directionVector until ya hit something.
         */
        int rowOffset = 0, colOffset = 0, lineLength = 1;
        boolean validMove, stop = false;
        do{
            rowOffset+=directionVector[0];
            colOffset+=directionVector[1];
            var endPosition = new ChessPosition(startPosition.getRow()+rowOffset,
                    startPosition.getColumn()+colOffset);

            // if rows or columns are out of bounds, break the loop
            if(endPosition.getRow() > ChessBoard.getBoardSize() || endPosition.getColumn() > ChessBoard.getBoardSize()
                    || endPosition.getRow() < 1 || endPosition.getColumn() < 1) break;

            if(board.getPiece(endPosition) != null){
                // is a valid move if it's not null, but it is an enemy (and this validation allows capturing)
                validMove = canCapture &&
                        board.getPiece(endPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor();

                // if we hit something that's not null, we'll have to stop whether we add the move or not
                stop = true;

            // is a valid move if it's null, unless otherwise specified
            }else validMove = allowNull;

            if(validMove){
                // if the piece is a promotion piece, we need 4 separate moves added.
                if(promotionPiece){
                    validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));

                // otherwise, just add the one
                }else{
                    validMoves.add(new ChessMove(startPosition, endPosition));
                }
            }
        } while(!stop && (lineLength++ < maxLineLength));
    }
    static void validateMovesFromVector(int[] directionVector, HashSet<ChessMove> validMoves, ChessBoard board,
                                        ChessPosition startPosition, int maxLineLength){
        validateMovesFromVector(directionVector, validMoves, board, startPosition, maxLineLength, true, true, false);
    }
    static void validateMovesFromVector(int[] directionVector, HashSet<ChessMove> validMoves, ChessBoard board,
                                        ChessPosition startPosition){
        validateMovesFromVector(directionVector, validMoves, board, startPosition, ChessBoard.getBoardSize() -1, true, true, false);
    }
/*
########## INSTANCE / NEED IMPLEMENTATION ##########
 */
    Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position);
}
