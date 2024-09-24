package chess;

import android.widget.ImageView;

import java.util.Stack;

/**
 * Chess piece
 * 
 * @author Anirvin Vaddiyar
 * @author Sean Read
 */
public class Piece {

    /**
     * @author Anirvin Vaddiyar
     * @author Sean Read
     * @param <X> first in pair
     * @param <Y> second in pair
     */
    class Pair<X, Y> {
        public X first;
        public Y second;

        /**
         * @param first  first in pair
         * @param second second in pair
         */
        public Pair(X first, Y second) {
            this.first = first;
            this.second = second;
        }

        public String toString() {
            return this.first + ", " + this.second;
        }
    }

    // the coordinates are 0-7

    private int imageID=-1;
    private int row;
    private int col;
    private char color;
    // color = 'w' for white and 'b' for black
    private String name;
    private ValidMoveChecker moveChecker;

    // only need this for castle and en passant
    public Stack<Pair<Integer, Integer>> moveHistory = new Stack<>();

    /**
     * Constructor with name, color, and ValidMoveChecker
     * 
     * @param name        name of piece
     * @param color       color of piece, 'w' or 'b'
     * @param moveChecker ValidMoveChecker to check validity of moves
     */
    public Piece(String name, char color, ValidMoveChecker moveChecker) {
        this.name = name;
        this.color = color;
        this.moveChecker = moveChecker;
    }

    /**
     * Constructor with name, color, and ValidMoveChecker and move history
     * 
     * @param name        name of piece
     * @param color       color of piece, 'w' or 'b'
     * @param moveChecker ValidMoveChecker to check validity of moves
     * @param moveHistory stack of moves made to track move history for piece
     */
    public Piece(String name, char color, ValidMoveChecker moveChecker, Stack<Pair<Integer, Integer>> moveHistory) {
        this.name = name;
        this.color = color;
        this.moveChecker = moveChecker;
        this.moveHistory = moveHistory;
    }

    /**
     * Moves the piece to row, col if the move is valid
     * 
     * @param row row to be moved to [0, 7]
     * @param col column to be moved to in range [0, 7]
     * @return returns 1 if move was valid and -1 otherwise
     */
    public int move(int row, int col) {
        // System.out.println("row:" + this.row + " col:" + this.col);
        // System.out.println("Move row:" + row + " Move col:" + col);
        // returns 1 on success, -1 on failure

        // first check if the coordinates are even inside the board
        if (!(row >= 0 && row < 8 && col >= 0 && col < 8)) {
            return -1;
        }

        if (this.moveChecker.check(this.row, this.col, row, col)) {
            this.row = row;
            this.col = col;
            moveHistory.push(new Pair<>(this.row, this.col));
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Adds row, col pair to moveHistory of piece
     * 
     * @param row row of move
     * @param col column of move
     */
    public void addToHistory(int row, int col) {
        moveHistory.push(new Pair<>(row, col));
    }

    /**
     * Returns the number of moves made so far by the piece
     * 
     * @return the size of moveHistory or the number of moves made so far
     */
    public int getMoveHistoryLength() {
        return this.moveHistory.size();
    }

    /**
     * Pops the last move in the moveHistory stack
     */
    public void deleteLastMoveInHistory() {
        moveHistory.pop();
    }

    /**
     * Returns the row of the piece
     * 
     * @return the row of the piece
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the column of the piece
     * 
     * @return the column of the piece
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Returns the ValidMoveChecker assigned to the piece
     * 
     * @return the ValidMoveChecker assigned to the piece
     */
    public ValidMoveChecker getChecker() {
        return this.moveChecker;
    }

    /**
     * Returns the color of the piece
     * 
     * @return the color of the piece
     */
    public char getColor() {
        return this.color;
    }

    /**
     * Returns the name or type of the piece
     * 
     * @return the name of the piece
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the row and column of the piece
     * 
     * @param row row in board
     * @param col column in board
     */
    public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Assigns new name to piece
     * 
     * @param newName name to be set
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Assigns new ValidMoveChecker to piece
     * 
     * @param newChecker new ValidMoveChecker to piece
     */
    public void setChecker(ValidMoveChecker newChecker) {
        this.moveChecker = newChecker;
    }

    /**
     * Checks if piece is black
     * 
     * @return true if piece is black and false otherwise
     */
    public boolean isBlack() {
        return this.color == 'b';
    }

    /**
     * Checks if piece is white
     * 
     * @return true if piece is white and false otherwise
     */
    public boolean isWhite() {
        return this.color == 'w';
    }

    public String toString() {
        return this.name;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
