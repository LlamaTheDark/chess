package chess;

import java.util.HashMap;

import chess.ChessGame.TeamColor;
import chess.ChessPiece.PieceType;
/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    /**
     * The length/width of the chess board.
     */
    public static final int BOARD_SIZE = 8;

    /**
     * A HashMap which maps chess positions to chess pieces.
     */
    private final HashMap<ChessPosition, ChessPiece> BOARD = new HashMap<>(BOARD_SIZE * BOARD_SIZE);

    /**
     * A Hashmap which maps chess positions to chess pieces. This is instantiated with the default staring positions for
     * a standard game of chess.
     */
    private static final HashMap<ChessPosition, ChessPiece> DEFAULT_BOARD = new HashMap<>(BOARD_SIZE * BOARD_SIZE);
    static {
        DEFAULT_BOARD.put(new ChessPosition(1, 1), new ChessPiece(TeamColor.WHITE, PieceType.ROOK));
        DEFAULT_BOARD.put(new ChessPosition(1, 2), new ChessPiece(TeamColor.WHITE, PieceType.KNIGHT));
        DEFAULT_BOARD.put(new ChessPosition(1, 3), new ChessPiece(TeamColor.WHITE, PieceType.BISHOP));
        DEFAULT_BOARD.put(new ChessPosition(1, 4), new ChessPiece(TeamColor.WHITE, PieceType.QUEEN));
        DEFAULT_BOARD.put(new ChessPosition(1, 5), new ChessPiece(TeamColor.WHITE, PieceType.KING));
        DEFAULT_BOARD.put(new ChessPosition(1, 6), new ChessPiece(TeamColor.WHITE, PieceType.BISHOP));
        DEFAULT_BOARD.put(new ChessPosition(1, 7), new ChessPiece(TeamColor.WHITE, PieceType.KNIGHT));
        DEFAULT_BOARD.put(new ChessPosition(1, 8), new ChessPiece(TeamColor.WHITE, PieceType.ROOK));
        for(int i = 1; i <= ChessBoard.BOARD_SIZE; i++){
            DEFAULT_BOARD.put(new ChessPosition(2, i), new ChessPiece(TeamColor.WHITE, PieceType.PAWN));
        }
        for(int i = 3; i <= 6; i++){
            for(int j = 1; j <= ChessBoard.BOARD_SIZE; j++){
                DEFAULT_BOARD.put(new ChessPosition(i, j), null);
            }
        }
        for(int i = 1; i <= ChessBoard.BOARD_SIZE; i++){
            DEFAULT_BOARD.put(new ChessPosition(7, i), new ChessPiece(TeamColor.BLACK, PieceType.PAWN));
        }
        DEFAULT_BOARD.put(new ChessPosition(8, 1), new ChessPiece(TeamColor.BLACK, PieceType.ROOK));
        DEFAULT_BOARD.put(new ChessPosition(8, 2), new ChessPiece(TeamColor.BLACK, PieceType.KNIGHT));
        DEFAULT_BOARD.put(new ChessPosition(8, 3), new ChessPiece(TeamColor.BLACK, PieceType.BISHOP));
        DEFAULT_BOARD.put(new ChessPosition(8, 4), new ChessPiece(TeamColor.BLACK, PieceType.QUEEN));
        DEFAULT_BOARD.put(new ChessPosition(8, 5), new ChessPiece(TeamColor.BLACK, PieceType.KING));
        DEFAULT_BOARD.put(new ChessPosition(8, 6), new ChessPiece(TeamColor.BLACK, PieceType.BISHOP));
        DEFAULT_BOARD.put(new ChessPosition(8, 7), new ChessPiece(TeamColor.BLACK, PieceType.KNIGHT));
        DEFAULT_BOARD.put(new ChessPosition(8, 8), new ChessPiece(TeamColor.BLACK, PieceType.ROOK));
    }


    public ChessBoard() {
        for(int row = 1; row <= BOARD_SIZE; row++){
            for(int col = 1; col <= BOARD_SIZE; col++) {
                BOARD.put(new ChessPosition(row, col), null);
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        BOARD.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return BOARD.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.BOARD.putAll(DEFAULT_BOARD);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return BOARD.equals(that.BOARD);
    }

    @Override
    public int hashCode() {
        return BOARD.hashCode();
    }
}
