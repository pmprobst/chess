package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
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
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK;

        /** Returns the other team color. */
        TeamColor opposite() {
            return this == WHITE ? BLACK : WHITE;
        }
    }

    private static final int BOARD_MIN = 1;
    private static final int BOARD_MAX = 8;

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
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
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
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
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheckOnBoard(board, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && !hasAnyValidMove(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && !hasAnyValidMove(teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    /**
     * Applies the given move to the board. Moves the piece from start to end;
     * if the move specifies a promotion piece, places the promoted piece instead.
     *
     * @param board the board to modify
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
     * Returns the position of the king of the given color on the board,
     * or null if no king is found.
     *
     * @param board the board to search
     * @param color the team color of the king to find
     * @return the king's position, or null
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
     * Returns true if the given team's king could be captured by an opposing
     * piece on the given board state.
     *
     * @param board     the board state to evaluate
     * @param teamColor the team to check for being in check
     * @return true if that team's king is under attack
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
     * Returns true if the given team has at least one valid move available.
     *
     * @param teamColor the team to check
     * @return true if any piece of that team has valid moves
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
