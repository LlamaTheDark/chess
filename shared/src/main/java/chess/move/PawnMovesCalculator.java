package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator extends PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        var validMoves = new HashSet<ChessMove>();

        /*
        pawnData has 3 parameters: [0] indicates the direction marked as "forward" for the pawn
                                   [1] indicates which row is this pawn's starting row.
                                   [2] indicates the other's side's pawn row (for promotion pieces)
         */
        int[] pawnData = switch(chessBoard.getPiece(position).getTeamColor()) {
            case WHITE -> new int[]{1, 2, 7};
            case BLACK -> new int[]{-1, 7, 2};
        };

        int maxForwardLength; boolean promotionPiece = false;
        if(position.getRow() == pawnData[1]){
            // if the pawn's row is his starting row, he can move forward up to 2 squares
            maxForwardLength = 2;
        }else{
            maxForwardLength = 1;

            // if this pawn is in his opponent's starting row, his next valid move will promote him.
            if(position.getRow() == pawnData[2]) promotionPiece = true;
        }

        // move forward
        PieceMovesCalculator.validateMovesFromVector(new int[]{pawnData[0], 0}, validMoves, chessBoard, position, maxForwardLength, false, true, promotionPiece);

        // capture diagonally
        PieceMovesCalculator.validateMovesFromVector(new int[]{pawnData[0], 1}, validMoves, chessBoard, position, 1, true, false, promotionPiece);
        PieceMovesCalculator.validateMovesFromVector(new int[]{pawnData[0], -1}, validMoves, chessBoard, position, 1, true, false, promotionPiece);

        return validMoves;
    }
}
