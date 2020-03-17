package com.example.memory;

import android.util.Log;

public class AI_PLayer_Beginner extends Player {
    final static String TAG ="AI_PLAYER_BEGINNER";
    public AI_PLayer_Beginner() {
        super(TYPE_COMP_BEGINNER);
    }

    @Override
    public int makeMove(final int[] status, final int [][] buckets, final int visible_id) {
        return super.makeRandomMove(status);
    }
}
