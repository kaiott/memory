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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class GameActivity extends BaseFullscreenActivity {

    private final static String TAG = "GameActivity";
    public final static int COVERED = 0, VISIBLE = 1, TURNED = 2, TAKEN = 3;

    Guideline [] verticalGuides = new Guideline[5];
    TextView playerBody, pointsBody, playerOverview;

    ArrayList<ImageView> cards;
    ArrayList<Player> players;
    ArrayList<Integer> objectA;

    int n, m, numTries, oldPosition, turnPlayer, cardsLeft;
    int [] boardStatus, catIDs = {R.drawable.cat0,R.drawable.cat1,R.drawable.cat2,R.drawable.cat3,R.drawable.cat4,R.drawable.cat5,R.drawable.cat6,R.drawable.cat7,R.drawable.cat8};
    boolean randomOrder;
    int durationFadeOut, durationComputerThink;
    int[][] buckets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getAndSetData();
        initializeMembers();
    }

    protected void addToBucket(int position) {
        int bucketNumber = objectA.get(position);
        int[] bucket = buckets[bucketNumber];
        boolean found = false;
        for (int i = 0; i < bucket.length-1; i++) {
            if (bucket[i] == position) {
                found = true;
                break;
            }
        }
        if (!found) {
            for (int i = 0; i < bucket.length-1; i++) {
                if (bucket[i] == -1) {
                    bucket[i] = position;
                    bucket[bucket.length-1]++;
                    break;
                }
            }
        }
    }
    protected void removeFromBucket(int position) {
        int bucketNumber = objectA.get(position);
        int[] bucket = buckets[bucketNumber];
        for (int i = 0; i < bucket.length-1; i++) {
            if (bucket[i] == position) {
                bucket[i] = -1;
                bucket[bucket.length-1]--;
                break;
            }
        }
    }

    protected void getAndSetData() {
        if (getIntent().hasExtra("state")) {
            durationFadeOut = 1000;
            durationComputerThink = 800;
            buckets = new int[9][5];
            for (int[] bucket : buckets) {
                Arrays.fill(bucket, -1);
                bucket[bucket.length-1] = 0;
            }
            Log.i(TAG, "getAndSetData: trying to set up game");
            Serializable s = getIntent().getSerializableExtra("state");
            GameState state = (GameState) s;
            assert state != null;
            Log.i(TAG, "getAndSetData: conversion correct");
            this.n = state.n;
            this.m = state.m;
            this.cardsLeft = state.cardsLeft;
            this.randomOrder = state.randomOrder;
            this.objectA = state.objectA;
            this.players = state.players;
            this.turnPlayer = state.turnPlayer-1; // -1 because in initialize we call nextPlayer() which increments the turn
            this.boardStatus = state.status;
            if (this.randomOrder) {
                Collections.shuffle(this.players);
            }
        }
        else {
            Log.i(TAG, "getAndSetData: No extra received, cannot create game");
            finish();
        }
    }

    public void cardSelected(int position) {
        if ((boardStatus[position] == COVERED || boardStatus[position] == TURNED) && numTries < 2 && players.get(turnPlayer).getType() == Player.TYPE_HUMAN) {
            turnCard(position);
        }
    }

    public void turnCard(int position) {
        Log.i(TAG, "turnCard: turning card by player of type " + players.get(turnPlayer).getType());
        Log.i(TAG, "turnCard: position to turn over: " + position);
        numTries++;
        cards.get(position).setImageResource(catIDs[objectA.get(position)]);
        boardStatus[position] = VISIBLE;
        Log.i(TAG, String.format(Locale.ENGLISH, "value %d discovered", objectA.get(position)));
        if (numTries == 1) {
            Log.i(TAG, "turnCard: first try");
            oldPosition = position;
        }
        if (numTries == 2) {
            Log.i(TAG, "turnCard: second try");
            if (objectA.get(oldPosition).equals(objectA.get(position))) {
                addPoints();
                cardsLeft -= 2;
                boardStatus[oldPosition] = TAKEN;
                boardStatus[position] = TAKEN;
                removeFromBucket(oldPosition);
                removeFromBucket(position);
                
            }
            else {
                boardStatus[oldPosition] = TURNED;
                boardStatus[position] = TURNED;
                addToBucket(oldPosition);
                addToBucket(position);
            }
            Log.i(TAG, "turnCard: buckets: " + Arrays.deepToString(buckets));
            new EndTurnTask().execute(oldPosition, position);
        }
    }

    private class LetComputerDoTurn extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... args) {
            Log.i(TAG, "doInBackground: computer turn started");
            try {
                Thread.sleep(durationComputerThink);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return players.get(turnPlayer).makeMove(boardStatus, buckets, oldPosition == -1 ? -1 : objectA.get(oldPosition));
        }
        protected void onPostExecute(Integer result) {
            turnCard(result);
            if (numTries < 2) {
                new LetComputerDoTurn().execute();
            }
        }
    }

    private class EndTurnTask extends AsyncTask<Integer, Void, Object> {
        int pos1, pos2;
        protected Object doInBackground(Integer... args) {
            pos1 = args[0];
            pos2 = args[1];
            try {
                Thread.sleep(durationFadeOut);
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
                boardStatus[pos1] = COVERED;
                boardStatus[pos2] = COVERED;
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
        if (cardsLeft == 0) {
            endGame();
            return;
        }
        numTries = 0;
        oldPosition = -1;
        if (players.get(turnPlayer).getType() != Player.TYPE_HUMAN) {
            new LetComputerDoTurn().execute();
        }
    }

    private void nextPlayer() {
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
        // TODO: show results of match, winner, ranking etc
        // TODO: Update statistics (either bare SharedPreferences or in statistics class)
    }

    protected void updateOverview() {
        SpannableString overview = new SpannableString("");
        for (Player player : players) {
            SpannableString s = new SpannableString(String.format(Locale.ENGLISH, "%s %d:  %d\n", getString(R.string.player), player.getNumber(), player.getPoints()));
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
            if(boardStatus[pos] == TAKEN || this.row(pos) >= n  || this.col(pos) >= n ) {
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
        mBuilder.setTitle(R.string.return_home_title)
                .setMessage(R.string.return_home_body)
                .setNegativeButton(R.string.return_home_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.return_home_leave, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (cardsLeft != 0) {
                            saveGameState();
                        }
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
        GameState state = new GameState(players, objectA, n, m, turnPlayer, cardsLeft, boardStatus, false);
        GsonBuilder gsonBilder = new GsonBuilder().setPrettyPrinting();
        gsonBilder.registerTypeAdapter(Player.class, new PlayerAdapter());
        Gson gson = gsonBilder.create();

        SharedPreferences preferences = getSharedPreferences("game_states", MODE_PRIVATE);
        preferences.edit().putString("state", gson.toJson(state)).apply();
    }

}
