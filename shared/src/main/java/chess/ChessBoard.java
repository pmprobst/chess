package chess;

import java.util.Arrays;

/**
 * An 8x8 chessboard that holds chess pieces. Rows and columns are 1-based (1–8).
 * <p>
 * Note: You can add to this class, but you may not alter the signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares;

    /** Creates an empty 8x8 board. */
    public ChessBoard() {
        squares = new ChessPiece[8][8];
    }

    /**
     * Places a piece on the board at the given position. Use null for piece to clear the square.
     *
     * @param position the square (row and column 1–8)
     * @param piece    the piece to place, or null to clear the square
     * @throws IllegalArgumentException if position is null or out of bounds
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        validatePosition(position);
        int row = position.getRow();
        int col = position.getColumn();
        squares[row - 1][col - 1] = piece;
    }

    /**
     * Returns the piece at the given position, or null if the square is empty.
     *
     * @param position the square (row and column 1–8)
     * @return the piece at that position, or null
     * @throws IllegalArgumentException if position is null or out of bounds
     */
    public ChessPiece getPiece(ChessPosition position) {
        validatePosition(position);
        int row = position.getRow();
        int col = position.getColumn();
        return squares[row - 1][col - 1];
    }

    /**
     * Validates that a position is not null and within board bounds.
     *
     * @param position the position to validate
     * @throws IllegalArgumentException if position is null or out of bounds
     */
    private void validatePosition(ChessPosition position) {
        if (position == null) {
            throw new IllegalArgumentException("position cannot be null");
        }
        int row = position.getRow();
        int col = position.getColumn();
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            throw new IllegalArgumentException("position out of bounds: " + row + "," + col);
        }
    }

    /**
     * Returns a new board with the same piece layout. Piece references are shared; only the grid is copied.
     *
     * @return a copy of this board
     */
    public ChessBoard copy() {
        ChessBoard copy = new ChessBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                copy.squares[row][col] = squares[row][col];
            }
        }
        return copy;
    }

    /** Clears the board and sets up the standard starting position (all pieces on ranks 1, 2, 7, 8). */
    public void resetBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = null;
            }
        }

        // Pawns
        for (int col = 1; col <= 8; col++) {
            addPiece(new ChessPosition(2, col),
                    new ChessPiece(ChessGame.TeamColor.WHITE,
                            ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, col),
                    new ChessPiece(ChessGame.TeamColor.BLACK,
                            ChessPiece.PieceType.PAWN));
        }

        // Rooks
        addPiece(new ChessPosition(1,1),
                new ChessPiece(ChessGame.TeamColor.WHITE,
                        ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1,8),
                new ChessPiece(ChessGame.TeamColor.WHITE,
                        ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,1),
                new ChessPiece(ChessGame.TeamColor.BLACK,
                        ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,8),
                new ChessPiece(ChessGame.TeamColor.BLACK,
                        ChessPiece.PieceType.ROOK));

        // Knights
        addPiece(new ChessPosition(1, 2),
                new ChessPiece(ChessGame.TeamColor.WHITE,
                        ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 7),
                new ChessPiece(ChessGame.TeamColor.WHITE,
                        ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 2),
                new ChessPiece(ChessGame.TeamColor.BLACK,
                        ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 7),
                new ChessPiece(ChessGame.TeamColor.BLACK,
                        ChessPiece.PieceType.KNIGHT));

        // Bishops
        addPiece(new ChessPosition(1, 3),
                new ChessPiece(ChessGame.TeamColor.WHITE,
                        ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 6),
                new ChessPiece(ChessGame.TeamColor.WHITE,
                        ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 3),
                new ChessPiece(ChessGame.TeamColor.BLACK,
                        ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 6),
                new ChessPiece(ChessGame.TeamColor.BLACK,
                        ChessPiece.PieceType.BISHOP));

        // Queens
        addPiece(new ChessPosition(1, 4),
                new ChessPiece(ChessGame.TeamColor.WHITE,
                        ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 4),
                new ChessPiece(ChessGame.TeamColor.BLACK,
                        ChessPiece.PieceType.QUEEN));

        // Kings
        addPiece(new ChessPosition(1, 5),
                new ChessPiece(ChessGame.TeamColor.WHITE,
                        ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 5),
                new ChessPiece(ChessGame.TeamColor.BLACK,
                        ChessPiece.PieceType.KING));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessBoard other)) return false;
        return Arrays.deepEquals(this.squares, other.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.squares);
    }
}
