package com.example.memory;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Guideline;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class GameActivity extends BaseFullscreenActivity {

    private final static String TAG = "GameActivity";
    public final static int COVERED = 0, TURNED = 1, TAKEN = 2;

    Guideline [] verticalGuides = new Guideline[5];
    TextView playerBody, pointsBody, playerOverview;

    ArrayList<ImageView> cards;
    ArrayList<Player> players;
    ArrayList<Integer> objectA;

    int n, m, numTries, oldPosition, turnPlayer, cardsLeft;
    int [] status, catIDs = {R.drawable.cat0,R.drawable.cat1,R.drawable.cat2,R.drawable.cat3,R.drawable.cat4,R.drawable.cat5,R.drawable.cat6,R.drawable.cat7,R.drawable.cat8};
    boolean randomOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getAndSetData();
        initializeMembers();
    }

    protected void getAndSetData() {
        if(getIntent().hasExtra("players")) {
            Log.i(TAG, "getAndSetData: there is an extra player");
            Serializable s = getIntent().getSerializableExtra("players");
            players = (ArrayList<Player>) s;
            n = 6;
            m = 6;
            cardsLeft = n*m;
            randomOrder = false;
            objectA = new ArrayList<>(36);
            status = new int[n*m];
            turnPlayer = -1;
            for (int i = 0; i < 9; i ++) {
                objectA.add(i);
                objectA.add(i);
                objectA.add(i);
                objectA.add(i);
            }
            Collections.shuffle(objectA);
        }
        else if (getIntent().hasExtra("previous_state")) {
            Log.i(TAG, "getAndSetData: trying to restore game");
            String previousState = getIntent().getStringExtra("previous_state");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type type = new TypeToken<GameState>() {}.getType();
            GameState state = gson.fromJson(previousState, type);
            assert state != null;
            Log.i(TAG, "getAndSetData: conversion correct");
            this.n = state.n;
            this.m = state.m;
            this.cardsLeft = state.cardsLeft;
            this.randomOrder = state.randomOrder;
            this.objectA = state.objectA;
            this.players = state.players;
            this.turnPlayer = state.turnPlayer-1; // -1 because in initialize we call nextPlayer() which increments the turn
            this.status = state.status;
        }
    }

    public void cardSelected(int position) {
        if (status[position] == COVERED && numTries < 2) {
            turnCard(position);
        }
    }

    public void turnCard(int position) {
        numTries++;
        cards.get(position).setImageResource(catIDs[objectA.get(position)]);
        status[position] = TURNED;
        Toast.makeText(this, String.format(Locale.ENGLISH, "value %d discovered", objectA.get(position)), Toast.LENGTH_SHORT).show();
        if (numTries == 1) {
            oldPosition = position;
        }
        if (numTries == 2) {
            if (objectA.get(oldPosition).equals(objectA.get(position))) {
                addPoints();
                cardsLeft -= 2;
                status[oldPosition] = TAKEN;
                status[position] = TAKEN;
            }
            else {
                status[oldPosition] = COVERED;
                status[position] = COVERED;
            }
            new EndTurnTask().execute(oldPosition, position);
        }
    }

    private class EndTurnTask extends AsyncTask<Integer, Void, Object> {
        int pos1, pos2;
        protected Object doInBackground(Integer... args) {
            pos1 = args[0];
            pos2 = args[1];
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        protected void onPostExecute(Object result) {
            if (objectA.get(pos1).equals(objectA.get(pos2))) {
                cards.get(pos1).setVisibility(View.INVISIBLE);
                cards.get(pos2).setVisibility(View.INVISIBLE);
                nextTurn();
            }
            else {
                status[pos1] = COVERED;
                status[pos2] = COVERED;
                cards.get(pos1).setImageResource(R.drawable.card_back);
                cards.get(pos2).setImageResource(R.drawable.card_back);
                nextPlayer();
            }
        }
    }

    private void addPoints() {
        players.get(turnPlayer).addPoint();
        pointsBody.setText(""+players.get(turnPlayer).getPoints());
        updateOverview();
    }

    private void nextTurn() {
        numTries = 0;
        oldPosition = -1;
    }

    private void nextPlayer() {
        if (cardsLeft == 0) {
            endGame();
            return;
        }
        turnPlayer++;
        turnPlayer %= players.size();
        playerBody.setText("Player " + turnPlayer);
        playerBody.setTextColor(players.get(turnPlayer).getColor());
        pointsBody.setText(""+players.get(turnPlayer).getPoints());
        pointsBody.setTextColor(players.get(turnPlayer).getColor());
        updateOverview();
        nextTurn();
    }

    private void endGame() {
        getSharedPreferences("game_states", MODE_PRIVATE).edit().clear().apply();
    }

    protected void updateOverview() {
        SpannableString overview = new SpannableString("");
        for (Player player : players) {
            SpannableString s = new SpannableString(String.format(Locale.ENGLISH, "Player %d:  %d\n", player.getNumber(), player.getPoints()));
            s.setSpan(new ForegroundColorSpan(player.getColor()), 0, s.length(), 0);
            overview = new SpannableString(TextUtils.concat(overview, s));
        }
        playerOverview.setText(overview, TextView.BufferType.SPANNABLE);
    }


    private int row(int position) {
        return position/m;
    }
    private int col(int position) {
        return position % m;
    }
    private int position(int row, int column) { return m*row + column; }

    public void makeGrid(int n) {
        Log.i(TAG, "makeGrid: setting grid to n =" +n);
        for (int i = 0; i< verticalGuides.length; i++) {
            verticalGuides[i].setGuidelinePercent((i + 1f )/n);
        }
        for (int pos = 0; pos < n*m; pos++) {
            if(status[pos] == TAKEN || this.row(pos) >= n  || this.col(pos) >= n ) {
                cards.get(pos).setVisibility(View.INVISIBLE);
            }
        }
    }

    protected void initializeMembers() {
        cards = new ArrayList<>();
        cards.add((ImageView) findViewById(R.id.card0));
        cards.add((ImageView) findViewById(R.id.card1));
        cards.add((ImageView) findViewById(R.id.card2));
        cards.add((ImageView) findViewById(R.id.card3));
        cards.add((ImageView) findViewById(R.id.card4));
        cards.add((ImageView) findViewById(R.id.card5));
        cards.add((ImageView) findViewById(R.id.card6));
        cards.add((ImageView) findViewById(R.id.card7));
        cards.add((ImageView) findViewById(R.id.card8));
        cards.add((ImageView) findViewById(R.id.card9));
        cards.add((ImageView) findViewById(R.id.card10));
        cards.add((ImageView) findViewById(R.id.card11));
        cards.add((ImageView) findViewById(R.id.card12));
        cards.add((ImageView) findViewById(R.id.card13));
        cards.add((ImageView) findViewById(R.id.card14));
        cards.add((ImageView) findViewById(R.id.card15));
        cards.add((ImageView) findViewById(R.id.card16));
        cards.add((ImageView) findViewById(R.id.card17));
        cards.add((ImageView) findViewById(R.id.card18));
        cards.add((ImageView) findViewById(R.id.card19));
        cards.add((ImageView) findViewById(R.id.card20));
        cards.add((ImageView) findViewById(R.id.card21));
        cards.add((ImageView) findViewById(R.id.card22));
        cards.add((ImageView) findViewById(R.id.card23));
        cards.add((ImageView) findViewById(R.id.card24));
        cards.add((ImageView) findViewById(R.id.card25));
        cards.add((ImageView) findViewById(R.id.card26));
        cards.add((ImageView) findViewById(R.id.card27));
        cards.add((ImageView) findViewById(R.id.card28));
        cards.add((ImageView) findViewById(R.id.card29));
        cards.add((ImageView) findViewById(R.id.card30));
        cards.add((ImageView) findViewById(R.id.card31));
        cards.add((ImageView) findViewById(R.id.card32));
        cards.add((ImageView) findViewById(R.id.card33));
        cards.add((ImageView) findViewById(R.id.card34));
        cards.add((ImageView) findViewById(R.id.card35));
        verticalGuides[0] = findViewById(R.id.guidelineV1);
        verticalGuides[1] = findViewById(R.id.guidelineV2);
        verticalGuides[2] = findViewById(R.id.guidelineV3);
        verticalGuides[3] = findViewById(R.id.guidelineV4);
        verticalGuides[4] = findViewById(R.id.guidelineV5);
        for (ImageView card : cards) {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cardSelected(cards.indexOf(view));
                    //n = 11 - n;
                    //changeCards(n);
                }
            });
        }

        makeGrid(n);

        playerBody = findViewById(R.id.player_body);
        pointsBody = findViewById(R.id.points_body);
        playerOverview = findViewById(R.id.player_overview);

        updateOverview();
        nextPlayer();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);
        mBuilder.setTitle("Return Home?")
                .setMessage("Do you really want to leave? You can continue this game later in the Home screen with \'continue game\' ")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveGameState();
                        Intent intent = new Intent(mBuilder.getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
        mBuilder.create();
        mBuilder.show();
    }

    private void saveGameState() {
        GameState state = new GameState(players, objectA, n, m, turnPlayer, cardsLeft, status, randomOrder);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SharedPreferences preferences = getSharedPreferences("game_states", MODE_PRIVATE);
        preferences.edit().putString("state", gson.toJson(state)).apply();
    }

    public static class GameState implements Serializable {
        ArrayList<Player> players;
        ArrayList<Integer> objectA;
        int n, m, turnPlayer, cardsLeft;
        int [] status;
        boolean randomOrder;

        public GameState(ArrayList<Player> players, ArrayList<Integer> objectA, int n, int m, int turnPlayer, int cardsLeft, int[] status, boolean randomOrder) {
            this.players = players;
            this.objectA = objectA;
            this.n = n;
            this.m = m;
            this.turnPlayer = turnPlayer;
            this.cardsLeft = cardsLeft;
            this.status = status;
            this.randomOrder = randomOrder;
        }
    }

}
