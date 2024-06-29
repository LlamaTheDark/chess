package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board.
 * Board indices range is [1,8].
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
/*
########## INSTANCE ##########
 */
    private final int ROW;
    private final int COL;

    public ChessPosition(int row, int col) {
        this.ROW = row;
        this.COL = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() { return this.ROW; }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() { return this.COL; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return getRow() == that.getRow() && getColumn() == that.getColumn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }
}
