package com.example.memory;

public class HumanPlayer extends Player {
    public HumanPlayer() {
        super(TYPE_HUMAN);
    }

    @Override
    public int makeMove(int[] status, int[][] buckets, int visible_id) {
        return 0;
    }

}
