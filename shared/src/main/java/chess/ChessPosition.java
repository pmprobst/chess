package chess;

/**
 * A single square on the board. Row and column are 1-based; row 1 is the bottom from white's view.
 * <p>
 * Note: You can add to this class, but you may not alter the signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    /**
     * Creates a position with the given row and column (1–8).
     *
     * @param row the row (1 = bottom, 8 = top from white's perspective)
     * @param col the column (1 = left, 8 = right from white's perspective)
     */
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the row of this position (1–8). Row 1 is the bottom from white's perspective.
     *
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of this position (1–8). Column 1 is the left from white's perspective.
     *
     * @return the column
     */
    public int getColumn() {
        return col;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPosition other)) return false;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(row);
        result = 31 * result + Integer.hashCode(col);
        return result;
    }

    @Override
    public String toString() {
        return "ChessPosition{" + "row=" + row + ", col=" + col + '}';
    }
}
