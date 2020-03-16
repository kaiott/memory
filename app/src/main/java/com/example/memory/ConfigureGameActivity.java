package com.example.memory;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

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
        int n = 6;
        int m = 6;
        int cardsLeft = n*m;
        boolean randomOrder = false;
        ArrayList<Integer> objectA = new ArrayList<>(36);
        int[] status = new int[n*m];
        int turnPlayer = 0;
        for (int i = 0; i < 9; i ++) {
            objectA.add(i);
            objectA.add(i);
            objectA.add(i);
            objectA.add(i);
        }
        Collections.shuffle(objectA);
        GameState state = new GameState(players,objectA,n,m,turnPlayer,cardsLeft,status, randomOrder);
        intent.putExtra("state", state);
        startActivity(intent);
    }

    public void backClicked(View view) {
        finish();
    }



}
