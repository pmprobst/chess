package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Manages a chess game: executes moves, tracks turn, and reports check, checkmate, and stalemate.
 * A new game starts with the default board and white to move.
 * <p>
 * Note: You can add to this class, but you may not alter the signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * Returns which team's turn it is.
     *
     * @return the team color whose turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which team's turn it is.
     *
     * @param team the team color whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /** The two teams in a chess game. */
    public enum TeamColor {
        WHITE,
        BLACK;

        /** Returns the opposite team color. */
        TeamColor opposite() {
            return this == WHITE ? BLACK : WHITE;
        }
    }

    /** Board row/column bounds (1-based, inclusive). */
    private static final int BOARD_MIN = 1;
    private static final int BOARD_MAX = 8;

    /**
     * Returns all legal moves for the piece at the given position. Excludes moves that would leave
     * the moving team's king in check.
     *
     * @param startPosition the square containing the piece
     * @return the collection of valid moves, or null if there is no piece at startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) return null;
        Collection<ChessMove> pseudoLegal = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> result = new ArrayList<>();
        for (ChessMove move : pseudoLegal) {
            ChessBoard copy = board.copy();
            applyMoveToBoard(copy, move);
            if (!isInCheckOnBoard(copy, piece.getTeamColor())) {
                result.add(move);
            }
        }
        return result;
    }

    /**
     * Executes the given move if it is legal (piece present, correct turn, and move is valid).
     * Updates the board and switches the turn to the other team.
     *
     * @param move the move to perform
     * @throws InvalidMoveException if there is no piece at the start, wrong team moves, or move is not valid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) throw new InvalidMoveException();
        if (piece.getTeamColor() != teamTurn) throw new InvalidMoveException();
        Collection<ChessMove> valid = validMoves(move.getStartPosition());
        if (valid == null || !valid.contains(move)) throw new InvalidMoveException();
        applyMoveToBoard(board, move);
        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Returns whether the given team's king is under attack by an opposing piece.
     *
     * @param teamColor the team to check
     * @return true if that team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheckOnBoard(board, teamColor);
    }

    /**
     * Returns whether the given team is in checkmate (in check with no legal move to escape).
     *
     * @param teamColor the team to check
     * @return true if that team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && !hasAnyValidMove(teamColor);
    }

    /**
     * Returns whether the given team is in stalemate: not in check but has no legal moves.
     *
     * @param teamColor the team to check
     * @return true if that team is in stalemate
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && !hasAnyValidMove(teamColor);
    }

    /**
     * Replaces this game's board with the given board.
     *
     * @param board the new board (can be null)
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Returns the current chessboard.
     *
     * @return the board (may be null if setBoard(null) was called)
     */
    public ChessBoard getBoard() {
        return board;
    }

    /**
     * Applies the move to the given board: clears the start square and places the piece (or promoted piece) on the end square.
     *
     * @param board the board to modify (not null)
     * @param move  the move to apply
     */
    private void applyMoveToBoard(ChessBoard board, ChessMove move) {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) return;
        board.addPiece(move.getStartPosition(), null);
        ChessPiece toPlace = move.getPromotionPiece() != null
                ? new ChessPiece(piece.getTeamColor(), move.getPromotionPiece())
                : piece;
        board.addPiece(move.getEndPosition(), toPlace);
    }

    /**
     * Finds the square containing the king of the given color.
     *
     * @param board the board to search
     * @param color the team that owns the king
     * @return the king's position, or null if not found
     */
    private ChessPosition findKing(ChessBoard board, TeamColor color) {
        for (int row = BOARD_MIN; row <= BOARD_MAX; row++) {
            for (int col = BOARD_MIN; col <= BOARD_MAX; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece p = board.getPiece(pos);
                if (p != null && p.getPieceType() == ChessPiece.PieceType.KING && p.getTeamColor() == color) {
                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Returns whether the given team's king is attacked by any opposing piece on the given board.
     *
     * @param board     the board state to evaluate
     * @param teamColor the team whose king to check
     * @return true if that team is in check on this board
     */
    private boolean isInCheckOnBoard(ChessBoard board, TeamColor teamColor) {
        ChessPosition kingPos = findKing(board, teamColor);
        if (kingPos == null) return false;
        TeamColor enemy = teamColor.opposite();
        for (int row = BOARD_MIN; row <= BOARD_MAX; row++) {
            for (int col = BOARD_MIN; col <= BOARD_MAX; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece p = board.getPiece(pos);
                if (p != null && p.getTeamColor() == enemy) {
                    for (ChessMove move : p.pieceMoves(board, pos)) {
                        if (move.getEndPosition().equals(kingPos)) return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns whether the given team has any legal move.
     *
     * @param teamColor the team to check
     * @return true if at least one piece of that team has a valid move
     */
    private boolean hasAnyValidMove(TeamColor teamColor) {
        for (int row = BOARD_MIN; row <= BOARD_MAX; row++) {
            for (int col = BOARD_MIN; col <= BOARD_MAX; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece p = board.getPiece(pos);
                if (p != null && p.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(pos);
                    if (moves != null && !moves.isEmpty()) return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessGame other)) return false;
        return teamTurn == other.teamTurn && Objects.equals(board, other.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}
