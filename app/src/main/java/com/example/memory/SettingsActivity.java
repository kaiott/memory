package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

public class SettingsActivity extends BaseFullscreenActivity {

    ImageView musicView, soundView, cardBackView, cardSetView;
    Switch animationsSwitch, darkThemeSwitch, childFriendlySwitch;

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
        cardBackView = findViewById(R.id.card_back_view);
        cardSetView = findViewById(R.id.card_set_view);

        animationsSwitch = findViewById(R.id.animations_switch);
        darkThemeSwitch = findViewById(R.id.dark_theme_switch);
        childFriendlySwitch = findViewById(R.id.child_friendly_switch);
    }

    protected void updateUI() {
        musicView.setImageResource(playMusic ? R.drawable.ic_volume_up_black_40dp : R.drawable.ic_volume_off_black_40dp);
        soundView.setImageResource(playSound ? R.drawable.ic_volume_up_black_40dp : R.drawable.ic_volume_off_black_40dp);
        cardBackView.setImageResource(R.drawable.card_back);
        cardSetView.setImageResource(cardSet == 0 ? R.drawable.cat0 : R.drawable.amazon);
        childFriendlySwitch.setChecked(isChildFriendlyVersion);
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
                updateUI();
            }
        });

        cardSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardSet++;
                cardSet %= isChildFriendlyVersion ? 1 : 2;
                getSharedPreferences("settings",MODE_PRIVATE).edit().putInt("cardSet", cardSet).apply();
                updateUI();
            }
        });

        childFriendlySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChildFriendlyVersion = b;
                getSharedPreferences("settings",MODE_PRIVATE).edit().putBoolean("isChildFriendlyVersion", isChildFriendlyVersion).apply();
                if (isChildFriendlyVersion && cardSet > 0) {
                    cardSet = 0;
                }
                updateUI();
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
