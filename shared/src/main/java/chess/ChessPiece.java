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
        Collection<ChessMove> moves = new ArrayList<>();
        
        switch (type) {
            case ROOK:
                return rookMoves(board, myPosition);
            case BISHOP:
                return bishopMoves(board, myPosition);
            case QUEEN:
                return queenMoves(board, myPosition);
            case KING:
                return kingMoves(board, myPosition);
            case KNIGHT:
                return knightMoves(board, myPosition);
            case PAWN:
                return pawnMoves(board, myPosition);
            default:
                return moves;
        }
    }
    
    /**
     * Calculates all valid moves for a rook
     */
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        
        // Move right (increasing column)
        for (int c = col + 1; c <= 8; c++) {
            ChessPosition endPos = new ChessPosition(row, c);
            ChessPiece pieceAtEnd = board.getPiece(endPos);
            if (pieceAtEnd == null) {
                // Empty square - can move here
                moves.add(new ChessMove(myPosition, endPos, null));
            } else {
                // Square occupied
                if (pieceAtEnd.getTeamColor() != pieceColor) {
                    // Enemy piece - can capture
                    moves.add(new ChessMove(myPosition, endPos, null));
                }
                // Stop in both cases (friendly or enemy)
                break;
            }
        }
        
        // Move left (decreasing column)
        for (int c = col - 1; c >= 1; c--) {
            ChessPosition endPos = new ChessPosition(row, c);
            ChessPiece pieceAtEnd = board.getPiece(endPos);
            if (pieceAtEnd == null) {
                moves.add(new ChessMove(myPosition, endPos, null));
            } else {
                if (pieceAtEnd.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, endPos, null));
                }
                break;
            }
        }
        
        // Move up (increasing row)
        for (int r = row + 1; r <= 8; r++) {
            ChessPosition endPos = new ChessPosition(r, col);
            ChessPiece pieceAtEnd = board.getPiece(endPos);
            if (pieceAtEnd == null) {
                moves.add(new ChessMove(myPosition, endPos, null));
            } else {
                if (pieceAtEnd.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, endPos, null));
                }
                break;
            }
        }
        
        // Move down (decreasing row)
        for (int r = row - 1; r >= 1; r--) {
            ChessPosition endPos = new ChessPosition(r, col);
            ChessPiece pieceAtEnd = board.getPiece(endPos);
            if (pieceAtEnd == null) {
                moves.add(new ChessMove(myPosition, endPos, null));
            } else {
                if (pieceAtEnd.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, endPos, null));
                }
                break;
            }
        }
        
        return moves;
    }
    
    /**
     * Calculates all valid moves for a bishop
     */
    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        
        // Move up-right (row++, col++)
        for (int r = row + 1, c = col + 1; r <= 8 && c <= 8; r++, c++) {
            ChessPosition endPos = new ChessPosition(r, c);
            ChessPiece pieceAtEnd = board.getPiece(endPos);
            if (pieceAtEnd == null) {
                moves.add(new ChessMove(myPosition, endPos, null));
            } else {
                if (pieceAtEnd.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, endPos, null));
                }
                break;
            }
        }
        
        // Move up-left (row++, col--)
        for (int r = row + 1, c = col - 1; r <= 8 && c >= 1; r++, c--) {
            ChessPosition endPos = new ChessPosition(r, c);
            ChessPiece pieceAtEnd = board.getPiece(endPos);
            if (pieceAtEnd == null) {
                moves.add(new ChessMove(myPosition, endPos, null));
            } else {
                if (pieceAtEnd.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, endPos, null));
                }
                break;
            }
        }
        
        // Move down-right (row--, col++)
        for (int r = row - 1, c = col + 1; r >= 1 && c <= 8; r--, c++) {
            ChessPosition endPos = new ChessPosition(r, c);
            ChessPiece pieceAtEnd = board.getPiece(endPos);
            if (pieceAtEnd == null) {
                moves.add(new ChessMove(myPosition, endPos, null));
            } else {
                if (pieceAtEnd.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, endPos, null));
                }
                break;
            }
        }
        
        // Move down-left (row--, col--)
        for (int r = row - 1, c = col - 1; r >= 1 && c >= 1; r--, c--) {
            ChessPosition endPos = new ChessPosition(r, c);
            ChessPiece pieceAtEnd = board.getPiece(endPos);
            if (pieceAtEnd == null) {
                moves.add(new ChessMove(myPosition, endPos, null));
            } else {
                if (pieceAtEnd.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, endPos, null));
                }
                break;
            }
        }
        
        return moves;
    }
    
    /**
     * Calculates all valid moves for a queen (combines rook and bishop moves)
     */
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        // Queen moves like both a rook and a bishop
        moves.addAll(rookMoves(board, myPosition));
        moves.addAll(bishopMoves(board, myPosition));
        return moves;
    }
    
    /**
     * Calculates all valid moves for a king (one square in any direction)
     */
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        
        // All 8 possible directions: up, down, left, right, and 4 diagonals
        int[] rowOffsets = {1, 1, 1, 0, 0, -1, -1, -1};
        int[] colOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};
        
        for (int i = 0; i < 8; i++) {
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];
            
            // Check if within board bounds
            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition endPos = new ChessPosition(newRow, newCol);
                ChessPiece pieceAtEnd = board.getPiece(endPos);
                
                // Can move to empty square or capture enemy piece
                if (pieceAtEnd == null || pieceAtEnd.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, endPos, null));
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Calculates all valid moves for a knight (L-shaped moves)
     */
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        
        // All 8 possible L-shaped moves
        int[] rowOffsets = {2, 2, -2, -2, 1, 1, -1, -1};
        int[] colOffsets = {1, -1, 1, -1, 2, -2, 2, -2};
        
        for (int i = 0; i < 8; i++) {
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];
            
            // Check if within board bounds
            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition endPos = new ChessPosition(newRow, newCol);
                ChessPiece pieceAtEnd = board.getPiece(endPos);
                
                // Can move to empty square or capture enemy piece (knight can jump over pieces)
                if (pieceAtEnd == null || pieceAtEnd.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, endPos, null));
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Calculates all valid moves for a pawn
     */
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        
        boolean isWhite = (pieceColor == ChessGame.TeamColor.WHITE);
        int forwardDirection = isWhite ? 1 : -1;
        int startingRow = isWhite ? 2 : 7;
        int promotionRow = isWhite ? 8 : 1;
        
        // Move forward 1 square
        int newRow = row + forwardDirection;
        if (newRow >= 1 && newRow <= 8) {
            ChessPosition forwardPos = new ChessPosition(newRow, col);
            ChessPiece pieceAtForward = board.getPiece(forwardPos);
            
            if (pieceAtForward == null) {
                // Empty square - can move forward
                if (newRow == promotionRow) {
                    // Promotion - generate moves for all 4 promotion types
                    moves.add(new ChessMove(myPosition, forwardPos, PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, forwardPos, PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, forwardPos, PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, forwardPos, PieceType.KNIGHT));
                } else {
                    // Normal forward move
                    moves.add(new ChessMove(myPosition, forwardPos, null));
                }
            }
        }
        
        // Move forward 2 squares (only from starting position)
        if (row == startingRow) {
            int twoSquaresRow = row + 2 * forwardDirection;
            if (twoSquaresRow >= 1 && twoSquaresRow <= 8) {
                ChessPosition oneSquarePos = new ChessPosition(row + forwardDirection, col);
                ChessPosition twoSquaresPos = new ChessPosition(twoSquaresRow, col);
                
                // Both squares must be empty
                if (board.getPiece(oneSquarePos) == null && board.getPiece(twoSquaresPos) == null) {
                    moves.add(new ChessMove(myPosition, twoSquaresPos, null));
                }
            }
        }
        
        // Capture diagonally forward (left and right)
        for (int colOffset : new int[]{-1, 1}) {
            int captureCol = col + colOffset;
            int captureRow = row + forwardDirection;
            
            if (captureRow >= 1 && captureRow <= 8 && captureCol >= 1 && captureCol <= 8) {
                ChessPosition capturePos = new ChessPosition(captureRow, captureCol);
                ChessPiece pieceAtCapture = board.getPiece(capturePos);
                
                if (pieceAtCapture != null && pieceAtCapture.getTeamColor() != pieceColor) {
                    // Enemy piece - can capture
                    if (captureRow == promotionRow) {
                        // Promotion capture - generate moves for all 4 promotion types
                        moves.add(new ChessMove(myPosition, capturePos, PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, capturePos, PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, capturePos, PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, capturePos, PieceType.KNIGHT));
                    } else {
                        // Normal capture
                        moves.add(new ChessMove(myPosition, capturePos, null));
                    }
                }
            }
        }
        
        return moves;
    }
}
