package chess;

import com.example.chessApp.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Main file to start a chess game
 * 
 * @author Anirvin Vaddiyar
 * @author Sean Read
 */
public class Chess {

    public static class Pair<U, V> implements Serializable {
        private U first;
        private V second;

        public Pair(U u, V v){
            first = u;
            second = v;
        }
        public U getFirst() {
            return first;
        }

        public V getSecond() {
            return second;
        }
    }
    private GameManager gameManager;

    private ArrayList<String[]> moveList = new ArrayList<>();
    private char turnColor = 'w';
    boolean undid = false;
    private boolean gameOver= false;

    private List<Piece> flattenedBoard;

    public Chess(){
        gameManager = new GameManager();
        gameManager.initializeBoard();
    }

    public Piece[][] getBoard(){
        return gameManager.board;
    }

    public void setGame(GameManager gameManager){
        this.gameManager = gameManager;
    }

    public ArrayList<String[]> getMoveList() {
        return moveList;
    }

    public int makeMove(String piece, String move){
        if(!gameOver && !gameManager.isGameOver(turnColor)){
            // returns 1 if move successful, -1 otherwise
            if(gameManager.makeMove(piece, move, turnColor)==1){
                this.turnColor = (this.turnColor =='w'?'b':'w');
                String[] temp = {piece, move, null};
                moveList.add(temp);
                undid = false;
                return 1;
            }else{
                return -1;
            }
        }else{
            this.gameOver = true;
        }
        return -1;
    }
    public int makeMove(String piece, String move, String promotion){
        if(!gameOver && !gameManager.isGameOver(turnColor)){
            // returns 1 if move successful, -1 otherwise
            if(gameManager.makeMove(piece, move, promotion, turnColor)==1){
                this.turnColor = (this.turnColor =='w'?'b':'w');
                String[] temp = {piece, move, promotion};
                moveList.add(temp);
                undid = false;
                return 1;
            }else{
                return -1;
            }
        }else{
            this.gameOver = true;
        }
        return -1;
    }

    public GameManager getGameManager() {
        return gameManager;
    }


    public char getTurnColor() {
        return turnColor;
    }

    public void setTurnColor(char turnColor) {
        this.turnColor = turnColor;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    public int undoLastMove(){

        // check if last move white and last turn white or last move black and last turn black
        if(undid){
            return -1;
        }
        undid = true;
        ArrayList<String[]> newMoveList = new ArrayList<>();
        for(int i =0; i < moveList.size() - 1; i++){
            newMoveList.add(moveList.get(i));
        }
        this.gameManager = new GameManager();
        this.gameManager.initializeBoard();

        this.moveList = newMoveList;

        char turn = 'w';
        for(String[] move: this.moveList){
            if (move[2] != null) {
                this.gameManager.makeMove(move[0], move[1], move[2], turn);
            } else {
                this.gameManager.makeMove(move[0], move[1], turn);
            }
            turn = turn == 'w'?'b':'w';
        }
        this.turnColor = turn;
        return 1;
    }
    public void performRandomMove() {
        Piece[][] board = gameManager.board;
        char turn = getTurnColor();
        List<Pair<String, String>> validMoves = new ArrayList<>();
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

                            if (gameManager.makeMoveTest(pieceLoc, moveLoc, turn)) {
                                validMoves.add(new Pair<>(pieceLoc, moveLoc));
                            }
                        }
                    }
                }
            }
        }
        if(validMoves.size() > 0){
            Random random = new Random();
            Pair<String, String> move = validMoves.get(random.nextInt(validMoves.size()));
            makeMove(move.getFirst(),  move.getSecond());
        }

    }

}