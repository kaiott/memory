package com.example.memory;

import java.io.Serializable;

public abstract class Player implements Serializable, Comparable<Player>{
    public static final int TYPE_HUMAN = 0, TYPE_COMP_BEGINNER = 1, TYPE_COMP_MEDIUM = 2, TYPE_COMP_GOD = 3;
    private int color;
    private int type;
    private int points;
    private int number;
    public Player() {
        this.color = -1;
        this.type = TYPE_HUMAN;
        this.points = 0;
    }
    public Player(int type) {
        this();
        this.type = type;
    }
    public Player(int color, int type) {
        this();
        this.color = color;
        this.type = type;
    }

    @Override
    public int compareTo(Player player) {
        return Integer.compare(this.points, player.points);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoint() {
        this.points++;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public abstract int makeMove(final int [] status, final int [][] buckets, final int visible_id);

    protected int makeRandomMove(final int[] status) {
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
