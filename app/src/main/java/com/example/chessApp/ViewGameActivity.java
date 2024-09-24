package com.example.chessApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chess.Chess;
import chess.Game;
import chess.GameManager;
import chess.Piece;

public class ViewGameActivity extends AppCompatActivity {
    private static RecyclerView chessBoard;
    private static RecyclerView.Adapter adapter;
    private static List<Piece> boardPieces;
    private Chess chessGame;

    private int movePtr = -1;
    private static Game selectedGame;

    public static void updateBoardPieces(Chess chessGame){
        GameManager gameManager = chessGame.getGameManager();
        boardPieces.clear();
        for(int i =0; i< gameManager.board.length; i++){
            for(int j = 0; j < gameManager.board[i].length;j++){
                boardPieces.add(gameManager.board[i][j]);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_game);

        boardPieces = new ArrayList<>();
        chessGame = new Chess();
        adapter = new NonInteractiveGridAdapter(this, boardPieces);
        updateBoardPieces(chessGame);

        chessBoard = (RecyclerView) findViewById(R.id.chessBoard);
        chessBoard.setLayoutManager(new GridLayoutManager(this, 8));
        chessBoard.setAdapter(adapter);

        chessBoard.setClickable(false);

        Button backButton = (Button) findViewById(R.id.backButton);

        Context context = this;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SavedGamesActivity.class);
                startActivity(intent);
            }
        });

        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button prevButton = (Button) findViewById(R.id.prevButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextMove();
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevMove();
            }
        });

    }
    public int makeMove(String piece, String move){
        return chessGame.makeMove(piece, move);
    }
    public int makeMove(String piece, String move, String promotion){
        return chessGame.makeMove(piece, move, promotion);
    }

    public static Game getSelectedGame() {
        return selectedGame;
    }
    public static void setSelectedGame(Game selectedGame) {
        ViewGameActivity.selectedGame = selectedGame;
    }

    public void nextMove(){
        List<String[]> moveList = selectedGame.getMoveList();
        if(movePtr >= moveList.size() - 1){
            return;
        }
        movePtr++;
        String[] move = moveList.get(movePtr);
        if(move[2] == null){
            makeMove(move[0], move[1]);
        }else{
            makeMove(move[0], move[1], move[2]);
        }
        updateBoardPieces(chessGame);

    }
    public void prevMove(){
        List<String[]> moveList = selectedGame.getMoveList();
        if(movePtr <= -1){
            return;
        }
        movePtr--;
        chessGame = new Chess();
        for(int i =0; i <= movePtr; i++){
            String[] move = moveList.get(i);
            if(move[2] == null){
                makeMove(move[0], move[1]);
            }else{
                makeMove(move[0], move[1], move[2]);
            }
        }
        updateBoardPieces(chessGame);
    }


}