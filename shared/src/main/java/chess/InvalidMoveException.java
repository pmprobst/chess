package chess;

/**
 * Thrown when an illegal move is attempted (e.g., wrong turn, no piece at start, or move not in valid moves).
 */
public class InvalidMoveException extends Exception {

    /** Creates an invalid move exception with no message. */
    public InvalidMoveException() {}

    /**
     * Creates an invalid move exception with the given message.
     *
     * @param message a description of why the move is invalid
     */
    public InvalidMoveException(String message) {
        super(message);
    }
}
