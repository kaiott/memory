package com.example.memory;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;

public class Statistics implements Serializable {


    /* Why non-static members except a static member of the very same class, and static function that
    act on the static member statistics? Because we want all these values to be stored during serialization
    so they cannot be static, but the logic calls for all of it to be static. Ugly fix, I know...
     */

    static Statistics statistics;

    // single player stats
    int  [] numberGames;
    int [] leastNumberTries;
    int [] totalNumberTries;

    // multi player stats
    int gamesFinished;
    int gamesStarted;

    public Statistics() {
        numberGames = new int[5];
        leastNumberTries = new int[5];
        Arrays.fill(leastNumberTries, Integer.MAX_VALUE);
        totalNumberTries = new int[5];

        gamesFinished = 0;
        gamesStarted = 0;
    }

    //adding single player stats after game finished
    public static void addToStats(int m, int n, int numTries) {
        if (statistics == null) {
            statistics = new Statistics();
        }
        int gameType = convertToGameType(m,n);
        statistics.numberGames[gameType]++;
        if (statistics.leastNumberTries[gameType] > numTries) {
            statistics.leastNumberTries[gameType] = numTries;
        }
        statistics.totalNumberTries[gameType] += numTries;
    }

    public static int convertToGameType(int m, int n) {
        switch (m*n) {
            case 36: return 0;
            case 30: return 1;
            case 20: return 2;
            case 16: return 3;
            case 12: return 4;
        }
        return 0;
    }

    public static void addStartedGame() {
        if (statistics == null) {
            statistics = new Statistics();
        }
        statistics.gamesStarted++;
    }
    public static void addFinishedGame() {
        if (statistics == null) {
            statistics = new Statistics();
        }
        statistics.gamesFinished++;
    }


    public static void buildStatistics(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("game_states", Context.MODE_PRIVATE);
        String statisticsString = preferences.getString("statistics", null);
        try {
            GsonBuilder gsonBilder = new GsonBuilder().setPrettyPrinting();
            Gson gson = gsonBilder.create();
            Type type = new TypeToken<Statistics>() {}.getType();
            statistics = gson.fromJson(statisticsString, type);
        } catch (Exception some) {
            statistics = new Statistics();
        }
        if (statistics == null) {
            statistics = new Statistics();
        }
    }

    public static void saveStatistics(Context context) {
        GsonBuilder gsonBilder = new GsonBuilder().setPrettyPrinting();
        Gson gson = gsonBilder.create();

        SharedPreferences preferences = context.getSharedPreferences("game_states", Context.MODE_PRIVATE);
        preferences.edit().putString("statistics", gson.toJson(statistics)).apply();
    }

    static int getMPGamesFinished() {
        return statistics.gamesFinished;
    }

    static int getMPGamesStarted() {
        return statistics.gamesStarted;
    }

    static int[] getSPTotalNumberTries() {
        return statistics.totalNumberTries;
    }

    static int[] getSPLeastNumberTries() {
        return statistics.leastNumberTries;
    }

    static int[] getSPNumberGames() {
        return statistics.numberGames;
    }

}
