package com.example.memory;

public class PlayerFactory {
    public static Player getInstance(int type) {
        switch (type) {
            case Player.TYPE_HUMAN: return new HumanPlayer();
            case Player.TYPE_COMP_BEGINNER: return new AI_PLayer_Beginner();
            case Player.TYPE_COMP_GOD: return new AI_PLayer_God();
            default: return null;
        }
    }
}
