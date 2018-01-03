package com.imaginarymakings.chess.Logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.imaginarymakings.chess.R;
import com.imaginarymakings.chess.Utils.Utils;

/**
 * Created by rafaelfrancisco on 27/12/17.
 */

public class SpaceAdapter extends BaseAdapter {

    private Context context;

    private int currentId;
    public Piece[] pieces = new Piece[64];

    public SpaceAdapter(Context c) {
        context = c;
        currentId = 0;

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
            pieces[i] = Piece.PAWN_WHITE;
        }

        for (int i = 48; i < 56; i++){
            pieces[i] = Piece.PAWN_BLACK;
        }

        pieces[0] = Piece.ROOK_WHITE;
        pieces[1] = Piece.KNIGHT_WHITE;
        pieces[2] = Piece.BISHOP_WHITE;
        pieces[3] = Piece.QUEEN_WHITE;
        pieces[4] = Piece.KING_WHITE;
        pieces[5] = Piece.BISHOP_WHITE;
        pieces[6] = Piece.KNIGHT_WHITE;
        pieces[7] = Piece.ROOK_WHITE;

        pieces[56] = Piece.ROOK_BLACK;
        pieces[57] = Piece.KNIGHT_BLACK;
        pieces[58] = Piece.BISHOP_BLACK;
        pieces[59] = Piece.QUEEN_BLACK;
        pieces[60] = Piece.KING_BLACK;
        pieces[61] = Piece.BISHOP_BLACK;
        pieces[62] = Piece.KNIGHT_BLACK;
        pieces[63] = Piece.ROOK_BLACK;
    }

    private void getPieceDone(ImageView view, int position) {
        Bitmap bitmap = Utils.drawableToBitmap(view.getDrawable());
        Bitmap newBitmap;

        switch (pieces[position]){
            case PAWN_WHITE:
                newBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chess_pdt60);
                bitmap = Utils.overlay(bitmap, newBitmap);

                view.setImageBitmap(bitmap);
                break;
            case PAWN_BLACK:
                newBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chess_plt60);
                bitmap = Utils.overlay(bitmap, newBitmap);

                view.setImageBitmap(bitmap);
                break;
            case ROOK_WHITE:
                newBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chess_rdt60);
                bitmap = Utils.overlay(bitmap, newBitmap);

                view.setImageBitmap(bitmap);
                break;
            case ROOK_BLACK:
                newBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chess_rlt60);
                bitmap = Utils.overlay(bitmap, newBitmap);

                view.setImageBitmap(bitmap);
                break;
            case QUEEN_WHITE:
                newBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chess_qdt60);
                bitmap = Utils.overlay(bitmap, newBitmap);

                view.setImageBitmap(bitmap);
                break;
            case QUEEN_BLACK:
                newBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chess_qlt60);
                bitmap = Utils.overlay(bitmap, newBitmap);

                view.setImageBitmap(bitmap);
                break;
            case KING_WHITE:
                newBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chess_kdt60);
                bitmap = Utils.overlay(bitmap, newBitmap);

                view.setImageBitmap(bitmap);
                break;
            case KING_BLACK:
                newBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chess_klt60);
                bitmap = Utils.overlay(bitmap, newBitmap);

                view.setImageBitmap(bitmap);
                break;
            case KNIGHT_WHITE:
                newBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chess_ndt60);
                bitmap = Utils.overlay(bitmap, newBitmap);

                view.setImageBitmap(bitmap);
                break;
            case KNIGHT_BLACK:
                newBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chess_nlt60);
                bitmap = Utils.overlay(bitmap, newBitmap);

                view.setImageBitmap(bitmap);
                break;
            case BISHOP_WHITE:
                newBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chess_bdt60);
                bitmap = Utils.overlay(bitmap, newBitmap);

                view.setImageBitmap(bitmap);
                break;
            case BISHOP_BLACK:
                newBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chess_blt60);
                bitmap = Utils.overlay(bitmap, newBitmap);

                view.setImageBitmap(bitmap);
                break;
            case EMPTY:
                break;
        }
    }
}