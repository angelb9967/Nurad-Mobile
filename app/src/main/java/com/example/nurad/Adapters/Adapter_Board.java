package com.example.nurad.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nurad.Models.BoardModel;
import com.example.nurad.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Board extends RecyclerView.Adapter<Adapter_Board.ViewHolder> {
    private List<BoardModel> boardList;

    public Adapter_Board(List<BoardModel> boardList) {
        this.boardList = boardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_of_directors_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BoardModel director = boardList.get(position);
        // Bind data to views
        Glide.with(holder.itemView.getContext())
                .load(director.getImageUrl())
                .placeholder(R.drawable.person)
                .into(holder.directorPhoto);
        holder.directorName.setText(director.getName());
        holder.directorPosition.setText(director.getPosition());
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView directorPhoto;
        TextView directorName;
        TextView directorPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            directorPhoto = itemView.findViewById(R.id.directorPhoto);
            directorName = itemView.findViewById(R.id.directorName);
            directorPosition = itemView.findViewById(R.id.directorPosition);
        }
    }
}