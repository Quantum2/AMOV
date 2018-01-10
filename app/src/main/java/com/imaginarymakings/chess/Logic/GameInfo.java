package com.imaginarymakings.chess.Logic;

import java.io.Serializable;

/**
 * Created by rafaelfrancisco on 08/01/18.
 */

public class GameInfo implements Serializable {
    public Profile profile;
    public Piece[] piecesMoved;

    public Player currentPlayer;
    public int turn = 0;

    public String message;
}
