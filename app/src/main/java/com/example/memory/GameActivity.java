package com.example.memory;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Guideline;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    int n, m, numTries, oldPosition, turnPlayer, cardsLeft, totalNumberOfTurns;
    int [] boardStatus;
    SoundPool soundPool;
    int soundSuccess, soundFail;

    int[][] buckets;
    int [] set_image_sources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getAndSetData();
        initializeMembers();
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

    protected void saveGameState() {
        GameState state = new GameState(players, objectA, n, m, turnPlayer, cardsLeft, boardStatus, totalNumberOfTurns);
        GsonBuilder gsonBilder = new GsonBuilder().setPrettyPrinting();
        gsonBilder.registerTypeAdapter(Player.class, new PlayerAdapter());
        Gson gson = gsonBilder.create();

        SharedPreferences preferences = getSharedPreferences("game_states", MODE_PRIVATE);
        preferences.edit().putString("state", gson.toJson(state)).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }


    protected void getAndSetData() {
        if (getIntent().hasExtra("state")) {
            Log.i(TAG, "getAndSetData: trying to set up game");
            Serializable serializableExtra = getIntent().getSerializableExtra("state");
            GameState state = (GameState) serializableExtra;
            assert state != null;
            Log.i(TAG, "getAndSetData: conversion correct");
            this.n = state.n;
            this.m = state.m;
            this.cardsLeft = state.cardsLeft;
            this.objectA = state.objectA;
            this.players = state.players;
            this.turnPlayer = state.turnPlayer-1; // -1 because in initialize we call nextPlayer() which increments the turn
            this.boardStatus = state.status;
            this.totalNumberOfTurns = state.totalNumberOfTurns;
            Log.i(TAG, "getAndSetData: cardset size " + CardSets.getSetSize(cardSet));
            int numBuckets = Math.min(m*n/2, CardSets.getSetSize(cardSet));
            int s = m*n / numBuckets;
            if (s*numBuckets != m*n) {
                s++;
                s += s%2;
            }
            buckets = new int[numBuckets][s + 1];
            for (int[] bucket : buckets) {
                Arrays.fill(bucket, -1);
                bucket[bucket.length-1] = 0;
            }
        }
        else {
            Log.i(TAG, "getAndSetData: No extra received, cannot create game");
            finish();
        }
    }

    protected void initializeMembers() {
        //temporary local list of ImageViews, not to be confused with the global one
        ArrayList<ImageView> localCards = new ArrayList<>();
        localCards.add((ImageView) findViewById(R.id.card0));
        localCards.add((ImageView) findViewById(R.id.card1));
        localCards.add((ImageView) findViewById(R.id.card2));
        localCards.add((ImageView) findViewById(R.id.card3));
        localCards.add((ImageView) findViewById(R.id.card4));
        localCards.add((ImageView) findViewById(R.id.card5));
        localCards.add((ImageView) findViewById(R.id.card6));
        localCards.add((ImageView) findViewById(R.id.card7));
        localCards.add((ImageView) findViewById(R.id.card8));
        localCards.add((ImageView) findViewById(R.id.card9));
        localCards.add((ImageView) findViewById(R.id.card10));
        localCards.add((ImageView) findViewById(R.id.card11));
        localCards.add((ImageView) findViewById(R.id.card12));
        localCards.add((ImageView) findViewById(R.id.card13));
        localCards.add((ImageView) findViewById(R.id.card14));
        localCards.add((ImageView) findViewById(R.id.card15));
        localCards.add((ImageView) findViewById(R.id.card16));
        localCards.add((ImageView) findViewById(R.id.card17));
        localCards.add((ImageView) findViewById(R.id.card18));
        localCards.add((ImageView) findViewById(R.id.card19));
        localCards.add((ImageView) findViewById(R.id.card20));
        localCards.add((ImageView) findViewById(R.id.card21));
        localCards.add((ImageView) findViewById(R.id.card22));
        localCards.add((ImageView) findViewById(R.id.card23));
        localCards.add((ImageView) findViewById(R.id.card24));
        localCards.add((ImageView) findViewById(R.id.card25));
        localCards.add((ImageView) findViewById(R.id.card26));
        localCards.add((ImageView) findViewById(R.id.card27));
        localCards.add((ImageView) findViewById(R.id.card28));
        localCards.add((ImageView) findViewById(R.id.card29));
        localCards.add((ImageView) findViewById(R.id.card30));
        localCards.add((ImageView) findViewById(R.id.card31));
        localCards.add((ImageView) findViewById(R.id.card32));
        localCards.add((ImageView) findViewById(R.id.card33));
        localCards.add((ImageView) findViewById(R.id.card34));
        localCards.add((ImageView) findViewById(R.id.card35));
        verticalGuides[0] = findViewById(R.id.guidelineV1);
        verticalGuides[1] = findViewById(R.id.guidelineV2);
        verticalGuides[2] = findViewById(R.id.guidelineV3);
        verticalGuides[3] = findViewById(R.id.guidelineV4);
        verticalGuides[4] = findViewById(R.id.guidelineV5);

        makeGrid(n, m, localCards);

        playerBody = findViewById(R.id.player_body);
        pointsBody = findViewById(R.id.points_body);
        playerOverview = findViewById(R.id.player_overview);

        set_image_sources = CardSets.getSet(cardSet);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build();


        soundSuccess = soundPool.load(this, R.raw.success, 1);
        soundFail = soundPool.load(this, R.raw.fail3, 1);

        nextPlayer();
        updateOverview();
    }

    protected void makeGrid(int n, int m, ArrayList<ImageView> localCards) {
        cards = new ArrayList<>();
        Log.i(TAG, "makeGrid: setting grid to nxm = " + n +"x" +m);
        for (int i = 0; i< verticalGuides.length; i++) {
            verticalGuides[i].setGuidelinePercent((i + 1f )/m);
        }

        for (int localPos = 0; localPos < localCards.size(); localPos++) {
            if (localPos / 6 < n && localPos % 6 < m) {
                cards.add(localCards.get(localPos));
                Log.i(TAG, "makeGrid: card added, position "+ localPos + " row: " + row(localPos) + " col: " + col(localPos));
            }
            else {
                localCards.get(localPos).setVisibility(View.INVISIBLE);
            }
        }

        for (int pos = 0; pos < cards.size(); pos++) {
            if (boardStatus[pos] == TAKEN) {
                cards.get(pos).setVisibility(View.INVISIBLE);
            }
        }
        for (ImageView card : cards) {
            card.setImageResource(CardSets.getCardBack(cardBack));
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cardSelected(cards.indexOf(view));
                }
            });
        }
    }


    protected void nextTurn() {
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

    protected void nextPlayer() {
        turnPlayer++;
        turnPlayer %= players.size();
        playerBody.setText(String.format(Locale.ENGLISH, "%s %d",getString(R.string.player),  players.get(turnPlayer).getNumber()));
        playerBody.setTextColor(players.get(turnPlayer).getColor());
        pointsBody.setText(String.valueOf(players.get(turnPlayer).getPoints()));
        pointsBody.setTextColor(players.get(turnPlayer).getColor());
        updateOverview();
        nextTurn();
    }


    protected void cardSelected(int position) {
        if ((boardStatus[position] == COVERED || boardStatus[position] == TURNED) && numTries < 2 && players.get(turnPlayer).getType() == Player.TYPE_HUMAN) {
            turnCard(position);
        }
    }

    protected void turnCard(int position) {
        Log.i(TAG, "turnCard: turning card by player of type " + players.get(turnPlayer).getType());
        Log.i(TAG, "turnCard: position to turn over: " + position);
        numTries++;

        /*the modulo applied in the case that you start a game with set that has a lot of cards,
        go to settings, select fewer cards and continue, in any other case it does nothing*/
        cards.get(position).setImageResource(set_image_sources[objectA.get(position) % set_image_sources.length]);

        boardStatus[position] = VISIBLE;
        Log.i(TAG, String.format(Locale.ENGLISH, "value %d discovered", objectA.get(position)));
        if (numTries == 1) {
            Log.i(TAG, "turnCard: first try");
            oldPosition = position;
        }
        if (numTries == 2) {
            totalNumberOfTurns++;
            Log.i(TAG, "turnCard: second try");
            if (objectA.get(oldPosition).equals(objectA.get(position))) {
                if (soundPool != null && playSound) {
                    soundPool.play(soundSuccess, 1, 1, 0, 0, 1);
                }

                addPoints();
                cardsLeft -= 2;
                boardStatus[oldPosition] = TAKEN;
                boardStatus[position] = TAKEN;
                removeFromBucket(oldPosition);
                removeFromBucket(position);

            }
            else {
                if (soundPool != null && playSound) {
                    soundPool.play(soundFail, 1, 1, 0, 0, 1);
                }
                boardStatus[oldPosition] = TURNED;
                boardStatus[position] = TURNED;
                addToBucket(oldPosition);
                addToBucket(position);
            }
            Log.i(TAG, "turnCard: buckets: " + Arrays.deepToString(buckets));
            new EndTurnTask().execute(oldPosition, position);
        }
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

    protected void addPoints() {
        players.get(turnPlayer).addPoint();
        pointsBody.setText(String.valueOf(players.get(turnPlayer).getPoints()));
        updateOverview();
    }

    protected void updateOverview() {
        if (players.size() > 1) {
            findViewById(R.id.turns_used_header).setVisibility(View.INVISIBLE);
            findViewById(R.id.turns_used_value).setVisibility(View.INVISIBLE);
            SpannableString overview = new SpannableString("");
            for (Player player : players) {
                SpannableString s = new SpannableString(String.format(Locale.ENGLISH, "%s %d:  %d\n", getString(R.string.player), player.getNumber(), player.getPoints()));
                s.setSpan(new ForegroundColorSpan(player.getColor()), 0, s.length(), 0);
                if (player == players.get(turnPlayer)) {
                    s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
                }
                overview = new SpannableString(TextUtils.concat(overview, s));
                playerOverview.setText(overview, TextView.BufferType.SPANNABLE);
            }
        }
        else {
            playerOverview.setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.turns_used_header)).setText(R.string.turns_used);
            ((TextView) findViewById(R.id.turns_used_value)).setText(String.valueOf(totalNumberOfTurns));
        }
    }

    protected class LetComputerDoTurn extends AsyncTask<String, Void, Integer> {
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

    protected class EndTurnTask extends AsyncTask<Integer, Void, Object> {
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
                //soundPool.play(soundSuccess, 1, 1, 0, 0, 1);
                cards.get(pos1).setVisibility(View.INVISIBLE);
                cards.get(pos2).setVisibility(View.INVISIBLE);
                nextTurn();
            }
            else {
                //soundPool.play(soundFail, 1, 1, 0, 0, 1);
                boardStatus[pos1] = COVERED;
                boardStatus[pos2] = COVERED;
                cards.get(pos1).setImageResource(CardSets.getCardBack(cardBack));
                cards.get(pos2).setImageResource(CardSets.getCardBack(cardBack));
                nextPlayer();
            }
        }
    }


    protected void endGame() {
        getSharedPreferences("game_states", MODE_PRIVATE).edit().clear().apply();
        makeEndGameAlert().show();
        if (players.size() == 1) {
            Statistics.addToStats(m, n, totalNumberOfTurns);
        }
        else {
            Statistics.addFinishedGame();
        }
        Statistics.saveStatistics(GameActivity.this);
    }

    protected AlertDialog makeEndGameAlert() {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.game_summary_dialog, null);
        Collections.sort(players, Collections.<Player>reverseOrder());
        TextView[] rankViews = {view.findViewById(R.id.rank_first_view), view.findViewById(R.id.rank_second_view), view.findViewById(R.id.rank_third_view),
        view.findViewById(R.id.rank_forth_view), view.findViewById(R.id.rank_fifth_view), view.findViewById(R.id.rank_sixth_view)};
        View[] ranks = {view.findViewById(R.id.rank_first), view.findViewById(R.id.rank_second), view.findViewById(R.id.rank_third),
                view.findViewById(R.id.rank_forth), view.findViewById(R.id.rank_fifth), view.findViewById(R.id.rank_sixth)};
        for (int i = 0; i < 6; i++) {
            if (i < players.size()) {
                rankViews[i].setText(String.format(Locale.ENGLISH, "%s %d: %d",  getString(R.string.player), players.get(i).getNumber(), players.get(i).getPoints()));
            }
            else {
                ranks[i].setVisibility(View.GONE);
                rankViews[i].setVisibility(View.GONE);
            }
        }
        if (players.size() == 1) {
            ranks[0].setVisibility(View.INVISIBLE);
            rankViews[0].setText(String.format(Locale.ENGLISH, "%s: %d", getString(R.string.turns_used) , totalNumberOfTurns));
        }
        mBuilder.setTitle("Game Summary")
                .setView(view)
                .setPositiveButton("Return Home", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(mBuilder.getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("See Statistics", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(mBuilder.getContext(), StatisticsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
        return mBuilder.create();
    }


    protected int row(int position) {
        return position/m;
    }
    protected int col(int position) {
        return position % m;
    }

}
