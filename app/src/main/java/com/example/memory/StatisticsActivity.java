package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        TextView[][] singlePlayerViews =   {{findViewById(R.id.sp_element11), findViewById(R.id.sp_element12)},
                                            {findViewById(R.id.sp_element21), findViewById(R.id.sp_element22)},
                                            {findViewById(R.id.sp_element31), findViewById(R.id.sp_element32)},
                                            {findViewById(R.id.sp_element41), findViewById(R.id.sp_element42)},
                                            {findViewById(R.id.sp_element51), findViewById(R.id.sp_element52)},
                                            {findViewById(R.id.sp_element61), findViewById(R.id.sp_element62)},};

        for (int i = 0; i < singlePlayerViews.length; i++) {
            if (Statistics.getSPNumberGames()[i] != 0) {
                singlePlayerViews[i][0].setText(String.format(Locale.ENGLISH,"Least number of turns: %d",Statistics.getSPLeastNumberTries()[i]));
                singlePlayerViews[i][1].setText(String.format(Locale.ENGLISH,"Average number of turns: %.2f",Statistics.getSPTotalNumberTries()[i] * 1.0f / Statistics.getSPNumberGames()[i]));
            }
            else {
                singlePlayerViews[i][0].setText(String.format(Locale.ENGLISH,"Least number of turns: -"));
                singlePlayerViews[i][1].setText(String.format(Locale.ENGLISH,"Average number of turns: -"));
            }
        }

        ((TextView) findViewById(R.id.mp_element1)).setText(String.format(Locale.ENGLISH, "Number of games started: %d", Statistics.getMPGamesStarted()));
        ((TextView) findViewById(R.id.mp_element2)).setText(String.format(Locale.ENGLISH, "Number of games finished: %d", Statistics.getMPGamesFinished()));
    }
}
