package com.imaginarymakings.chess.Logic;

import java.util.ArrayList;

/**
 * Created by rafaelfrancisco on 10/01/18.
 */

public class PastGame {
    private ArrayList<Movement> movements;
    public boolean won;

    public PastGame() {
        movements = new ArrayList<>();
    }

    public void addMovement(int from, int to){
        Movement movement = new Movement(from, to);
        movements.add(movement);
    }

    private class Movement{
        int from, to;

        Movement(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }
}
