package com.example.memory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class PlayerTileAdapter extends RecyclerView.Adapter<PlayerTileAdapter.EventViewHolder> {

    private static final String TAG = "PlayerTileAdapter";

    private int[] colors = {Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.BLUE};
    ArrayList<Player> players;
    private Context context;

    public PlayerTileAdapter(Context ct, ArrayList<Player> players) {
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
        players.get(position).setNumber(position+1);
        players.get(position).setColor(colors[position]);

        final EventViewHolder reference_copy = holder;

        setTypeIcon(holder, position);

        holder.playerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference_copy.playerTypeSpinner.setVisibility(View.VISIBLE);
                reference_copy.playerTypeSpinner.performClick();
            }
        });

        holder.colorView.setBackgroundColor(players.get(position).getColor());
        holder.colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

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

        holder.playerText.setText(String.format(Locale.ENGLISH, "%s %d", context.getString(R.string.player), players.get(position).getNumber()));

        holder.playerTypeSpinner.setVisibility(View.INVISIBLE);
        final String[] themes = {context.getString(R.string.human), context.getString(R.string.ai_beginner), context.getString(R.string.ai_medium),context.getString(R.string.ai_god)};
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, themes);
        holder.playerTypeSpinner.setAdapter(mAdapter);
        holder.playerTypeSpinner.setSelection(players.get(position).getType(),false);
        holder.playerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int type, long id) {
                Log.i(TAG, "onItemSelected: position: " + position + ", type apriori:" + type);

                players.set(position, PlayerFactory.changeType(players.get(position), type));
                Log.i(TAG, "onItemSelected: position: " + position + ", type after change:" + players.get(position).getType());
                setTypeIcon(reference_copy, position);
                reference_copy.playerTypeSpinner.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                reference_copy.playerTypeSpinner.setVisibility(View.GONE);
            }
        });
    }

    private void setTypeIcon(@NonNull EventViewHolder holder, final int position) {
        Log.i(TAG, "onItemSelected: position + " + position + ", type:" + players.get(position).getType());
        switch (players.get(position).getType()) {
            case Player.TYPE_HUMAN:
                Log.i(TAG, "setTypeIcon: human");
                holder.playerImage.setImageResource(R.drawable.ic_person_black_24dp);
                break;
            case Player.TYPE_COMP_BEGINNER:
                Log.i(TAG, "setTypeIcon: beginner");
                holder.playerImage.setImageResource(R.drawable.ic_computer_black_24dp);
                break;
            case Player.TYPE_COMP_MEDIUM:
                Log.i(TAG, "setTypeIcon: medium");
                holder.playerImage.setImageResource(R.drawable.ai2);
                break;
            case Player.TYPE_COMP_GOD:
                Log.i(TAG, "setTypeIcon: god");
                holder.playerImage.setImageResource(R.drawable.ai3);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{

        TextView colorView, playerText;
        ImageView playerImage, deleteImage;
        Spinner playerTypeSpinner;
        ConstraintLayout constraintLayout;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImage = itemView.findViewById(R.id.player_image);
            colorView = itemView.findViewById(R.id.colorView);
            deleteImage = itemView.findViewById(R.id.deleteView);
            playerText = itemView.findViewById(R.id.player_text_view);
            playerTypeSpinner = itemView.findViewById(R.id.spinner_type_player);
        }
    }
}
