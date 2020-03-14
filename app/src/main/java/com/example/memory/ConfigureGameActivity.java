package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class ConfigureGameActivity extends Activity {
    ArrayList<Player> players;
    RecyclerView recyclerView;
    private int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_configure_game);

        recyclerView = findViewById(R.id.recyclerView);
        players = new ArrayList<>();
        players.add(new Player(Player.TYPE_HUMAN));
        players.add(new Player(Player.TYPE_COMP_BEGINNER));

        updateUI();
    }


    protected void updateUI() {
        PlayerTileAdapter myAdapter = new PlayerTileAdapter(this, players);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void addPlayerClicked(View view) {
        players.add(new Player(Player.TYPE_HUMAN));
        updateUI();
    }

    public static class Player {
        public static final int TYPE_HUMAN = 0, TYPE_COMP_BEGINNER = 1, TYPE_COMP_MEDIUM = 2, TYPE_COMP_GOD = 3;
        int color;
        int type;
        public Player() {

        }
        public Player(int type) {
            this.type = type;
        }
        public Player(int color, int type) {
            this.color = color;
            this.type = type;
        }
    }
}
