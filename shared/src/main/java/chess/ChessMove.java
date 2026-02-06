package chess;

/**
 * A single move: from one square to another, with an optional pawn promotion type.
 * If the move is not a pawn promotion, promotionPiece is null.
 * <p>
 * Note: You can add to this class, but you may not alter the signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    /**
     * Creates a move from start to end, with optional promotion type for pawn moves to the last rank.
     *
     * @param startPosition  the square the piece moves from
     * @param endPosition    the square the piece moves to
     * @param promotionPiece the piece type to promote to (QUEEN, ROOK, BISHOP, KNIGHT), or null if not a promotion
     */
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * Returns the starting square of this move.
     *
     * @return the start position
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * Returns the destination square of this move.
     *
     * @return the end position
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Returns the promotion piece type when this move is a pawn promotion, or null otherwise.
     *
     * @return QUEEN, ROOK, BISHOP, or KNIGHT for promotion; null if not a promotion move
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessMove other)) return false;
        return startPosition.equals(other.startPosition) &&
               endPosition.equals(other.endPosition) &&
               promotionPiece == other.promotionPiece;
    }

    @Override
    public int hashCode() {
        int result = startPosition.hashCode();
        result = 31 * result + endPosition.hashCode();
        result = 31 * result + (promotionPiece != null ? promotionPiece.hashCode() : 0);
        return result;
    }
}
