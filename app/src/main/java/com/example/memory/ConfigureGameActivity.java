package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

public class ConfigureGameActivity extends BaseFullscreenActivity {
    static ArrayList<Player> players;
    RecyclerView recyclerView;
    ImageView addPlayer;
    private int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_configure_game);

        recyclerView = findViewById(R.id.recyclerView);
        addPlayer = findViewById(R.id.button_add_player);
        players = new ArrayList<>();
        players.add(new Player(Player.TYPE_HUMAN));
        players.add(new Player(Player.TYPE_COMP_BEGINNER));

        updateUI();
    }


    protected void updateUI() {
        PlayerTileAdapter myAdapter = new PlayerTileAdapter(this, players);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (players.size() < 6) {
            addPlayer.setVisibility(View.VISIBLE);
        }
        else {
            addPlayer.setVisibility(View.INVISIBLE);
        }
    }

    public void addPlayerClicked(View view) {
        players.add(new Player(Player.TYPE_HUMAN));
        updateUI();
    }

    public void deletePlayerClicked(final int position) {
        players.remove(position);
        updateUI();
    }

    public void playClicked(View view) {
        Intent intent = new Intent(view.getContext(), GameActivity.class);
        //intent.putExtra("players", players);
        startActivity(intent);
    }

    public void backClicked(View view) {
        finish();
    }


    public static class Player {
        public static final int TYPE_HUMAN = 0, TYPE_COMP_BEGINNER = 1, TYPE_COMP_MEDIUM = 2, TYPE_COMP_GOD = 3;
        int color;
        int type;
        public Player() {

        }
        public Player(int type) {
            this.color = -1;
            this.type = type;
        }
        public Player(int color, int type) {
            this.color = color;
            this.type = type;
        }
    }
}
