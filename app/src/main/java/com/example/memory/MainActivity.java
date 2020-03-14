package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }





    public void newGameClicked(android.view.View view) {
        Toast.makeText(this,"New Game clicked", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this,"Exit Game clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
