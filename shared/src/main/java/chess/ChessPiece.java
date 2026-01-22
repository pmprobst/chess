package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
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
        int result = pieceColor != null ? pieceColor.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (type) {
            case ROOK -> slidingMoves(board, myPosition, new int[][]{{0,1},{0,-1},{1,0},{-1,0}});
            case BISHOP -> slidingMoves(board, myPosition, new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}});
            case QUEEN -> {
                Collection<ChessMove> moves = new ArrayList<>();
                moves.addAll(slidingMoves(board, myPosition, new int[][]{{0,1},{0,-1},{1,0},{-1,0}}));
                moves.addAll(slidingMoves(board, myPosition, new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}}));
                yield moves;
            }
            case KING -> fixedMoves(board, myPosition, new int[][]{{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}});
            case KNIGHT -> fixedMoves(board, myPosition, new int[][]{{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}});
            case PAWN -> pawnMoves(board, myPosition);
        };
    }

    private Collection<ChessMove> slidingMoves(ChessBoard board, ChessPosition pos, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = pos.getRow(), col = pos.getColumn();
        for (int[] dir : directions) {
            for (int r = row + dir[0], c = col + dir[1]; r >= 1 && r <= 8 && c >= 1 && c <= 8; r += dir[0], c += dir[1]) {
                ChessPiece p = board.getPiece(new ChessPosition(r, c));
                if (p == null) moves.add(new ChessMove(pos, new ChessPosition(r, c), null));
                else {
                    if (p.getTeamColor() != pieceColor) moves.add(new ChessMove(pos, new ChessPosition(r, c), null));
                    break;
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> fixedMoves(ChessBoard board, ChessPosition pos, int[][] offsets) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = pos.getRow(), col = pos.getColumn();
        for (int[] off : offsets) {
            int r = row + off[0], c = col + off[1];
            if (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                ChessPiece p = board.getPiece(new ChessPosition(r, c));
                if (p == null || p.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(pos, new ChessPosition(r, c), null));
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = pos.getRow(), col = pos.getColumn();
        int dir = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startRow = pieceColor == ChessGame.TeamColor.WHITE ? 2 : 7;
        int promoRow = pieceColor == ChessGame.TeamColor.WHITE ? 8 : 1;
        
        // Forward 1
        if (row + dir >= 1 && row + dir <= 8) {
            ChessPosition fwd = new ChessPosition(row + dir, col);
            if (board.getPiece(fwd) == null) {
                addMoveOrPromotions(moves, pos, fwd, row + dir == promoRow);
            }
        }
        
        // Forward 2 from start
        if (row == startRow && board.getPiece(new ChessPosition(row + dir, col)) == null) {
            ChessPosition fwd2 = new ChessPosition(row + 2 * dir, col);
            if (board.getPiece(fwd2) == null) moves.add(new ChessMove(pos, fwd2, null));
        }
        
        // Captures
        for (int c : new int[]{col - 1, col + 1}) {
            if (c >= 1 && c <= 8 && row + dir >= 1 && row + dir <= 8) {
                ChessPosition cap = new ChessPosition(row + dir, c);
                ChessPiece p = board.getPiece(cap);
                if (p != null && p.getTeamColor() != pieceColor) {
                    addMoveOrPromotions(moves, pos, cap, row + dir == promoRow);
                }
            }
        }
        return moves;
    }

    private void addMoveOrPromotions(Collection<ChessMove> moves, ChessPosition from, ChessPosition to, boolean promote) {
        if (promote) {
            moves.add(new ChessMove(from, to, PieceType.QUEEN));
            moves.add(new ChessMove(from, to, PieceType.ROOK));
            moves.add(new ChessMove(from, to, PieceType.BISHOP));
            moves.add(new ChessMove(from, to, PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(from, to, null));
        }
    }
}
