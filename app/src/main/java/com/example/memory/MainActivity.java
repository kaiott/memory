package com.example.memory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class MainActivity extends BaseFullscreenActivity {

    private static final String TAG = "MainActivity";
    private String previous_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonContinueGame = findViewById(R.id.button_continue_game);
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

        GsonBuilder gsonBilder = new GsonBuilder().setPrettyPrinting();
        gsonBilder.registerTypeAdapter(Player.class, new PlayerAdapter());
        Gson gson = gsonBilder.create();

        Type type = new TypeToken<GameState>() {}.getType();
        GameState state = gson.fromJson(previous_state, type);
        assert state != null;
        Intent intent = new Intent(view.getContext(), GameActivity.class);
        intent.putExtra("state", state);
        startActivity(intent);
    }

    public void statisticsClicked(View view) {
        Log.i(TAG, "statisticsClicked: ");
        Intent intent = new Intent(view.getContext(), StatisticsActivity.class);
        startActivity(intent);
    }

    public void settingsClicked(android.view.View view) {
        Log.i(TAG, "settingsClicked: ");
        Intent intent = new Intent(view.getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void exitGameClicked(android.view.View view) {
        Log.i(TAG, "exitGameClicked: exitGameClicked");

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
