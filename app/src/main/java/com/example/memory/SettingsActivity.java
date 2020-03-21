package com.example.memory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends BaseFullscreenActivity {

    ImageView musicView, soundView, cardBackView, cardSetView;
    Switch darkThemeSwitch, childFriendlySwitch;
    Spinner languageSpinner;
    SeekBar durationFadeSeekBar, durationComputerThinkSeekBar;

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

        durationFadeSeekBar = findViewById(R.id.durationFadeSeekBar);
        durationComputerThinkSeekBar = findViewById(R.id.durationCompThinkSeekBar);
    }

    protected void updateUI() {
        musicView.setImageResource(playMusic ? R.drawable.ic_volume_up_black_40dp : R.drawable.ic_volume_off_black_40dp);
        soundView.setImageResource(playSound ? R.drawable.ic_volume_up_black_40dp : R.drawable.ic_volume_off_black_40dp);
        cardBackView.setImageResource(CardSets.getCardBack(cardBack));
        cardSetView.setImageResource(CardSets.getSet(cardSet)[0]);
        darkThemeSwitch.setChecked(isDarkTheme);
        childFriendlySwitch.setChecked(isChildFriendlyVersion);
        durationFadeSeekBar.setProgress(durationFadeOut/200-2);
        durationComputerThinkSeekBar.setProgress(durationComputerThink/200-2);
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

        cardBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardBack++;
                cardBack %= CardSets.getNumberOfBacks();
                getSharedPreferences("settings",MODE_PRIVATE).edit().putInt("cardBack", cardBack).apply();
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
                //instead of recreate
                startActivity(getIntent());
                finish();
                overridePendingTransition(0, 0);
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
                    getSharedPreferences("settings", MODE_PRIVATE).edit().putInt("language", language).apply();
                    updateUI();

                    //instead of recreate
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        durationFadeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                durationFadeOut = i*200 + 400;
                getSharedPreferences("settings", MODE_PRIVATE).edit().putInt("durationFadeOut", durationFadeOut).apply();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        durationComputerThinkSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                durationComputerThink = i*200 + 400;
                getSharedPreferences("settings", MODE_PRIVATE).edit().putInt("durationComputerThink", durationComputerThink).apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void resetToDefault(View view) {
        getSharedPreferences("settings", MODE_PRIVATE).edit().clear().commit();
        //instead of recreate
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
    }

    public void saveAndReturn(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
