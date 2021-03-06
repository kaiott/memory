package com.example.memory;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

public class ConfigureGameActivity extends BaseFullscreenActivity {
    static ArrayList<Player> players;
    RecyclerView recyclerView;
    ImageView addPlayer;
    int n, m;
    CheckBox randomOrderCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_configure_game);

        recyclerView = findViewById(R.id.recyclerView);
        addPlayer = findViewById(R.id.button_add_player);
        players = new ArrayList<>();
        players.add(PlayerFactory.getInstance(Player.TYPE_HUMAN));
        players.add(PlayerFactory.getInstance(Player.TYPE_COMP_GOD));

        randomOrderCheckBox = findViewById(R.id.checkBox_random_order);

        Spinner boardSize = findViewById(R.id.spinner_board_size);
        final String[] themes = {"4x3", "4x4", "5x4", "6x5", "6x6"};
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, themes);
        boardSize.setAdapter(mAdapter);
        n = 5; m = 4;
        boardSize.setSelection(2,false);
        boardSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: n = 4; m = 3; break;
                    case 1: n = 4; m = 4; break;
                    case 2: n = 5; m = 4; break;
                    case 3: n = 6; m = 5; break;
                    case 4: n = 6; m = 6; break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
        players.add(PlayerFactory.getInstance(Player.TYPE_HUMAN));
        updateUI();
    }

    public void deletePlayerClicked(final int position) {
        players.remove(position);
        updateUI();
    }

    public void playClicked(View view) {
        Intent intent = new Intent(view.getContext(), GameActivity.class);
        int cardsLeft = n*m;
        ArrayList<Integer> objectA = new ArrayList<>(n*m);
        int[] status = new int[n*m];
        int turnPlayer = 0;
        int totalNumberOfTurns= 0;
        int numBuckets = Math.min(m*n/2, CardSets.getSetSize(cardSet));
        for (int i = 0; i < m*n/2; i ++) {
            objectA.add(i%numBuckets);
            objectA.add(i%numBuckets);
            if (objectA.size() >= m*n) {
                break;
            }
        }
        Collections.shuffle(objectA);
        if (randomOrderCheckBox.isChecked()) {
            Collections.shuffle(players);
        }
        GameState state = new GameState(players,objectA,n,m,turnPlayer,cardsLeft,status,totalNumberOfTurns);
        if (players.size() > 1) {
            Statistics.addStartedGame();
            Statistics.saveStatistics(ConfigureGameActivity.this);
        }
        intent.putExtra("state", state);
        startActivity(intent);
    }

    public void backClicked(View view) {
        finish();
    }


}
