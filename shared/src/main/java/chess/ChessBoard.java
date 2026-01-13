package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares;
    public ChessBoard() {
        squares = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (position == null) {
            throw new IllegalArgumentException("position cannot be null");
        }

        int row = position.getRow();
        int col = position.getColumn();

        if (row <1 || row > 8 || col < 1 || col > 8) {
            throw new IllegalArgumentException("position out of bounds: " + row + "," + col);
        }

        squares[row - 1][col - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (position == null) {
            throw new IllegalArgumentException("position cannot be null");
        }

        int row = position.getRow();
        int col = position.getColumn();

        if (row <1 || row > 8 || col < 1 || col > 8) {
            throw new IllegalArgumentException("position out of bounds: " + row + "," + col);
        }

        return squares[row - 1][col - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
