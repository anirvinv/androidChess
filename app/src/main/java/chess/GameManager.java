package chess;

import com.example.chessApp.MainActivity;
import com.example.chessApp.R;

/**
 * Contains a board 2d-array and contains all the methods to play a chess game
 * 
 * @author Anirvin Vaddiyar
 * @author Sean Read
 */
public class GameManager {
    public Piece[][] board = new Piece[8][8];

    /**
     * Initializes the board 2d-array with all the pieces necessary to start a chess
     * game
     */
    public void initializeBoard() {
        initializeWhiteSide();
        initializeBlackSide();
    }

    /**
     * Creates and places all the pieces on the white side of a chess board
     */
    private void initializeWhiteSide() {
        // Rooks
        ValidMoveChecker rookChecker = MoveCheckGenerator.getRookChecker(board, 'w');
        Piece rook1 = new Piece("wR", 'w', rookChecker);
        rook1.setImageID(R.drawable.white_rook);
        Piece rook2 = new Piece("wR", 'w', rookChecker);
        rook2.setImageID(R.drawable.white_rook);

        // Knights
        ValidMoveChecker knightChecker = MoveCheckGenerator.getKnightChecker(board, 'w');
        Piece knight1 = new Piece("wN", 'w', knightChecker);
        knight1.setImageID(R.drawable.white_knight);
        Piece knight2 = new Piece("wN", 'w', knightChecker);
        knight2.setImageID(R.drawable.white_knight);

        // Bishops
        ValidMoveChecker bishopChecker = MoveCheckGenerator.getBishopChecker(board, 'w');
        Piece bishop1 = new Piece("wB", 'w', bishopChecker);
        bishop1.setImageID(R.drawable.white_bishop);
        Piece bishop2 = new Piece("wB", 'w', bishopChecker);
        bishop2.setImageID(R.drawable.white_bishop);

        ValidMoveChecker kingChecker = MoveCheckGenerator.getKingChecker(board, 'w');
        Piece king = new Piece("wK", 'w', kingChecker);
        king.setImageID(R.drawable.white_king);

        ValidMoveChecker queenChecker = MoveCheckGenerator.getQueenChecker(board, 'w');
        Piece queen = new Piece("wQ", 'w', queenChecker);
        queen.setImageID(R.drawable.white_queen);

        rook1.setLocation(7, 0);
        rook2.setLocation(7, 7);
        knight1.setLocation(7, 1);
        knight2.setLocation(7, 6);
        bishop1.setLocation(7, 2);
        bishop2.setLocation(7, 5);
        king.setLocation(7, 4);
        queen.setLocation(7, 3);

        board[7][0] = rook1;
        board[7][7] = rook2;
        board[7][1] = knight1;
        board[7][6] = knight2;
        board[7][2] = bishop1;
        board[7][5] = bishop2;
        board[7][4] = king;
        board[7][3] = queen;

        for (int i = 0; i < 8; i++) {
            Piece pawn = new Piece("wp", 'w', MoveCheckGenerator.getWhitePawnChecker(board));
            pawn.setLocation(6, i);
            pawn.setImageID(R.drawable.white_pawn);
            board[6][i] = pawn;
        }
    }

    /**
     * Creates and places all the pieces on the black side of a chess board
     */
    private void initializeBlackSide() {
        // Rooks
        ValidMoveChecker rookChecker = MoveCheckGenerator.getRookChecker(board, 'b');
        Piece rook1 = new Piece("bR", 'b', rookChecker);
        rook1.setImageID(R.drawable.black_rook);
        Piece rook2 = new Piece("bR", 'b', rookChecker);
        rook2.setImageID(R.drawable.black_rook);

        // Knights
        ValidMoveChecker knightChecker = MoveCheckGenerator.getKnightChecker(board, 'b');
        Piece knight1 = new Piece("bN", 'b', knightChecker);
        knight1.setImageID(R.drawable.black_knight);
        Piece knight2 = new Piece("bN", 'b', knightChecker);
        knight2.setImageID(R.drawable.black_knight);

        // Bishops
        ValidMoveChecker bishopChecker = MoveCheckGenerator.getBishopChecker(board, 'b');
        Piece bishop1 = new Piece("bB", 'b', bishopChecker);
        bishop1.setImageID(R.drawable.black_bishop);
        Piece bishop2 = new Piece("bB", 'b', bishopChecker);
        bishop2.setImageID(R.drawable.black_bishop);

        ValidMoveChecker kingChecker = MoveCheckGenerator.getKingChecker(board, 'b');
        Piece king = new Piece("bK", 'b', kingChecker);
        king.setImageID(R.drawable.black_king);

        ValidMoveChecker queenChecker = MoveCheckGenerator.getQueenChecker(board, 'b');
        Piece queen = new Piece("bQ", 'b', queenChecker);
        queen.setImageID(R.drawable.black_queen);

        rook1.setLocation(0, 0);
        rook2.setLocation(0, 7);
        knight1.setLocation(0, 1);
        knight2.setLocation(0, 6);
        bishop1.setLocation(0, 2);
        bishop2.setLocation(0, 5);
        king.setLocation(0, 4);
        queen.setLocation(0, 3);

        board[0][0] = rook1;
        board[0][7] = rook2;
        board[0][1] = knight1;
        board[0][6] = knight2;
        board[0][2] = bishop1;
        board[0][5] = bishop2;
        board[0][4] = king;
        board[0][3] = queen;
        for (int i = 0; i < 8; i++) {
            Piece pawn = new Piece("bp", 'b', MoveCheckGenerator.getBlackPawnChecker(board));
            pawn.setLocation(1, i);
            pawn.setImageID(R.drawable.black_pawn);
            board[1][i] = pawn;
        }
    }

    /**
     * Tests if a square on the chessboard is being targeted by any piece of the
     * specified color
     * 
     * @param color either 'w' or 'b'
     * @param row   in range [0,7]
     * @param col   in range [0,7]
     * @return if the square is vulnerable to any piece of the specified color
     */
    public boolean isCheckableSquare(char color, int row, int col) {
        // point of this function is to see if the square at row,col is vulnerable to
        // any piece of the specified color

        boolean isCheckable = false;

        Piece originalPiece = board[row][col];

        char oppositeColor = color == 'w' ? 'b' : 'w';
        board[row][col] = new Piece("TEMPORARY_TO_BE_REMOVED", oppositeColor, null);
        // need to do this to see if for example, king kills pawn and pawn is diagonally
        // guarding that killed pawn. Applies to all "guarded" pieces

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[i][j].getColor() == color) {
                    if (board[i][j].getChecker().check(i, j, row, col)) {
                        isCheckable = true;
                    }
                }
            }
        }
        board[row][col] = originalPiece;
        return isCheckable;
    }

    /**
     * This method is used to "remember" the chess board before a move is made so
     * that it can be reverted if its invalid
     * 
     * @param board chess board
     * @return clone of the board with shallow copies of the original pieces
     */
    public static Piece[][] cloneBoard(Piece[][] board) {
        // preserves positions, but uses the same pieces
        Piece[][] boardClone = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardClone[i][j] = board[i][j];
            }
        }
        return boardClone;
    }

    /**
     * Returns a boolean indicating if a move is valid on a chess board for a given
     * piece
     * 
     * @param piece piece to make move
     * @param move  the location to which piece is trying to be moved
     * @param color 'w' or 'b'
     * @return boolean indicating the validity of the move
     */
    public boolean makeMoveTest(String piece, String move, char color) {

        int pieceRow = 8 - (int) (piece.charAt(1) - '0');
        int pieceCol = piece.charAt(0) - 'a';

        int moveRow = 8 - (int) (move.charAt(1) - '0');
        int moveCol = move.charAt(0) - 'a';

        if (board[pieceRow][pieceCol] == null) {
            return false;
        }

        // castle --> king cannot move through a checkable square and cannot castle
        // while in check
        if (pieceCol == 4 && (moveCol == 2 || moveCol == 6)) {
            if (board[pieceRow][pieceCol].getName().equals(color + "K")) {
                for (int col = pieceCol; col != moveCol; col += moveCol > pieceCol ? 1 : -1) {
                    if (isCheckableSquare(color == 'w' ? 'b' : 'w', moveRow, col)) {
                        return false;
                    }
                }
            }
        }

        Piece[][] previousBoard = cloneBoard(board);

        if (board[pieceRow][pieceCol].getColor() == color && board[pieceRow][pieceCol].move(moveRow, moveCol) == 1) {

            Piece victim = board[moveRow][moveCol];

            board[moveRow][moveCol] = board[pieceRow][pieceCol];
            board[pieceRow][pieceCol] = null;

            if (kingInCheck(color)) {
                board[moveRow][moveCol].deleteLastMoveInHistory();
                board[moveRow][moveCol].setLocation(pieceRow, pieceCol);
                board[pieceRow][pieceCol] = board[moveRow][moveCol];
                board[moveRow][moveCol] = victim;
                setBoard(previousBoard);
                return false;
            }

            board[moveRow][moveCol].deleteLastMoveInHistory();
            board[moveRow][moveCol].setLocation(pieceRow, pieceCol);
            board[pieceRow][pieceCol] = board[moveRow][moveCol];
            board[moveRow][moveCol] = victim;
            setBoard(previousBoard);
            // Make sure this move doesn't open up check or leave one open
            return true;
        }
        return false;

    }

    /**
     * Moves a chess piece and returns -1 if move is invalid and 1 if it is valid
     * 
     * @param piece piece to make move
     * @param move  the location to which piece is trying to be moved
     * @param color 'w' or 'b'
     * @return -1 or 1 where -1 indicates failure to make the move and 1 for success
     */
    public int makeMove(String piece, String move, char color) {

        int pieceRow = 8 - (int) (piece.charAt(1) - '0');
        int pieceCol = piece.charAt(0) - 'a';

        int moveRow = 8 - (int) (move.charAt(1) - '0');
        int moveCol = move.charAt(0) - 'a';

        if (board[pieceRow][pieceCol] == null) {
            return -1;
        }

        Piece[][] previousBoard = cloneBoard(board);
        Piece victim = board[moveRow][moveCol];

        // DEFAULT PAWN PROMOTION = QUEEN

        if (board[pieceRow][pieceCol].getColor() == color
                && ((color == 'w' && pieceRow == 1) || (color == 'b' && pieceRow == 6))
                && board[pieceRow][pieceCol].getChecker().check(pieceRow, pieceCol, moveRow, moveCol)
                && (board[pieceRow][pieceCol].getName().equals("wp")
                || board[pieceRow][pieceCol].getName().equals("bp"))) {
            board[moveRow][moveCol] = board[pieceRow][pieceCol];
            board[pieceRow][pieceCol] = null;

            board[moveRow][moveCol].deleteLastMoveInHistory();
            board[moveRow][moveCol].setLocation(pieceRow, pieceCol);
            board[pieceRow][pieceCol] = board[moveRow][moveCol];
            board[moveRow][moveCol] = victim;
            setBoard(previousBoard);
            return makeMove(piece, move, "Q", color);
        }
        // castle --> king cannot move through a checkable square and cannot castle
        // while in check
        if (pieceCol == 4 && (moveCol == 2 || moveCol == 6)) {
            if (board[pieceRow][pieceCol].getName().equals(color + "K")) {
                for (int col = pieceCol; col != moveCol; col += moveCol > pieceCol ? 1 : -1) {
                    if (isCheckableSquare(color == 'w' ? 'b' : 'w', moveRow, col)) {
                        return -1;
                    }
                }
            }
        }

        if (board[pieceRow][pieceCol].getColor() == color && board[pieceRow][pieceCol].move(moveRow, moveCol) == 1) {

            board[moveRow][moveCol] = board[pieceRow][pieceCol];
            board[pieceRow][pieceCol] = null;

            if (kingInCheck(color)) {
                board[moveRow][moveCol].deleteLastMoveInHistory();
                board[moveRow][moveCol].setLocation(pieceRow, pieceCol);
                board[pieceRow][pieceCol] = board[moveRow][moveCol];
                board[moveRow][moveCol] = victim;
                setBoard(previousBoard);
                return -1;
            }
            // Make sure this move doesn't open up check or leave one open
            return 1;
        }
        return -1;
    }

    /**
     * Overloaded version of previous makeMove to account for the extra input for
     * pawn promotions
     * 
     * @param piece     piece to make move
     * @param move      the location to which piece is trying to be moved
     * @param promotion the promotion piece
     * @param color     'w' or 'b'
     * @return -1 or 1 where -1 indicates failure to make the move and 1 for success
     */
    public int makeMove(String piece, String move, String promotion, char color) {

        // Same as before
        int pieceRow = 8 - (int) (piece.charAt(1) - '0');
        int pieceCol = piece.charAt(0) - 'a';

        int moveRow = 8 - (int) (move.charAt(1) - '0');
        int moveCol = move.charAt(0) - 'a';

        if (board[pieceRow][pieceCol] == null) {
            return -1;
        }
        Piece[][] previousBoard = cloneBoard(board);

        // Now we must check if the piece is in the appropriate row based on color and
        // is a pawn
        if (board[pieceRow][pieceCol].getColor() == color
                && ((color == 'w' && pieceRow == 1) || (color == 'b' && pieceRow == 6))
                && board[pieceRow][pieceCol].getChecker().check(pieceRow, pieceCol, moveRow, moveCol)
                && (board[pieceRow][pieceCol].getName().equals("wp")
                        || board[pieceRow][pieceCol].getName().equals("bp"))) {

            // Same as before
            board[moveRow][moveCol] = board[pieceRow][pieceCol];
            board[pieceRow][pieceCol] = null;

            if (kingInCheck(color)) {
                board[moveRow][moveCol].setLocation(pieceRow, pieceCol);
                board[pieceRow][pieceCol] = board[moveRow][moveCol];
                setBoard(previousBoard);
                return -1;
            }

            // Promotion happens after the move is guaranteed to be valid
            // The promotion string is also guaranteed to be valid, so adjust for different
            // cases
            board[moveRow][moveCol].setLocation(moveRow, moveCol);
            if (promotion.equals("N")) {
                String newName = color + "N";
                board[moveRow][moveCol].setImageID(color=='w'?R.drawable.white_knight:R.drawable.black_knight);
                board[moveRow][moveCol].setName(newName);
                board[moveRow][moveCol]
                        .setChecker(MoveCheckGenerator.getKnightChecker(board, color));
            } else if (promotion.equals("Q")) {
                String newName = color + "Q";
                board[moveRow][moveCol].setImageID(color=='w'?R.drawable.white_queen:R.drawable.black_queen);
                board[moveRow][moveCol].setName(newName);
                board[moveRow][moveCol].setChecker(MoveCheckGenerator.getQueenChecker(board, color));
            } else if (promotion.equals("B")) {
                String newName = color + "B";
                board[moveRow][moveCol].setImageID(color=='w'?R.drawable.white_bishop:R.drawable.black_bishop);
                board[moveRow][moveCol].setName(newName);
                board[moveRow][moveCol]
                        .setChecker(MoveCheckGenerator.getBishopChecker(board, color));
            } else if (promotion.equals("R")) {
                String newName = color + "R";
                board[moveRow][moveCol].setImageID(color=='w'?R.drawable.white_rook:R.drawable.black_rook);
                board[moveRow][moveCol].setName(newName);
                board[moveRow][moveCol].setChecker(MoveCheckGenerator.getRookChecker(board, color));
            }

            return 1;
        }
        return -1;
    }

    /**
     * Prints the current state of the board
     */
    public void printBoard() {
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null) {
                    System.out.print(board[i][j]);
                } else {
                    if ((i + j) % 2 == 0) {
                        System.out.print("  ");
                    } else {

                        System.out.print("##");
                    }
                }
                System.out.print(" ");
            }
            System.out.println(8 - i);
        }
        System.out.print(" ");
        for (int i = 0; i < 8; i++) {
            System.out.print((char) ('a' + i) + "  ");
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Returns boolean indicating whether the king with the specified color is in
     * check
     * 
     * @param color 'w' or 'b'
     * @return boolean if king of specified color is checked
     */
    public boolean kingInCheck(char color) {
        // Identify the {color} king's location on board
        int kingRow = -1;
        int kingCol = -1;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != null && board[i][j].toString().equals(color + "K")) {
                    kingRow = i;
                    kingCol = j;
                }
            }
        }
        char oppositeColor = color == 'w' ? 'b' : 'w';
        // Check if any piece on the {opposite color}'s side can capture {color} king
        return isCheckableSquare(oppositeColor, kingRow, kingCol);
    }

    /**
     * Updates the board to hold the same Piece references in the same positions as
     * the parameter board
     * 
     * @param board
     */
    public void setBoard(Piece[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    /**
     * Checks if the current turn is in check mate
     * 
     * @param turn 'w' or 'b'
     * @return true if the current turn has check mate and false otherwise
     */
    public boolean isGameOver(char turn) {
        Piece[][] boardClone = cloneBoard(board);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null && board[i][j].getColor() == turn) {
                    for (int a = 0; a < board.length; a++) {
                        for (int b = 0; b < board[0].length; b++) {
                            String pieceLoc = "";
                            String moveLoc = "";
                            pieceLoc += (char) (j + 'a');
                            pieceLoc += (char) ((8 - i) + '0');
                            moveLoc += (char) (b + 'a');
                            moveLoc += (char) ((8 - a) + '0');
                            Piece victim = board[a][b];

                            if (makeMoveTest(pieceLoc, moveLoc, turn)) {
                                setBoard(boardClone);
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

}
