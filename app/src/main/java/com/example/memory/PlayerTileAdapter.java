package com.example.memory;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class PlayerTileAdapter extends RecyclerView.Adapter<PlayerTileAdapter.EventViewHolder> {

    private int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};
    ArrayList<Player> players;
    private Context context;

    public PlayerTileAdapter(Context ct, ArrayList<Player> players) {
        context = ct;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getColor() == -1) {
                players.get(i).setColor(colors[i]);
            }
        }
        this.players = players;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.player_tile, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, final int position) {
        players.get(position).setNumber(position);
        holder.playerImage.setImageResource(R.drawable.ic_person_black_24dp);
        holder.colorView.setBackgroundColor(players.get(position).getColor());
        if (position != 0) {
            holder.deleteImage.setImageResource(R.drawable.ic_close_red_48dp);
            holder.deleteImage.setVisibility(View.VISIBLE);
        }
        else holder.deleteImage.setVisibility(View.INVISIBLE);
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof ConfigureGameActivity) {
                    ((ConfigureGameActivity) context).deletePlayerClicked(position);
                }
            }
        });
        holder.playerText.setText(String.format(Locale.ENGLISH, "%s %d", context.getString(R.string.player), position+1));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{

        TextView colorView, playerText;
        ImageView playerImage, deleteImage;
        ConstraintLayout constraintLayout;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImage = itemView.findViewById(R.id.player_image);
            colorView = itemView.findViewById(R.id.colorView);
            deleteImage = itemView.findViewById(R.id.deleteView);
            playerText = itemView.findViewById(R.id.player_text_view);
        }
    }
}
