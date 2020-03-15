package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class GameActivity extends BaseFullscreenActivity {

    ArrayList<ImageView> cards;
    Guideline [] verticalGuides = new Guideline[5];
    private final static String TAG = "GameActivity";
    public final static int COVERED = 0, TURNED = 1;
    int n = 6;
    int m = 6;
    int numTries;
    int oldPosition;
    ArrayList<Integer> objectA;
    int [] catIDs = {R.drawable.cat0,R.drawable.cat1,R.drawable.cat2,R.drawable.cat3,R.drawable.cat4,R.drawable.cat5,R.drawable.cat6,R.drawable.cat7,R.drawable.cat8};
    int [] status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initializeMembers();

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
            new EndTurnTask().execute(oldPosition, position);

            if (objectA.get(oldPosition).equals(objectA.get(position))) {
                addPoints();
            }
        }
    }

    private class EndTurnTask extends AsyncTask<Integer, Void, Object> {
        int pos1, pos2;
        protected Object doInBackground(Integer... args) {
            pos1 = args[0];
            pos2 = args[1];
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        }

        protected void onPostExecute(Object result) {
            if (objectA.get(pos1).equals(objectA.get(pos2))) {
                cards.get(pos1).setVisibility(View.INVISIBLE);
                cards.get(pos2).setVisibility(View.INVISIBLE);
            }
            else {
                status[pos1] = COVERED;
                status[pos2] = COVERED;
                cards.get(pos1).setImageResource(R.drawable.card_back);
                cards.get(pos2).setImageResource(R.drawable.card_back);
            }
            nextPlayer();
        }
    }

    private void addPoints() {

    }

    private void nextPlayer() {
        numTries = 0;
        oldPosition = -1;
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
        if (n==5) {
            for (int i = 0; i< verticalGuides.length; i++) {
                verticalGuides[i].setGuidelinePercent((i + 1f )/n);
            }
            for(int i = 0; i < 6; i++) {
                cards.get(5 + 6*i).setVisibility(View.INVISIBLE);
                cards.get(30 + i).setVisibility(View.INVISIBLE);
            }
        }
        else if (n==6) {
            for (int i = 0; i< verticalGuides.length; i++) {
                verticalGuides[i].setGuidelinePercent((i + 1f )/n);
            }
            for(int i = 0; i < 6; i++) {
                cards.get(5 + 6*i).setVisibility(View.VISIBLE);
                cards.get(30 + i).setVisibility(View.VISIBLE);
            }
        }
    }

    protected void initializeMembers() {
        status = new int[n*m];
        numTries = 0;
        oldPosition = -1;
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
        objectA = new ArrayList<>(36);
        for (int i = 0; i < 9; i ++) {
            objectA.add(i);
            objectA.add(i);
            objectA.add(i);
            objectA.add(i);
        }
        Collections.shuffle(objectA);
    }
}
