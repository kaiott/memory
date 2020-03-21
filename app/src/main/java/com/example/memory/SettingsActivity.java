package com.example.memory;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Locale;

public class SettingsActivity extends BaseFullscreenActivity {

    ImageView musicView, soundView, cardBackView, cardSetView;
    Switch darkThemeSwitch, childFriendlySwitch;
    Spinner languageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        defineViews();
        updateUI();
        setOnClickListenersETC();
    }

    protected void defineViews() {
        musicView = findViewById(R.id.music_view);
        soundView = findViewById(R.id.sound_view);
        cardBackView = findViewById(R.id.card_back_view);
        cardSetView = findViewById(R.id.card_set_view);

        darkThemeSwitch = findViewById(R.id.dark_theme_switch);
        childFriendlySwitch = findViewById(R.id.child_friendly_switch);

        languageSpinner = findViewById(R.id.language_spinner);
        final String[] themes = {"English", "Español",};
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, themes);
        languageSpinner.setAdapter(mAdapter);
        languageSpinner.setSelection(language,false);
    }

    protected void updateUI() {
        musicView.setImageResource(playMusic ? R.drawable.ic_volume_up_black_40dp : R.drawable.ic_volume_off_black_40dp);
        soundView.setImageResource(playSound ? R.drawable.ic_volume_up_black_40dp : R.drawable.ic_volume_off_black_40dp);
        cardBackView.setImageResource(R.drawable.card_back);
        cardSetView.setImageResource(CardSets.getSet(cardSet)[0]);
        darkThemeSwitch.setChecked(isDarkTheme);
        childFriendlySwitch.setChecked(isChildFriendlyVersion);
    }

    protected void setOnClickListenersETC() {
        musicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playMusic) {
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

        soundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound = !playSound;
                getSharedPreferences("settings",MODE_PRIVATE).edit().putBoolean("playSound", playSound).apply();
                updateUI();
            }
        });

        cardSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardSet++;
                cardSet %= isChildFriendlyVersion ? CardSets.getNumberChildFriendlySets() : CardSets.getNumberOfSets();
                getSharedPreferences("settings",MODE_PRIVATE).edit().putInt("cardSet", cardSet).apply();
                updateUI();
            }
        });

        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isDarkTheme = b;
                getSharedPreferences("settings", MODE_PRIVATE).edit().putBoolean("isDarkTheme", isDarkTheme).apply();
                recreate();
            }
        });

        childFriendlySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChildFriendlyVersion = b;
                getSharedPreferences("settings",MODE_PRIVATE).edit().putBoolean("isChildFriendlyVersion", isChildFriendlyVersion).apply();
                if (isChildFriendlyVersion && cardSet >= CardSets.getNumberChildFriendlySets()) {
                    cardSet = 0;
                }
                updateUI();
            }
        });

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (language != position) {
                    language = position;
                    setLocale(language);
                    updateUI();
                    recreate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
