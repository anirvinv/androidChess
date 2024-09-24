package chess;

/**
 * Functional interface for checking validity of a move for a certain piece in
 * chess
 * 
 * @author Anirvin Vaddiyar
 * @author Sean Read
 *
 */
public interface ValidMoveChecker {

    /**
     * Checks if the move from sRow and sCol to moveRow and moveCol is valid and
     * returns true or returns false otherwise.
     * 
     * @param sRow    row of piece
     * @param sCol    column of piece
     * @param moveRow move row
     * @param moveCol move column
     * @return returns true if move is valid and false otherwise
     */
    public boolean check(int sRow, int sCol, int moveRow, int moveCol);

}
