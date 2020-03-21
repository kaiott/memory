package com.example.memory;

public class CardSets {
    final static int numberChildFriendlySets = 2;
    static int[] set_0 = {R.drawable.cat0, R.drawable.cat1, R.drawable.cat2, R.drawable.cat3, R.drawable.cat4, R.drawable.cat5, R.drawable.cat6, R.drawable.cat7, R.drawable.cat8};
    static int[] set_1 = {R.drawable.bluttest2713182, R.drawable.handwaesche2713253, R.drawable.henne2713260, R.drawable.krankenhaus2713224, R.drawable.lunge2713243, R.drawable.menschen2713219, R.drawable.moskito2713259, R.drawable.mund2713244, R.drawable.ratte2713179, R.drawable.schlaeger2713262, R.drawable.schwein2713261, R.drawable.sex2713246, R.drawable.spritze2713235, R.drawable.tod2713258, R.drawable.virus2713175, R.drawable.virus2713189, R.drawable.virus2713196, R.drawable.virus2713245,};
    static int[] set_2 = {R.drawable.air_rider, R.drawable.amazon, R.drawable.aries, R.drawable.backwards_cowgirl, R.drawable.ball, R.drawable.bench, R.drawable.bent_cowgirl, R.drawable.bridge, R.drawable.cowgirl, R.drawable.crouching_tiger, R.drawable.downstroke, R.drawable.pokemon, R.drawable.precipice, R.drawable.princess, R.drawable.reverse_cowgirl, R.drawable.shuttle, R.drawable.sledge, R.drawable.soft_landing,};

    static int[] back = {R.drawable.card_back1, R.drawable.card_back2, R.drawable.card_back3};

    static int[][] sets = {set_0, set_1, set_2};

    public static int[] getSet(int setNumber) {
        return sets[setNumber];
    }

    public static int getSetSize(int setNumber) {
        return sets[setNumber].length;
    }

    public static int getNumberChildFriendlySets() {
        return numberChildFriendlySets;
    }

    public static int getNumberOfSets() {
        return sets.length;
    }

    public static int getCardBack(int backNumber) {
        return back[backNumber];
    }

    public static int getNumberOfBacks() {
        return back.length;
    }
}
