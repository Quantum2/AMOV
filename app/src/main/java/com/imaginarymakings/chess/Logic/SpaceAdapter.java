package com.imaginarymakings.chess.Logic;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.imaginarymakings.chess.R;

import java.util.ArrayList;

/**
 * Created by rafaelfrancisco on 27/12/17.
 */

public class SpaceAdapter extends BaseAdapter {

    private Context context;

    private int currentId;
    public Piece[] pieces = new Piece[64];

    public Player whoAmI = Player.WHITE;
    public Player currentPlayer;
    public int currentTurn;

    public ArrayList<Piece> eatenWHITE;
    public ArrayList<Piece> eatenBLACK;

    public String lastMessage;

    public SpaceAdapter(Context c) {
        context = c;
        currentId = 0;
        currentTurn = 0;

        eatenWHITE = new ArrayList<>();
        eatenBLACK = new ArrayList<>();

        setupBoard();
    }

    @Override
    public int getCount() {
        return pieces.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return currentId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        currentId = i;
        int currentLine = i / 8;

        if (view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.square_view, null);
        }

        ImageView iv = view.findViewById(R.id.pieceView);

        if ( (currentLine & 1) == 0 ) {
            if ( (currentId & 1) == 0 ) {
                iv.setImageResource(R.drawable.light);
            } else {
                iv.setImageResource(R.drawable.dark);
            }
        } else {
            if ( (currentId & 1) == 0 ) {
                iv.setImageResource(R.drawable.dark);
            } else {
                iv.setImageResource(R.drawable.light);
            }
        }

        getPieceDone(iv, currentId);
        return view;
    }

    private void setupBoard(){
        for (int i = 0; i < 64; i++){
            pieces[i] = Piece.EMPTY;
        }

        for (int i = 8; i < 16; i++){
            pieces[i] = Piece.PAWN_BLACK;
        }

        for (int i = 48; i < 56; i++){
            pieces[i] = Piece.PAWN_WHITE;
        }

        pieces[0] = Piece.ROOK_BLACK;
        pieces[1] = Piece.KNIGHT_BLACK;
        pieces[2] = Piece.BISHOP_BLACK;
        pieces[3] = Piece.QUEEN_BLACK;
        pieces[4] = Piece.KING_BLACK;
        pieces[5] = Piece.BISHOP_BLACK;
        pieces[6] = Piece.KNIGHT_BLACK;
        pieces[7] = Piece.ROOK_BLACK;

        pieces[56] = Piece.ROOK_WHITE;
        pieces[57] = Piece.KNIGHT_WHITE;
        pieces[58] = Piece.BISHOP_WHITE;
        pieces[59] = Piece.QUEEN_WHITE;
        pieces[60] = Piece.KING_WHITE;
        pieces[61] = Piece.BISHOP_WHITE;
        pieces[62] = Piece.KNIGHT_WHITE;
        pieces[63] = Piece.ROOK_WHITE;

        currentPlayer = Player.WHITE;
    }

    private void getPieceDone(ImageView view, int position) {
        Drawable[] layers;
        LayerDrawable ld;

        switch (pieces[position]){
            case PAWN_WHITE:
                layers = new Drawable[]{
                        view.getDrawable(),
                        context.getResources().getDrawable(R.drawable.chess_plt60)
                };

                ld = new LayerDrawable(layers);
                view.setImageDrawable(ld);
                break;
            case PAWN_BLACK:
                layers = new Drawable[]{
                        view.getDrawable(),
                        context.getResources().getDrawable(R.drawable.chess_pdt60)
                };

                ld = new LayerDrawable(layers);
                view.setImageDrawable(ld);
                break;
            case ROOK_WHITE:
                layers = new Drawable[]{
                        view.getDrawable(),
                        context.getResources().getDrawable(R.drawable.chess_rlt60)
                };

                ld = new LayerDrawable(layers);
                view.setImageDrawable(ld);
                break;
            case ROOK_BLACK:
                layers = new Drawable[]{
                        view.getDrawable(),
                        context.getResources().getDrawable(R.drawable.chess_rdt60)
                };

                ld = new LayerDrawable(layers);
                view.setImageDrawable(ld);
                break;
            case QUEEN_WHITE:
                layers = new Drawable[]{
                        view.getDrawable(),
                        context.getResources().getDrawable(R.drawable.chess_qlt60)
                };

                ld = new LayerDrawable(layers);
                view.setImageDrawable(ld);
                break;
            case QUEEN_BLACK:
                layers = new Drawable[]{
                        view.getDrawable(),
                        context.getResources().getDrawable(R.drawable.chess_qdt60)
                };

                ld = new LayerDrawable(layers);
                view.setImageDrawable(ld);
                break;
            case KING_WHITE:
                layers = new Drawable[]{
                        view.getDrawable(),
                        context.getResources().getDrawable(R.drawable.chess_klt60)
                };

                ld = new LayerDrawable(layers);
                view.setImageDrawable(ld);
                break;
            case KING_BLACK:
                layers = new Drawable[]{
                        view.getDrawable(),
                        context.getResources().getDrawable(R.drawable.chess_kdt60)
                };

                ld = new LayerDrawable(layers);
                view.setImageDrawable(ld);
                break;
            case KNIGHT_WHITE:
                layers = new Drawable[]{
                        view.getDrawable(),
                        context.getResources().getDrawable(R.drawable.chess_nlt60)
                };

                ld = new LayerDrawable(layers);
                view.setImageDrawable(ld);
                break;
            case KNIGHT_BLACK:
                layers = new Drawable[]{
                        view.getDrawable(),
                        context.getResources().getDrawable(R.drawable.chess_ndt60)
                };

                ld = new LayerDrawable(layers);
                view.setImageDrawable(ld);
                break;
            case BISHOP_WHITE:
                layers = new Drawable[]{
                        view.getDrawable(),
                        context.getResources().getDrawable(R.drawable.chess_blt60)
                };

                ld = new LayerDrawable(layers);
                view.setImageDrawable(ld);
                break;
            case BISHOP_BLACK:
                layers = new Drawable[]{
                        view.getDrawable(),
                        context.getResources().getDrawable(R.drawable.chess_bdt60)
                };

                ld = new LayerDrawable(layers);
                view.setImageDrawable(ld);
                break;
            case EMPTY:
                break;
        }
    }

    public GameInfo getGameInfo(){
        GameInfo gm = new GameInfo();

        gm.currentPlayer = currentPlayer;
        gm.turn = currentTurn;
        gm.piecesMoved = pieces;
        gm.message = lastMessage;

        return gm;
    }

    public void setGameInfo(GameInfo gm){
        if (gm.turn > currentTurn){
            currentPlayer = gm.currentPlayer;
            pieces = gm.piecesMoved;

            currentTurn = gm.turn;

            Intent intent = new Intent("refresh");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }
}