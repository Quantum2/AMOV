package com.imaginarymakings.chess.Logic;

/**
 * Created by rafaelfrancisco on 29/12/17.
 */

public class ChessMoves {
    public static void movePiece(int piece, int postitionTo, Piece[] pieces){
        Piece temp = pieces[piece];

        switch (temp){
            case PAWN_WHITE:
                break;
            case PAWN_BLACK:
                break;
            case ROOK_WHITE:
                break;
            case ROOK_BLACK:
                break;
            case QUEEN_WHITE:
                break;
            case QUEEN_BLACK:
                break;
            case KING_WHITE:
                break;
            case KING_BLACK:
                break;
            case KNIGHT_WHITE:
                break;
            case KNIGHT_BLACK:
                break;
            case BISHOP_WHITE:
                break;
            case BISHOP_BLACK:
                break;
            case EMPTY:
                break;
        }

        pieces[piece] = Piece.EMPTY;
        pieces[postitionTo] = temp;
    }
}