package com.example.memory;

public class AI_PLayer_Medium extends Player {

    public AI_PLayer_Medium() {
        super(TYPE_COMP_MEDIUM);
    }

    @Override
    public int makeMove(final int[] status, final int [][] buckets, final int visible_id) {
        // if no card is visible
        if (visible_id == -1) {
            // we check for all card_ids if in the corresponding bucket there are two or more positions known
            for (int [] bucket : buckets) {
                if (bucket[bucket.length-1] >= 2) {
                    // if we have such a bucket we get the first element and return it
                    for (int i = 0; i < bucket.length-1; i++) {
                        if (bucket[i] != -1) {
                            return bucket[i];
                        }
                    }
                }
            }
        }
        // meaning there is a card visible
        else {
            int [] bucket = buckets[visible_id];
            //we check if the corresponding bucket has one other element
            if (bucket[bucket.length-1] >= 1) {
                // if we have such a bucket we get the first element that isn't visible and return it
                for (int i = 0; i < bucket.length-1; i++) {
                    if (bucket[i] != -1 && status[bucket[i]] != GameActivity.VISIBLE) {
                        return bucket[i];
                    }
                }
            }
        }
        // if no such bucket is found we make a random move
        return super.makeRandomMove(status);
    }
}
