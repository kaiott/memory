package com.example.memory;

public class AI_PLayer_God extends Player {

    public AI_PLayer_God() {
        super(TYPE_COMP_GOD);
    }

    @Override
    public int makeMove(final int[] status) {
        int [] eligible = new int[status.length];
        int eligibleCount = 0;
        for (int pos = 0; pos < status.length; pos ++) {
            if (status[pos] == GameActivity.COVERED || status[pos] == GameActivity.TURNED) {
                eligible[eligibleCount] = pos;
                eligibleCount++;
            }
        }
        return eligible[(int) (Math.random()*eligibleCount)];
    }
}
