package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board. Board indices range is [1,8].
 * <p>
 * Note: You can add to this class, but you may not alter signature of the existing methods.
 */
public
class ChessPosition {
    /*
    ########## INSTANCE ##########
     */
    private final int row;
    private final int col;

    public
    ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in 1 codes for the bottom row
     */
    public
    int getRow() {return this.row;}

    /**
     * @return which column this position is in 1 codes for the left row
     */
    public
    int getColumn() {return this.col;}

    @Override
    public
    int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }

    @Override
    public
    boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPosition that = (ChessPosition) o;
        return getRow() == that.getRow() && getColumn() == that.getColumn();
    }

    @Override
    public
    String toString() {
        return switch (col) {
            case 1 -> "a";
            case 2 -> "b";
            case 3 -> "c";
            case 4 -> "d";
            case 5 -> "e";
            case 6 -> "f";
            case 7 -> "g";
            case 8 -> "h";
            default -> throw new IllegalStateException("Unexpected value: " + col);
        } + row;
    }
}
