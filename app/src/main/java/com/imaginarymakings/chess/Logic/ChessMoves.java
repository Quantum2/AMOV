package com.imaginarymakings.chess.Logic;

import static com.imaginarymakings.chess.Utils.Utils.isIntInArray;

/**
 * Created by rafaelfrancisco on 29/12/17.
 */

public class ChessMoves {

    public static boolean movePiece(int piece, int postitionTo, SpaceAdapter adapter, boolean isAI){
        if (piece == postitionTo)
            return false;

        if (adapter.whoAmI == adapter.currentPlayer || isAI){
            Piece temp = adapter.pieces[piece];

            if (isColor(adapter.currentPlayer, temp)){
                int currentLine = getWhichLine(piece);

                switch (temp){
                    case PAWN_WHITE:
                        if (!canPawnMoveWhite(piece, postitionTo, adapter.pieces) || getWhichLine(postitionTo) != (currentLine - 1)){
                            return false;
                        }

                        break;
                    case PAWN_BLACK:
                        if (!canPawnMoveBlack(piece, postitionTo, adapter.pieces) || getWhichLine(postitionTo) != (currentLine + 1)){
                            return false;
                        }

                        break;
                    case ROOK_WHITE:
                        if (!canRookMove(piece, postitionTo, adapter.pieces)){
                            return false;
                        }
                        break;
                    case ROOK_BLACK:
                        if (!canRookMove(piece, postitionTo, adapter.pieces)){
                            return false;
                        }
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
                        if (!canKnightMove(piece, postitionTo, adapter.pieces)){
                            return false;
                        }
                        break;
                    case KNIGHT_BLACK:
                        if (!canKnightMove(piece, postitionTo, adapter.pieces)){
                            return false;
                        }
                        break;
                    case BISHOP_WHITE:
                        if (!canBishopMove(piece, postitionTo, adapter.pieces)){
                            return false;
                        }
                        break;
                    case BISHOP_BLACK:
                        if (!canBishopMove(piece, postitionTo, adapter.pieces)){
                            return false;
                        }
                        break;
                    case EMPTY:
                        return false;
                }

                if (adapter.pieces[postitionTo] != Piece.EMPTY){
                    //Eats
                    if (adapter.currentPlayer == Player.WHITE){
                        adapter.eatenWHITE.add(adapter.pieces[postitionTo]);
                    } else {
                        adapter.eatenBLACK.add(adapter.pieces[postitionTo]);
                    }
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

    private static boolean isColor(Player player, Piece piece){
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

    private static int[] getLineOfEight(int line){
        int[] temp = new int[8];
        int startingNumber = line * 8;

        for (int i = 0; i < temp.length; i++){
            temp[i] = startingNumber;
            startingNumber++;
        }

        return temp;
    }

    private static int[] getColumnOfEight(int column){
        int[] temp = new int[8];

        for (int i = 0; i < temp.length; i++){
            temp[i] = column;
            column = column + 8;
        }

        return temp;
    }

    private static int getWhichLine(int pos){
        int counter = 0;

        for (int i = 0; i < 8; i++){
            if (isIntInArray(getLineOfEight(i), pos)){
                return counter;
            }

            counter++;
        }

        return counter;
    }

    private static int getWhichColumn(int pos){
        int counter = 0;

        for (int i = 0; i < 8; i++){
            if (isIntInArray(getColumnOfEight(i), pos)){
                return counter;
            }

            counter++;
        }

        return counter;
    }

    private static boolean oppositeColor(Piece piece, Piece otherPiece) {
        return piece.name().contains("WHITE") && otherPiece.name().contains("BLACK") || piece.name().contains("BLACK") && otherPiece.name().contains("WHITE");

    }

    private static boolean canPawnMoveWhite(int pos, int posTo, Piece[] pieces){
        int difs = Math.abs(pos - posTo);

        if (pieces[pos - 7] != Piece.EMPTY && difs == 7 && oppositeColor(pieces[pos], pieces[pos - 7])){
            return true;
        }
        if (pieces[pos - 9] != Piece.EMPTY && difs == 9 && oppositeColor(pieces[pos], pieces[pos - 9])){
            return true;
        }

        return (pieces[posTo] == Piece.EMPTY && (pos - posTo) == 8);
    }

    private static boolean canPawnMoveBlack(int pos, int posTo, Piece[] pieces){
        int difs = Math.abs(pos - posTo);

        if (pieces[pos + 7] != Piece.EMPTY && difs == 7 && oppositeColor(pieces[pos], pieces[pos + 7])){
            return true;
        }
        if (pieces[pos + 9] != Piece.EMPTY && difs == 9 && oppositeColor(pieces[pos], pieces[pos + 9])){
            return true;
        }

        return (pieces[posTo] == Piece.EMPTY && (posTo - pos) == 8);
    }

    private static boolean canRookMove(int pos, int posTo, Piece[] pieces){
        int line = getWhichLine(pos);
        int column = getWhichColumn(pos);

        int lineTo = getWhichLine(posTo);
        int columnTo = getWhichColumn(posTo);

        Piece orignalPiece = pieces[pos];

        if (columnTo == column){
            if (posTo - pos > 0) {
                pos = pos + 8;

                while (pos >= 0 && pos <= 63 && pos != posTo) {
                    if (pieces[pos] == Piece.EMPTY) {
                        pos = pos + 8;
                    } else {
                        return false;
                    }
                }

                return pieces[pos] == Piece.EMPTY || oppositeColor(orignalPiece, pieces[pos]);
            } else {
                pos = pos - 8;

                while (pos >= 0 && pos <= 63 && pos != posTo){
                    if (pieces[pos] == Piece.EMPTY){
                        pos = pos - 8;
                    } else {
                        return false;
                    }
                }

                return pieces[pos] == Piece.EMPTY || oppositeColor(orignalPiece, pieces[pos]);
            }
        } else if (line == lineTo){
            if (posTo - pos > 0) {
                pos = pos + 1;

                while (pos >= 0 && pos <= 63 && pos != posTo) {
                    if (pieces[pos] == Piece.EMPTY) {
                        pos = pos + 1;
                    } else {
                        return false;
                    }
                }

                return pieces[pos] == Piece.EMPTY || oppositeColor(orignalPiece, pieces[pos]);
            } else {
                pos = pos - 1;

                while (pos >= 0 && pos <= 63 && pos != posTo){
                    if (pieces[pos] == Piece.EMPTY){
                        pos = pos - 1;
                    } else {
                        return false;
                    }
                }

                return pieces[pos] == Piece.EMPTY || oppositeColor(orignalPiece, pieces[pos]);
            }
        }

        return false;
    }

    private static boolean canBishopMove(int pos, int posTo, Piece[] pieces){
        Piece originalPiece = pieces[pos];

        if (posTo > pos){
            pos = pos + 7;

            while (pos >= 0 && pos <= 63){
                if (pieces[pos] == Piece.EMPTY && pos == posTo)
                    return true;
                else if (pos == posTo)
                    return oppositeColor(originalPiece, pieces[posTo]);

                pos = pos + 2;

                if (pieces[pos] == Piece.EMPTY && pos == posTo)
                    return true;
                else if (pos == posTo)
                    return oppositeColor(originalPiece, pieces[posTo]);

                pos = pos + 7;
            }
        } else {
            pos = pos - 7;

            while (pos >= 0 && pos <= 63){
                if (pieces[pos] == Piece.EMPTY && pos == posTo)
                    return true;
                else if (pos == posTo)
                    return oppositeColor(originalPiece, pieces[posTo]);

                pos = pos - 2;

                if (pieces[pos] == Piece.EMPTY && pos == posTo)
                    return true;
                else if (pos == posTo)
                    return oppositeColor(originalPiece, pieces[posTo]);

                pos = pos - 7;
            }
        }

        return false;
    }

    private static boolean canKnightMove(int pos, int posTo, Piece[] pieces){
        if (posTo == (pos + 6) || posTo == (pos + 15) || posTo == (pos - 6) || posTo == (pos - 15)){
            if (pieces[posTo] == Piece.EMPTY)
                return true;
            else
                return oppositeColor(pieces[pos], pieces[posTo]);
        }

        if (posTo == (pos + 17) || posTo == (pos + 10) || posTo == (pos - 17) || posTo == (pos - 10)){
            if (pieces[posTo] == Piece.EMPTY)
                return true;
            else
                return oppositeColor(pieces[pos], pieces[posTo]);
        }

        return false;
    }
}