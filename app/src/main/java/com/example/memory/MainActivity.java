package com.example.memory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends BaseFullscreenActivity {

    private static final String TAG = "MainActivity";
    private String previous_state;
    private Button buttonContinueGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonContinueGame = findViewById(R.id.button_continue_game);
        SharedPreferences preferences = getSharedPreferences("game_states", MODE_PRIVATE);
        previous_state = preferences.getString("state", null);
        if (previous_state == null) {
            buttonContinueGame.setEnabled(false);
            buttonContinueGame.setAlpha(0.6f);
        }
    }


    public void newGameClicked(View view) {
        Log.i(TAG, "newGameClicked: newGameClicked");
        Intent intent = new Intent(view.getContext(), ConfigureGameActivity.class);
        startActivity(intent);
    }

    public void continueGameClicked(View view) {
        Log.i(TAG, "continueGameClicked: ");
        SharedPreferences preferences = getSharedPreferences("game_states", MODE_PRIVATE);
        previous_state = preferences.getString("state", null);
        Intent intent = new Intent(view.getContext(), GameActivity.class);
        intent.putExtra("previous_state", previous_state);
        startActivity(intent);
    }

    public void statisticsClicked(View view) {
        Toast.makeText(this,"Statistics clicked", Toast.LENGTH_SHORT).show();
    }

    public void helpClicked(android.view.View view) {
        Toast.makeText(this,"Help clicked", Toast.LENGTH_SHORT).show();
    }

    public void settingsClicked(android.view.View view) {
        Toast.makeText(this,"Settings clicked", Toast.LENGTH_SHORT).show();
    }

    public void creditsClicked(android.view.View view) {
        Toast.makeText(this,"Credits clicked", Toast.LENGTH_SHORT).show();
    }

    public void exitGameClicked(android.view.View view) {
        Log.i(TAG, "exitGameClicked: exitGameClicked");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
