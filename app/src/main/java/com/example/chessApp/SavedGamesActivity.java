package com.example.chessApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import chess.Chess;
import chess.Game;

public class SavedGamesActivity extends AppCompatActivity {

    private SavedGamesAdapter adapter;
    private HashMap<String, Chess.Pair<ArrayList<String[]>, LocalDate>> savedGamesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games);

        readData();
        if(savedGamesMap == null){
            savedGamesMap = new HashMap<>();
        }
        List<Game> savedGames = new ArrayList<>();
        for(String key: savedGamesMap.keySet()){
            List<String[]> moveList =  savedGamesMap.get(key).getFirst();
            LocalDate date =  savedGamesMap.get(key).getSecond();
            savedGames.add(new Game(key, moveList, date));
        }

        adapter = new SavedGamesAdapter(this, savedGames);
        RecyclerView savedGameList = (RecyclerView) findViewById(R.id.savedGameList);
        savedGameList.setLayoutManager(new LinearLayoutManager(this));
        savedGameList.setAdapter(adapter);

        Button homeButton = (Button) findViewById(R.id.homeButton);
        Context context = this;
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
            }
        });

        Button dateButton = (Button) findViewById(R.id.dateButton);
        Button titleButton = (Button) findViewById(R.id.titleButton);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                savedGames.sort(new Comparator<Game>() {
                    @Override
                    public int compare(Game g1, Game g2) {
                        return g1.getDate().compareTo(g2.getDate());
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });
        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                savedGames.sort(new Comparator<Game>() {
                    @Override
                    public int compare(Game g1, Game g2) {
                        return g1.getTitle().compareTo(g2.getTitle());
                    }
                });
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void readData() {
        try {
            String path = this.getFilesDir() + "/data/" + "application.dat";
            File data = new File(path);
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(data));
            savedGamesMap = (HashMap<String, Chess.Pair<ArrayList<String[]>, LocalDate>>) stream.readObject();
            stream.close();
        } catch (Exception e) {
        }
    }



}