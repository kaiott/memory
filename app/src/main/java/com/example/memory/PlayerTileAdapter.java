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

public class PlayerTileAdapter extends RecyclerView.Adapter<PlayerTileAdapter.EventViewHolder> {

    private int numPlayers;
    private int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};
    ArrayList<ConfigureGameActivity.Player> players;
    private Context context;

    public PlayerTileAdapter(Context ct, ArrayList<ConfigureGameActivity.Player> players) {
        context = ct;
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
        holder.playerImage.setImageResource(R.drawable.ic_person_black_24dp);
        holder.colorView.setBackgroundColor(colors[position]);
        if (position != 0) {
            holder.deleteImage.setImageResource(R.drawable.ic_close_red_48dp);
            holder.deleteImage.setVisibility(View.VISIBLE);
        }
        else holder.deleteImage.setVisibility(View.INVISIBLE);

        /*final Event e = Event.allEvents.get(ids[position]);
        holder.titleText.setText(e.getTitle());
        holder.timeText.setText(Event.dayToString(e.getStartDate(), Event.NAME_AND_DAY_AND_TIME));
        holder.locationText.setText(e.getLocation());
        holder.logoView.setImageResource(Event.imagesrc[e.getCompanyID()]);
        if (e.isFavourite()) {
            holder.favView.setVisibility(View.VISIBLE);
        }
        else {
            holder.favView.setVisibility(View.GONE);
        }
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (e.getId() != 7) {
                    Intent intent = new Intent(context, EventDetailsActivity.class);
                    intent.putExtra("event_id", e.getId());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, TicTacToeActivity.class);
                    intent.putExtra("event_id", e.getId());
                    context.startActivity(intent);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{

        TextView colorView, locationText;
        ImageView playerImage, deleteImage;
        ConstraintLayout constraintLayout;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImage = itemView.findViewById(R.id.player_image);
            colorView = itemView.findViewById(R.id.colorView);
            deleteImage = itemView.findViewById(R.id.deleteView);
            /*titleText = itemView.findViewById(R.id.titleText_places);
            timeText = itemView.findViewById(R.id.timeText);
            locationText = itemView.findViewById(R.id.locationText_places);
            logoView = itemView.findViewById(R.id.logoImage);
            favView = itemView.findViewById(R.id.favView);
            constraintLayout = itemView.findViewById(R.id.mainLayout)*/;
        }
    }
}
