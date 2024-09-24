package chess;

/**
 * Only contains static methods to generate ValidMoveCheckers for chess pieces
 * 
 * @author Anirvin Vaddiyar
 * @author Sean Read
 *
 */
public class MoveCheckGenerator {

    /**
     * This method returns a ValidMoveChecker which validates movements of white
     * pawns
     * 
     * @param board chess board
     * @return ValidMoveChecker for white pawns
     */
    public static ValidMoveChecker getWhitePawnChecker(Piece[][] board) {
        return (int sRow, int sCol, int moveRow, int moveCol) -> {
            // CASE 1: 1 up
            if (sRow - 1 == moveRow && moveCol == sCol && board[moveRow][moveCol] == null) {
                return true;
            }
            // CASE 2: 2 up
            if (sRow == 6 && sRow - 2 == moveRow && moveCol == sCol && board[moveRow][moveCol] == null
                    && board[sRow - 1][moveCol] == null) {
                return true;
            }
            // CASE 3: Diagonal kill or En Passant
            if (sRow - 1 == moveRow && (sCol - 1 == moveCol || sCol + 1 == moveCol)) {
                if (board[moveRow][moveCol] != null && board[moveRow][moveCol].isBlack())
                    return true;
                else if (board[moveRow][moveCol] == null) {
                    /*
                     * EN PASSANT
                     * 
                     */
                    Piece blackPawn = board[moveRow + 1][moveCol];
                    if (blackPawn != null && blackPawn.toString().equals("bp")
                            && blackPawn.getMoveHistoryLength() == 1 && blackPawn.moveHistory.peek().first == 3) {
                        board[moveRow + 1][moveCol] = null;
                        return true;
                    }
                }
            }
            return false;
        };
    }

    /**
     * This method returns a ValidMoveChecker which validates movements of black
     * pawns
     * 
     * @param board chess board
     * @return ValidMoveChecker for black pawns
     */
    public static ValidMoveChecker getBlackPawnChecker(Piece[][] board) {
        return (int sRow, int sCol, int moveRow, int moveCol) -> {
            // CASE 1: 1 down
            if (sRow + 1 == moveRow && moveCol == sCol && board[moveRow][moveCol] == null) {
                return true;
            }
            // CASE 2: 2 down
            if (sRow == 1 && sRow + 2 == moveRow && moveCol == sCol && board[moveRow][moveCol] == null
                    && board[sRow + 1][moveCol] == null) {
                return true;
            }
            // CASE 3: Diagonal kill or En Passant
            if (sRow + 1 == moveRow && (sCol - 1 == moveCol || sCol + 1 == moveCol)) {
                if (board[moveRow][moveCol] != null && board[moveRow][moveCol].isWhite())
                    return true;
                else if (board[moveRow][moveCol] == null) {
                    /*
                     * EN PASSANT
                     * 
                     */
                    Piece whitePawn = board[moveRow - 1][moveCol];
                    if (whitePawn != null && whitePawn.toString().equals("wp")
                            && whitePawn.getMoveHistoryLength() == 1 && whitePawn.moveHistory.peek().first == 4) {
                        board[moveRow - 1][moveCol] = null;
                        return true;
                    }
                }
            }
            return false;
        };
    }

    /**
     * Returns a ValidMoveChecker to validate moves for Rooks
     * 
     * @param board chess board
     * @param color color of rook
     * @return ValidMoveChecker to validate moves for Rooks
     */
    public static ValidMoveChecker getRookChecker(Piece[][] board, char color) {
        return (int sRow, int sCol, int moveRow, int moveCol) -> {

            if (board[moveRow][moveCol] != null && board[moveRow][moveCol].getColor() == color) {
                return false;
            }
            // CASE1: Horizontal move
            if (sRow - moveRow == 0) {
                int colInc = sCol < moveCol ? 1 : -1;
                for (int col = sCol; col != moveCol; col += colInc) {
                    if (board[sRow][col] != null && board[sRow][col] != board[sRow][sCol])
                        return false;

                }
                return true;
            }
            // CASE2: Vertical move
            if (sCol - moveCol == 0) {
                int rowInc = sRow < moveRow ? 1 : -1;
                for (int row = sRow; row != moveRow; row += rowInc) {
                    if (board[row][sCol] != null && board[row][sCol] != board[sRow][sCol])
                        return false;
                }
                return true;
            }

            return false;
        };
    }

    /**
     * Returns a ValidMoveChecker to validate moves for Knights
     * 
     * @param board chess board
     * @param color color of Knight
     * @return ValidMoveChecker to validate moves for Knights
     */
    public static ValidMoveChecker getKnightChecker(Piece[][] board, char color) {
        return (int sRow, int sCol, int moveRow, int moveCol) -> {
            if (board[moveRow][moveCol] != null && board[moveRow][moveCol].getColor() == color) {
                return false;
            }
            if (abs(sRow - moveRow) == 2 && abs(sCol - moveCol) == 1) {
                return true;
            }
            if (abs(sRow - moveRow) == 1 && abs(sCol - moveCol) == 2) {
                return true;
            }
            return false;
        };
    }

    /**
     * Returns a ValidMoveChecker to validate moves for bishops
     * 
     * @param board chess board
     * @param color color of bishop
     * @return ValidMoveChecker to validate moves for bishops
     */
    public static ValidMoveChecker getBishopChecker(Piece[][] board, char color) {
        return (int sRow, int sCol, int moveRow, int moveCol) -> {
            if (board[moveRow][moveCol] != null && board[moveRow][moveCol].getColor() == color) {
                return false;
            }

            if (abs(sRow - moveRow) != abs(sCol - moveCol))
                return false;

            int rowInc = sRow > moveRow ? -1 : 1;
            int colInc = sCol > moveCol ? -1 : 1;

            // travel diagonally. If bishop encounter any pieces on the way, then its not a
            // valid move
            for (int row = sRow, col = sCol; row != moveRow; row += rowInc, col += colInc) {
                if (board[row][col] != null && board[row][col] != board[sRow][sCol]) {
                    return false;
                }
            }
            return true;
        };
    }

    /**
     * Returns a ValidMoveChecker to validate moves for kings
     * 
     * @param board chess board
     * @param color color of king
     * @return ValidMoveChecker to validate moves for kings
     */
    public static ValidMoveChecker getKingChecker(Piece[][] board, char color) {
        return (int pieceRow, int pieceCol, int moveRow, int moveCol) -> {
            if (board[moveRow][moveCol] != null && board[moveRow][moveCol].getColor() == color) {
                return false;
            }

            if (abs(pieceRow - moveRow) <= 1 && abs(pieceCol - moveCol) <= 1
                    && (board[moveRow][moveCol] == null || board[moveRow][moveCol].getColor() != color)) {
                return true;
            }

            // CASTLE check
            if (pieceCol == 4 && (moveCol == 6 || moveCol == 2) && pieceRow == moveRow
                    && (moveRow == 0 || moveRow == 7)) {
                Piece king = board[pieceRow][pieceCol];
                Piece rook = board[moveRow][moveCol == 6 ? 7 : 0];
                if (rook != null && king != null
                        && rook.getColor() == king.getColor() && rook.getColor() == color
                        && king.toString().charAt(1) == 'K' && king.getMoveHistoryLength() == 0
                        && rook.toString().charAt(1) == 'R' && rook.getMoveHistoryLength() == 0) {

                    int rookCol = moveCol == 6 ? 5 : 3;

                    // check nothing is between the king and rook
                    for (int col = 4; col != rookCol; col += moveCol > king.getCol() ? 1 : -1) {
                        if (board[moveRow][col] != king && board[moveRow][col] != rook && board[moveRow][col] != null) {
                            return false;
                        }
                    }
                    rook.setLocation(moveRow, rookCol);

                    board[pieceRow][moveCol == 6 ? 7 : 0] = null;
                    board[moveRow][rookCol] = rook;
                    rook.addToHistory(moveRow, rookCol);
                    return true;
                }
                return false;
            }

            return false;
        };
    }

    /**
     * Returns a ValidMoveChecker to validate moves for queens
     * 
     * @param board chess board
     * @param color color of queen
     * @return ValidMoveChecker to validate moves for queens
     */
    public static ValidMoveChecker getQueenChecker(Piece[][] board, char color) {
        return (int sRow, int sCol, int moveRow, int moveCol) -> {
            if (board[moveRow][moveCol] != null && board[moveRow][moveCol].getColor() == color) {
                return false;
            }
            if (getRookChecker(board, color).check(sRow, sCol, moveRow, moveCol) ||
                    getBishopChecker(board, color).check(sRow, sCol, moveRow, moveCol)) {
                return true;
            }

            return false;
        };

    }

    /**
     * returns absolute value of an integer
     * 
     * @param x any integer
     * @return absolute value of x
     */
    private static int abs(int x) {
        return x > 0 ? x : (-1 * x);
    }

}
