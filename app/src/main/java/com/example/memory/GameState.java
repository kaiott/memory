package com.example.memory;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {
    ArrayList<Player> players;
    ArrayList<Integer> objectA;
    int n, m, turnPlayer, cardsLeft;
    int [] status;
    int totalNumberOfTurns;

    public GameState(ArrayList<Player> players, ArrayList<Integer> objectA, int n, int m, int turnPlayer, int cardsLeft, int[] status, int totalNumberOfTries) {
        this.players = players;
        this.objectA = objectA;
        this.n = n;
        this.m = m;
        this.turnPlayer = turnPlayer;
        this.cardsLeft = cardsLeft;
        this.status = status;
        this.totalNumberOfTurns = totalNumberOfTries;
    }
}
