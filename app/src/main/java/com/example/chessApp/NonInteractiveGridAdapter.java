package com.example.chessApp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import chess.Piece;

public class NonInteractiveGridAdapter extends RecyclerView.Adapter<NonInteractiveGridAdapter.ViewHolder> {

    private List<Piece> pieces;
    private Context context;

    @NonNull
    @Override
    public NonInteractiveGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(v);

    }

    public NonInteractiveGridAdapter(Context context, List<Piece> pieces) {
        this.pieces = pieces;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull NonInteractiveGridAdapter.ViewHolder holder, int position) {
        Piece piece = pieces.get(position);
        int row = position/8;
        int col = position % 8;
        if((row+col) % 2 == 1){
            // dark color
            holder.linearLayout.setBackgroundColor(Color.parseColor("#c97b42"));
        }else{
            // light color
            holder.linearLayout.setBackgroundColor(Color.parseColor("#ffcca6"));
        }

        if(piece != null && piece.getImageID() != -1){
            holder.imageView.setImageResource(piece.getImageID());
        }else{
            holder.imageView.setImageDrawable(null);
        }

        String squareLoc = (char)('a' + col) + "" + (8 - row);

    }

    @Override
    public int getItemCount() {
        return pieces.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout linearLayout;
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.grid_image);
            this.linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }
    }
}
