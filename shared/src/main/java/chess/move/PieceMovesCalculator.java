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
     * @param vec An integer array of size 2: {row offset, column offset}
     * @param validMoves A HashSet which will store the moves which have been validated.
     * @param board The board on which the pieces are arranged.
     * @param start The position the testing will begin.
     * @param maxLineLength The length of the line to be tested for (default BOARD_SIZE)
     * @param allowCapture A boolean value. True: allows movement into an enemy space. False: does not allow movement
     *                   into enemy space. True by default.
     * @param allowEmpty A boolean value. True: allows movement into an empty space. False: does not allow movement
     *                  into an empty space. True by default.
     * @param promotionPiece A boolean value. True: if this is a valid move, this move will promote the piece in
     *                       question. False: This move cannot promote a piece.
     */
    static void validateMovesFromVector(int[] vec, HashSet<ChessMove> validMoves, ChessBoard board,
                                        ChessPosition start, int maxLineLength, boolean allowCapture,
                                        boolean allowEmpty, boolean promotionPiece){
        int[] offset = new int[]{0, 0};
        int lineLength = 1;

        boolean validMove, stop = false;

        while(!stop && lineLength++ <= maxLineLength){
            offset[0]+=vec[0];
            offset[1]+=vec[1];
            var end = new ChessPosition(start.getRow()+offset[0], start.getColumn()+offset[1]);

            // if end position is not already in the board, it's invalid
            if(board.hasPosition(end)) break;

            if(board.getPiece(end) != null){
                // is a valid move if it's not null, but it is an enemy (and this validation allows capturing)
                validMove = allowCapture && board.getPiece(end).getTeamColor() != board.getPiece(start).getTeamColor();

                // if we hit something that's not null, we'll have to stop whether we add the move or not
                stop = true;

            // is a valid move if it's null, unless otherwise specified
            }else validMove = allowEmpty;

            if(validMove){
                // if the piece is a promotion piece, we need 4 separate moves added.
                if(promotionPiece){
                    validMoves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));

                // otherwise, just add the one
                }else{
                    validMoves.add(new ChessMove(start, end));
                }
            }
        }
    }
    static void validateMovesFromVector(int[] directionVector, HashSet<ChessMove> validMoves, ChessBoard board,
                                        ChessPosition startPosition, int maxLineLength){
        validateMovesFromVector(directionVector, validMoves, board, startPosition, maxLineLength, true, true, false);
    }
    static void validateMovesFromVector(int[] directionVector, HashSet<ChessMove> validMoves, ChessBoard board,
                                        ChessPosition startPosition){
        validateMovesFromVector(directionVector, validMoves, board, startPosition, ChessBoard.getBoardSize() -1, true, true, false);
    }

    /**
     *
     * @param type The type of chess piece to create.
     * @return An instance of the correct implementing class for the given piece type.
     */
    static PieceMovesCalculator createInstance(ChessPiece.PieceType type){
        return switch(type){
            case KING -> new KingMovesCalculator();
            case QUEEN -> new QueenMovesCalculator();
            case BISHOP -> new BishopMovesCalculator();
            case KNIGHT -> new KnightMovesCalculator();
            case ROOK -> new RookMovesCalculator();
            case PAWN -> new PawnMovesCalculator();
        };
    }

/*
########## INSTANCE / NEED IMPLEMENTATION ##########
 */
    Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position);
}
