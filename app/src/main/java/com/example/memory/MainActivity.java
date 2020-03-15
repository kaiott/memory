package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends BaseFullscreenActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }





    public void newGameClicked(android.view.View view) {
        Log.i(TAG, "newGameClicked: newGameClicked");
        Intent intent = new Intent(view.getContext(), ConfigureGameActivity.class);
        startActivity(intent);
    }

    public void continueGameClicked(android.view.View view) {
        Toast.makeText(this,"Continue Game clicked", Toast.LENGTH_SHORT).show();
    }

    public void statisticsClicked(android.view.View view) {
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
