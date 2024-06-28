package chess.move;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        var validMoves = new HashSet<ChessMove>();

        /*
        pawnData has 3 parameters: [0] indicates the direction marked as "foward" for the pawn
                                   [1] indicates which row is this pawn's starting row.
                                   [2] indicates the other's side's pawn row (for promotion pieces)
         */
        int[] pawnData = (chessBoard.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE)
                ? new int[]{ 1, 2, 7}
                : new int[]{-1, 7, 2};
        /*
        If the pawn is in the starting location, he can move two ahead of him.
        Otherwise, he can move one ahead of him.

        If there is an enemy in front of him, he cannot take it. He only captures diagonally forward.

        also remember to take into account whether it's a white or black pawn
         */

        int maxForwardLength; boolean promotionPiece = false;
        if(position.getRow() == pawnData[1]){
            maxForwardLength = 2;
        }else{
            maxForwardLength = 1;
            if(position.getRow() == pawnData[2]) promotionPiece = true;
        }
        PieceMovesCalculator.validateMovesFromVector(new int[]{pawnData[0], 0}, validMoves, chessBoard, position, maxForwardLength, false, true, promotionPiece);

        PieceMovesCalculator.validateMovesFromVector(new int[]{pawnData[0], 1}, validMoves, chessBoard, position, 1, true, false, promotionPiece);
        PieceMovesCalculator.validateMovesFromVector(new int[]{pawnData[0], -1}, validMoves, chessBoard, position, 1, true, false, promotionPiece);

        return validMoves;
    }
}
