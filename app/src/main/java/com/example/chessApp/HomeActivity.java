package com.example.chessApp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import chess.Chess;

public class HomeActivity extends AppCompatActivity {

    private Button newGame;
    private Button viewGames;
    private static HashMap<String, Chess.Pair<ArrayList<String[]>, LocalDate>> oldGames = new HashMap<>();

    public static HashMap<String, Chess.Pair<ArrayList<String[]>, LocalDate>> getOldGames() {
        return oldGames;
    }

    public static void setOldGames(HashMap<String, Chess.Pair<ArrayList<String[]>, LocalDate>> temp) {
        oldGames = temp;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        newGame = (Button) findViewById(R.id.newGame);
        viewGames = (Button) findViewById(R.id.viewGames);
        readData();

        Context context = this;

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
        viewGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PopupMenu popup = new PopupMenu(viewGames.getContext(), viewGames);
//                Set<String> temp = oldGames.keySet();
//                String[] names = Arrays.copyOf(temp.toArray(), temp.toArray().length, String[].class);
//                for (String curr : names) {
//                    popup.getMenu().add(curr);
//                }
//                popup.show();
//                // For when we implement replayability:
//                // popup.setOnMenuItemClickListener
                Intent intent = new Intent(context, SavedGamesActivity.class);
                startActivity(intent);
            }
        });

    }

    public void readData() {
        try {
            String path = viewGames.getContext().getFilesDir() + "/data/" + "application.dat";
            File data = new File(path);
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(data));
            oldGames = (HashMap<String, Chess.Pair<ArrayList<String[]>, LocalDate>>) stream.readObject();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}