package com.example.memory;

public class PlayerFactory {
    public static Player getInstance(int type) {
        switch (type) {
            case Player.TYPE_HUMAN: return new HumanPlayer();
            case Player.TYPE_COMP_BEGINNER: return null;
            case Player.TYPE_COMP_GOD: return null;
            default: return null;
        }
    }
}
