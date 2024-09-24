package com.example.chessApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chess.Chess;
import chess.Game;
import chess.Piece;

public class SavedGamesAdapter extends RecyclerView.Adapter<SavedGamesAdapter.ViewHolder> {

    private List<Game> games;
    private Context context;

    @NonNull
    @Override
    public SavedGamesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_game, parent, false);
        return new ViewHolder(v);

    }

    public SavedGamesAdapter(Context context, List<Game> games) {
        this.games = games;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedGamesAdapter.ViewHolder holder, int position) {
        holder.gameTitle.setText("Title: " + games.get(position).getTitle());
        holder.gameDate.setText("Date: " + games.get(position).getDate().toString());
        holder.viewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGameActivity.setSelectedGame(games.get(position));
                Intent intent = new Intent(view.getContext(), ViewGameActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView gameTitle;
        public TextView gameDate;

        public Button viewGame;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            gameDate = itemView.findViewById(R.id.gameDate);
            viewGame = itemView.findViewById(R.id.viewGame);
        }
    }
}
