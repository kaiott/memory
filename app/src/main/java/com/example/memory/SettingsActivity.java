package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class SettingsActivity extends BaseFullscreenActivity {

    ImageView musicView, soundView, cardBackView, cardSetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        defineViews();
        updateUI();
        setOnClickListeners();
    }

    protected void defineViews() {
        musicView = findViewById(R.id.music_view);
        soundView = findViewById(R.id.sound_view);
    }

    protected void updateUI() {
        musicView.setImageResource(playMusic ? R.drawable.ic_volume_up_black_40dp : R.drawable.ic_volume_off_black_40dp);
        soundView.setImageResource(playMusic ? R.drawable.ic_volume_up_black_40dp : R.drawable.ic_volume_off_black_40dp);
    }

    protected void setOnClickListeners() {
        musicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!playMusic) {
                    //doBindingShitForMusicAndWhatNotImTiredOfThis();
                    Log.i("SettingsActivity", "onClick: now it should start playing music again");
                    doBindService();
                    Intent music = new Intent();
                    music.setClass(SettingsActivity.this, MusicService.class);
                    startService(music);
                    playMusic = true;
                    if (mServ != null) {
                        mServ.resumeMusic();
                    }
                } else {
                    playMusic = false;
                    if (mServ != null) {
                        mServ.pauseMusic();
                    }
                }
                getSharedPreferences("settings",MODE_PRIVATE).edit().putBoolean("playMusic", playMusic).apply();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
}
