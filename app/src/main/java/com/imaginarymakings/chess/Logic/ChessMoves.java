package com.imaginarymakings.chess.Logic;

import static com.imaginarymakings.chess.Utils.Utils.isIntInArray;

/**
 * Created by rafaelfrancisco on 29/12/17.
 */

public class ChessMoves {

    public static boolean movePiece(int piece, int postitionTo, SpaceAdapter adapter, boolean isAI){
        if (adapter.whoAmI == adapter.currentPlayer || isAI){
            Piece temp = adapter.pieces[piece];

            if (isColor(adapter.currentPlayer, temp)){
                int currentLine = getWhichLine(piece);

                switch (temp){
                    case PAWN_WHITE:
                        if (getWhichLine(postitionTo) != (currentLine - 1))
                            return false;
                        break;
                    case PAWN_BLACK:
                        if (getWhichLine(postitionTo) != (currentLine + 1))
                            return false;
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

                adapter.pieces[piece] = Piece.EMPTY;
                adapter.pieces[postitionTo] = temp;

                if (adapter.currentPlayer == Player.WHITE)
                    adapter.currentPlayer = Player.BLACK;
                else
                    adapter.currentPlayer = Player.WHITE;

                adapter.currentTurn++;

                return true;
            }
        }

        return false;
    }

    static boolean isColor(Player player, Piece piece){
        if (Player.WHITE == player){
            switch (piece){
                case PAWN_WHITE:
                    return true;
                case ROOK_WHITE:
                    return true;
                case QUEEN_WHITE:
                    return true;
                case KING_WHITE:
                    return true;
                case KNIGHT_WHITE:
                    return true;
                case BISHOP_WHITE:
                    return true;
            }

            return false;
        } else {
            switch (piece) {
                case PAWN_BLACK:
                    return true;
                case ROOK_BLACK:
                    return true;
                case QUEEN_BLACK:
                    return true;
                case KING_BLACK:
                    return true;
                case KNIGHT_BLACK:
                    return true;
                case BISHOP_BLACK:
                    return true;
            }

            return false;
        }
    }

    static int[] getLineOfEight(int line){
        int[] temp = new int[8];
        int startingNumber = line * 8;

        for (int i = 0; i < temp.length; i++){
            temp[i] = startingNumber;
            startingNumber++;
        }

        return temp;
    }

    static int[] getColumnOfEight(int column){
        int[] temp = new int[8];

        for (int i = 0; i < temp.length; i++){
            temp[i] = column;
            column = column + 8;
        }

        return temp;
    }

    static int[] getDiagonalOfEightRight(int line){
        int[] temp = new int[8];
        int startingNumber = line * 8;

        for (int i = 0; i < temp.length; i++){
            temp[i] = startingNumber;
            startingNumber++;
        }

        return temp;
    }

    static int[] getDiagonalOfEightLeft(int line){
        int[] temp = new int[8];
        int startingNumber = line * 8;

        for (int i = 0; i < temp.length; i++){
            temp[i] = startingNumber;
            startingNumber++;
        }

        return temp;
    }

    static int getWhichLine(int pos){
        int counter = 0;

        for (int i = 0; i < 8; i++){
            if (isIntInArray(getLineOfEight(i), pos)){
                return counter;
            }

            counter++;
        }

        return counter;
    }

    static int getWhichColumn(int pos){
        int counter = 0;

        for (int i = 0; i < 8; i++){
            if (isIntInArray(getColumnOfEight(i), pos)){
                return counter;
            }

            counter++;
        }

        return counter;
    }
}