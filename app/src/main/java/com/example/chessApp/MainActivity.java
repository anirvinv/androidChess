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
import chess.GameManager;
import chess.Piece;

public class MainActivity extends AppCompatActivity {
    private static RecyclerView chessBoard;
    private static RecyclerView.Adapter adapter;
    private static List<Piece> boardPieces;
    private static Chess chessGame;

    private static Toast invalidMoveToast;
    private static Toast checkToast;

    private static AlertDialog pawnPromotionDialog;
    private static Square[] moveFromTo = new Square[]{null, null};
    private static boolean drawBool = false;

    private static int[] pawnPromotionSelection = {0};
    public static void touchSquare(String squareLoc, LinearLayout square, Piece piece){
        if(moveFromTo[0] == null){
            moveFromTo[0] = new Square(squareLoc, square, piece);
            square.setBackgroundColor(Color.parseColor("#90EE90"));
        }else if(moveFromTo[1] == null){
            moveFromTo[1] = new Square(squareLoc, square, piece);

            // prompt for pawn promotion
            if(chessGame
                    .getGameManager()
                    .makeMoveTest(moveFromTo[0].getSquareLoc(),
                            moveFromTo[1].getSquareLoc(), chessGame.getTurnColor())){
                Piece chessPiece = moveFromTo[0].getPiece();
                char color = chessPiece.getColor();
                // if its a pawn, then check what row it's in
                if((chessPiece.getName().equals("bp") || chessPiece.getName().equals("wp"))
                        && ((color == 'w' && chessPiece.getRow() == 1) || (color == 'b' && chessPiece.getRow() == 6))){
                    pawnPromotionDialog.show();
                    return;
                }
            }

            int flag = makeMove(moveFromTo[0].getSquareLoc(),moveFromTo[1].getSquareLoc());
            if(flag == -1){
                invalidMoveToast.show();
            }
            updateBoardPieces(chessGame);
            if (chessGame.getGameManager().isGameOver(chessGame.getTurnColor())) {
                endGame();
            }else{
                if(chessGame.getGameManager().kingInCheck(chessGame.getTurnColor())){
                    checkToast.show();
                }
            }
            moveFromTo = new Square[]{null, null};
        }
    }

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
        setContentView(R.layout.activity_main);


        boardPieces = new ArrayList<>();
        chessGame = new Chess();
        adapter = new GridAdapter(this, boardPieces);
        updateBoardPieces(chessGame);

        chessBoard = (RecyclerView) findViewById(R.id.chessBoard);
        chessBoard.setLayoutManager(new GridLayoutManager(this, 8));
        chessBoard.setAdapter(adapter);
        invalidMoveToast = Toast.makeText(this, "Invalid Move", Toast.LENGTH_SHORT );
        checkToast = Toast.makeText(this, "Check", Toast.LENGTH_SHORT );


        final String[] pawnPromotionOptions = {"Queen", "Rook", "Bishop", "Knight"};
        AlertDialog.Builder pawnPromoDialogBuilder = new AlertDialog.Builder(this);
        pawnPromoDialogBuilder.setTitle("Pawn promotion options");
        pawnPromoDialogBuilder.setSingleChoiceItems(pawnPromotionOptions, pawnPromotionSelection[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pawnPromotionSelection[0]=i;

                String[] promoOptions = {"Q","R","B","N"};
                int flag = makeMove(moveFromTo[0].getSquareLoc(),moveFromTo[1].getSquareLoc(), promoOptions[pawnPromotionSelection[0]]);
                pawnPromotionSelection[0] = 0;
                updateBoardPieces(chessGame);
                moveFromTo = new Square[]{null, null};


                dialogInterface.dismiss();
            }
        });

        pawnPromoDialogBuilder.setCancelable(false);
        pawnPromotionDialog = pawnPromoDialogBuilder.create();

        Button home = (Button) findViewById(R.id.homeButton);

        Button resign = (Button) findViewById(R.id.resignButton);
        Button undo = (Button) findViewById(R.id.undoButton);
        Button AI = (Button) findViewById(R.id.AIButton);
        Button draw = (Button) findViewById(R.id.drawButton);

        Context context = this;

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
            }
        });

        resign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chessGame.setGameOver(true);
                endGame();
            }
        });
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chessGame.setGameOver(true);
                drawBool = true;
                endGame();
            }
        });
        AI.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                chessGame.performRandomMove();
                updateBoardPieces(chessGame);
                if (chessGame.getGameManager().isGameOver(chessGame.getTurnColor())) {
                    chessGame.setGameOver(true);
                    endGame();
                }else{
                    if(chessGame.getGameManager().kingInCheck(chessGame.getTurnColor())){
                        checkToast.show();
                    }
                }
            }
        });
        undo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                int flag = chessGame.undoLastMove();
                if(flag == 1){
                    updateBoardPieces(chessGame);
                }
            }
        });
    }

    public static void endGame() {
        DialogInterface.OnClickListener saveDialog = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        android.app.AlertDialog.Builder textBuilder = new android.app.AlertDialog.Builder(chessBoard.getContext());
                        EditText input = new EditText(chessBoard.getContext());
                        input.setHint("Enter game name here...");
                        textBuilder.setView(input);
                        textBuilder.setPositiveButton("Save", (dialogInterface1, i1) -> {
                            String name = input.getText().toString();
                            LocalDate curr = LocalDate.now();
                            Chess.Pair<ArrayList<String[]>, LocalDate> movesAndDate = new Chess.Pair<>(chessGame.getMoveList(), curr);
                            HashMap<String, Chess.Pair<ArrayList<String[]>, LocalDate>> oldGames = HomeActivity.getOldGames();
                            oldGames.put(name, movesAndDate);
                            HomeActivity.setOldGames(oldGames);
                            try {
                                File directory = new File(chessBoard.getContext().getFilesDir(), "data");
                                if (!directory.exists()) {
                                    directory.mkdir();
                                }
                                ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(new File(directory, "application.dat")));
                                stream.writeObject(HomeActivity.getOldGames());
                                stream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(chessBoard.getContext(), MainActivity.class);
                            chessBoard.getContext().startActivity(intent);
                        }).setCancelable(false).show();
                        break;
                }
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(chessBoard.getContext());
        if (drawBool) {
            builder.setMessage("Draw!");
            drawBool = false;
        } else {
            String winner = chessGame.getTurnColor() =='w'?"Black":"White";
            builder.setMessage(winner + " won!");
        }
        builder.setPositiveButton("Save Game", saveDialog).setNegativeButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(chessBoard.getContext(), MainActivity.class);
                chessBoard.getContext().startActivity(intent);
            }

        }).show();
    }
    public static int makeMove(String piece, String move){
        return chessGame.makeMove(piece, move);
    }
    public static int makeMove(String piece, String move, String promotion){
        return chessGame.makeMove(piece, move, promotion);
    }
}
class Square{
    LinearLayout square;
    String squareLoc;



    Piece piece;

    public Square(String pieceMove, LinearLayout square, Piece piece) {
        this.square = square;
        this.squareLoc = pieceMove;
        this.piece= piece;
    }

    public LinearLayout getSquare() {
        return square;
    }

    public void setSquare(LinearLayout square) {
        this.square = square;
    }

    public String getSquareLoc() {
        return squareLoc;
    }
    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    public void setSquareLoc(String squareLoc) {
        this.squareLoc = squareLoc;
    }
}