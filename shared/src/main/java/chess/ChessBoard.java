package chess;

import chess.ChessGame.TeamColor;
import chess.ChessPiece.PieceType;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter signature of the existing methods.
 */
public
class ChessBoard {

    /*
    ########## STATIC ##########
     */
    private static final int BOARD_SIZE = 8;

    /**
     * A Hashmap which maps chess positions to chess pieces. This is instantiated with the default starting positions
     * for a standard game of chess.
     */
    private static final LinkedHashMap<ChessPosition, ChessPiece> DEFAULT_BOARD =
            new LinkedHashMap<>(getBoardSize() * getBoardSize());

    private static final ChessPiece[][]                           DEFAULT_BOARD_ARR = {
            {
                    new ChessPiece(TeamColor.WHITE, PieceType.ROOK),
                    new ChessPiece(TeamColor.WHITE, PieceType.KNIGHT),
                    new ChessPiece(TeamColor.WHITE, PieceType.BISHOP),
                    new ChessPiece(TeamColor.WHITE, PieceType.QUEEN),
                    new ChessPiece(TeamColor.WHITE, PieceType.KING),
                    new ChessPiece(TeamColor.WHITE, PieceType.BISHOP),
                    new ChessPiece(TeamColor.WHITE, PieceType.KNIGHT),
                    new ChessPiece(TeamColor.WHITE, PieceType.ROOK)
            },
            {
                    new ChessPiece(TeamColor.WHITE, PieceType.PAWN),
                    new ChessPiece(TeamColor.WHITE, PieceType.PAWN),
                    new ChessPiece(TeamColor.WHITE, PieceType.PAWN),
                    new ChessPiece(TeamColor.WHITE, PieceType.PAWN),
                    new ChessPiece(TeamColor.WHITE, PieceType.PAWN),
                    new ChessPiece(TeamColor.WHITE, PieceType.PAWN),
                    new ChessPiece(TeamColor.WHITE, PieceType.PAWN),
                    new ChessPiece(TeamColor.WHITE, PieceType.PAWN)
            },
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {
                    new ChessPiece(TeamColor.BLACK, PieceType.PAWN),
                    new ChessPiece(TeamColor.BLACK, PieceType.PAWN),
                    new ChessPiece(TeamColor.BLACK, PieceType.PAWN),
                    new ChessPiece(TeamColor.BLACK, PieceType.PAWN),
                    new ChessPiece(TeamColor.BLACK, PieceType.PAWN),
                    new ChessPiece(TeamColor.BLACK, PieceType.PAWN),
                    new ChessPiece(TeamColor.BLACK, PieceType.PAWN),
                    new ChessPiece(TeamColor.BLACK, PieceType.PAWN)
            },
            {
                    new ChessPiece(TeamColor.BLACK, PieceType.ROOK),
                    new ChessPiece(TeamColor.BLACK, PieceType.KNIGHT),
                    new ChessPiece(TeamColor.BLACK, PieceType.BISHOP),
                    new ChessPiece(TeamColor.BLACK, PieceType.QUEEN),
                    new ChessPiece(TeamColor.BLACK, PieceType.KING),
                    new ChessPiece(TeamColor.BLACK, PieceType.BISHOP),
                    new ChessPiece(TeamColor.BLACK, PieceType.KNIGHT),
                    new ChessPiece(TeamColor.BLACK, PieceType.ROOK)
            }
    };

    /*
    Instantiates the DEFAULT_BOARD map.
     */
    static {
        DEFAULT_BOARD.put(new ChessPosition(1, 1), new ChessPiece(TeamColor.WHITE, PieceType.ROOK));
        DEFAULT_BOARD.put(new ChessPosition(1, 2), new ChessPiece(TeamColor.WHITE, PieceType.KNIGHT));
        DEFAULT_BOARD.put(new ChessPosition(1, 3), new ChessPiece(TeamColor.WHITE, PieceType.BISHOP));
        DEFAULT_BOARD.put(new ChessPosition(1, 4), new ChessPiece(TeamColor.WHITE, PieceType.QUEEN));
        DEFAULT_BOARD.put(new ChessPosition(1, 5), new ChessPiece(TeamColor.WHITE, PieceType.KING));
        DEFAULT_BOARD.put(new ChessPosition(1, 6), new ChessPiece(TeamColor.WHITE, PieceType.BISHOP));
        DEFAULT_BOARD.put(new ChessPosition(1, 7), new ChessPiece(TeamColor.WHITE, PieceType.KNIGHT));
        DEFAULT_BOARD.put(new ChessPosition(1, 8), new ChessPiece(TeamColor.WHITE, PieceType.ROOK));
        for (int i = 1; i <= ChessBoard.getBoardSize(); i++) {
            DEFAULT_BOARD.put(new ChessPosition(2, i), new ChessPiece(TeamColor.WHITE, PieceType.PAWN));
        }
        for (int i = 3; i <= 6; i++) {
            for (int j = 1; j <= ChessBoard.getBoardSize(); j++) {
                DEFAULT_BOARD.put(new ChessPosition(i, j), null);
            }
        }
        for (int i = 1; i <= ChessBoard.getBoardSize(); i++) {
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

    /**
     * A HashMap which maps chess positions to chess pieces.
     */
    private final LinkedHashMap<ChessPosition, ChessPiece> board = new LinkedHashMap<>(getBoardSize() * getBoardSize());

    /*
    ########## INSTANCE ##########
     */
    public
    ChessBoard() {
        for (int row = 1; row <= getBoardSize(); row++) {
            for (int col = 1; col <= getBoardSize(); col++) {
                board.put(new ChessPosition(row, col), null);
            }
        }
    }

    public
    ChessBoard(ChessBoard toCopy) {
        this.board.putAll(toCopy.board);
    }

    /**
     * The length/width of the chess board.
     */
    public static
    int getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * Tests to see if a ChessPosition exists in the BOARD HashMap.
     *
     * @param position The position in question.
     *
     * @return Whether position is in the board.<br><code>true</code>: yes, position is in the
     * board.<br><code>false</code>: no, position is not in the board.
     */
    public
    boolean hasPosition(ChessPosition position) {
        return board.containsKey(position);
    }

    /**
     * Sets the board to the default starting board.
     *
     * @see ChessBoard#DEFAULT_BOARD
     */
    public
    void resetBoard() {
        this.board.putAll(DEFAULT_BOARD);
    }

    /**
     * Adds a {@link ChessPiece} to the chessboard at a given {@link ChessPosition}.
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public
    void addPiece(ChessPosition position, ChessPiece piece) {
        /*
        Every valid position should exist upon instantiation, so we'll throw an illegal argument exception
        if we get one that's not already there.
         */
        if (!board.containsKey(position)) {
            throw new IllegalArgumentException(
                    String.format("Chess position illegal. Suggested addition:\nRow: %d, Col: %d",
                                  position.getRow(), position.getColumn()
                    ));
        }
        board.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     *
     * @return Either the piece at the position, or null if no piece is at that position
     */
    public
    ChessPiece getPiece(ChessPosition position) {
        return board.get(position);
    }

    public
    Iterator<ChessPosition> positionIterator() {
        return board.keySet().iterator();
    }

    @Override
    public
    int hashCode() {
        return board.hashCode();
    }

    @Override
    public
    boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessBoard that = (ChessBoard) o;
        return board.equals(that.board);
    }

    @Override
    public
    String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = BOARD_SIZE; row >= 1; row--) {
            sb.append("\n|");
            for (int col = 1; col <= BOARD_SIZE; col++) {
                var piece = board.get(new ChessPosition(row, col));
                if (piece == null) {
                    sb.append(" ");
                } else {
                    sb.append(switch (piece.getPieceType()) {
                        case KING -> piece.getTeamColor() == TeamColor.WHITE ? "K" : "k";
                        case QUEEN -> piece.getTeamColor() == TeamColor.WHITE ? "Q" : "q";
                        case BISHOP -> piece.getTeamColor() == TeamColor.WHITE ? "B" : "b";
                        case KNIGHT -> piece.getTeamColor() == TeamColor.WHITE ? "N" : "n";
                        case ROOK -> piece.getTeamColor() == TeamColor.WHITE ? "R" : "r";
                        case PAWN -> piece.getTeamColor() == TeamColor.WHITE ? "P" : "p";
                    });
                }
                sb.append("|");
            }
        }
        return sb.toString();
    }
}
