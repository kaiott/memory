package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    /*don't get me started on the rudimentary handling of this class*/

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
                singlePlayerViews[i][0].setText(String.format(Locale.ENGLISH,"%s: %d", getString(R.string.least_number_of_tries), Statistics.getSPLeastNumberTries()[i]));
                singlePlayerViews[i][1].setText(String.format(Locale.ENGLISH,"%s: %.2f", getString(R.string.average_number_of_tries),Statistics.getSPTotalNumberTries()[i] * 1.0f / Statistics.getSPNumberGames()[i]));
            }
            else {
                singlePlayerViews[i][0].setText(String.format(Locale.ENGLISH,"%s: -", getString(R.string.least_number_of_tries)));
                singlePlayerViews[i][1].setText(String.format(Locale.ENGLISH,"%s: -", getString(R.string.average_number_of_tries)));
            }
        }

        ((TextView) findViewById(R.id.mp_element1)).setText(String.format(Locale.ENGLISH, "%s: %d", getString(R.string.number_of_games_started), Statistics.getMPGamesStarted()));
        ((TextView) findViewById(R.id.mp_element2)).setText(String.format(Locale.ENGLISH, "%s: %d", getString(R.string.number_of_games_finished), Statistics.getMPGamesFinished()));
    }
}
