package com.example.memory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

public abstract class BaseFullscreenActivity extends AppCompatActivity {

    HomeWatcher mHomeWatcher;
    
    private static final String TAG = "BaseFullscreenActivity";

    // Default settings
    final static boolean playMusicDefault = true, playSoundDefault = true, showAnimationDefault = false, isChildFriendlyVersionDefault = false, isDarkThemeDefault = true;
    static boolean playMusic, playSound, showAnimation, isChildFriendlyVersion, isDarkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Intent intent = new Intent(this, BackgroundSoundService.class);
        //startService(intent);

        getAndSetPreferences();


        if (playMusic) {
            doBindingShitForMusicAndWhatNotImTiredOfThis();
        }


        //Start HomeWatcher
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }

            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();
    }

    protected void doBindingShitForMusicAndWhatNotImTiredOfThis() {
        Log.i(TAG, "doBindingShitForMusicAndWhatNotImTiredOfThis: binding....");
        //BIND Music Service
        doBindService();

        Intent music = new Intent();
        music.setClass(this, MusicService.class);

        startService(music);
    }

    protected void getAndSetPreferences() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        playMusic = preferences.getBoolean("playMusic", playMusicDefault);
        playSound = preferences.getBoolean("playSound", playSoundDefault);
        showAnimation= preferences.getBoolean("showAnimation", showAnimationDefault);
        isChildFriendlyVersion = preferences.getBoolean("isChildFriendlyVersion", isChildFriendlyVersionDefault);
        isDarkTheme = preferences.getBoolean("isDarkTheme", isDarkTheme);
    }

    //Bind/Unbind music service
    protected boolean mIsBound = false;
    protected MusicService mServ;
    protected ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null && playMusic) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Detect idle screen
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm. isInteractive();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //UNBIND music service
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);

    }
}
