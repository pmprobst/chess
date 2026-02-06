package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A single chess piece with a team color and piece type (king, queen, rook, bishop, knight, pawn).
 * <p>
 * Note: You can add to this class, but you may not alter the signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    /**
     * Creates a piece of the given color and type.
     *
     * @param pieceColor the team (white or black)
     * @param type       the kind of piece
     */
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /** The six types of chess pieces. */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * Returns which team this piece belongs to.
     *
     * @return WHITE or BLACK
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * Returns the type of this piece (king, queen, rook, bishop, knight, pawn).
     *
     * @return the piece type
     */
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece other)) return false;
        return pieceColor == other.pieceColor && type == other.type;
    }

    @Override
    public int hashCode() {
        return 31 * pieceColor.hashCode() + type.hashCode();
    }

    /**
     * Returns all pseudo-legal moves for this piece from the given position. Does not filter out
     * moves that would leave the king in check (that is done by ChessGame.validMoves).
     *
     * @param board      the current board
     * @param myPosition the square this piece is on
     * @return collection of moves this piece can make from myPosition (may include moves that put own king in check)
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (type) {
            case ROOK -> slidingMoves(board, myPosition, new int[][]{{0,1},{0,-1},{1,0},{-1,0}});
            case BISHOP -> slidingMoves(board, myPosition, new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}});
            case QUEEN -> slidingMoves(board, myPosition, new int[][]{{0,1},{0,-1},{1,0},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}});
            case KING -> fixedMoves(board, myPosition, new int[][]{{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}});
            case KNIGHT -> fixedMoves(board, myPosition, new int[][]{{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}});
            case PAWN -> pawnMoves(board, myPosition);
        };
    }

    /** Sliding moves (rook, bishop, queen): follow each direction until blocked or capture. */
    private Collection<ChessMove> slidingMoves(ChessBoard board, ChessPosition pos, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = pos.getRow(), col = pos.getColumn();
        for (int[] dir : directions) {
            for (int r = row + dir[0], c = col + dir[1]; inBounds(r, c); r += dir[0], c += dir[1]) {
                ChessPosition endPos = new ChessPosition(r, c);
                ChessPiece p = board.getPiece(endPos);
                if (p == null) moves.add(new ChessMove(pos, endPos, null));
                else {
                    if (p.getTeamColor() != pieceColor) moves.add(new ChessMove(pos, endPos, null));
                    break;
                }
            }
        }
        return moves;
    }

    /** Fixed-step moves (king, knight): one step per direction, no sliding. */
    private Collection<ChessMove> fixedMoves(ChessBoard board, ChessPosition pos, int[][] offsets) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = pos.getRow(), col = pos.getColumn();
        for (int[] off : offsets) {
            int r = row + off[0], c = col + off[1];
            if (inBounds(r, c)) {
                ChessPosition endPos = new ChessPosition(r, c);
                ChessPiece p = board.getPiece(endPos);
                if (p == null || p.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(pos, endPos, null));
                }
            }
        }
        return moves;
    }

    /** Pawn moves: forward one or two, diagonal capture, promotion on last rank. */
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = pos.getRow(), col = pos.getColumn();
        boolean isWhite = pieceColor == ChessGame.TeamColor.WHITE;
        int dir = isWhite ? 1 : -1;
        int newRow = row + dir;
        if (!inBounds(newRow, col)) return moves;
        
        ChessPosition fwd = new ChessPosition(newRow, col);
        if (board.getPiece(fwd) == null) {
            addMoveOrPromotions(moves, pos, fwd, newRow == (isWhite ? 8 : 1));
            if (row == (isWhite ? 2 : 7)) {
                ChessPosition fwd2 = new ChessPosition(row + 2 * dir, col);
                if (board.getPiece(fwd2) == null) moves.add(new ChessMove(pos, fwd2, null));
            }
        }
        
        for (int c : new int[]{col - 1, col + 1}) {
            if (inBounds(newRow, c)) {
                ChessPosition cap = new ChessPosition(newRow, c);
                ChessPiece p = board.getPiece(cap);
                if (p != null && p.getTeamColor() != pieceColor) {
                    addMoveOrPromotions(moves, pos, cap, newRow == (isWhite ? 8 : 1));
                }
            }
        }
        return moves;
    }

    /** True if (r, c) is on the board (1–8). */
    private boolean inBounds(int r, int c) {
        return r >= 1 && r <= 8 && c >= 1 && c <= 8;
    }

    /** Adds one move or, if promote is true, four promotion moves (queen, rook, bishop, knight). */
    private void addMoveOrPromotions(Collection<ChessMove> moves, ChessPosition from, ChessPosition to, boolean promote) {
        if (promote) {
            for (PieceType pt : new PieceType[]{PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                moves.add(new ChessMove(from, to, pt));
            }
        } else {
            moves.add(new ChessMove(from, to, null));
        }
    }
}
