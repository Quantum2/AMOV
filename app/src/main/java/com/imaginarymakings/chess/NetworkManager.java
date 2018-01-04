package com.imaginarymakings.chess;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.imaginarymakings.chess.Logic.Piece;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by rafaelfrancisco on 02/01/18.
 */

public class NetworkManager {
    private static final int SERVERPORT = 6000;
    private boolean end = false;

    private Context c;

    private Piece pieces[];

    public Piece[] getPieces() {
        return pieces;
    }

    public void setPieces(Piece[] pieces) {
        this.pieces = pieces;
    }

    NetworkManager(Context c) {
        this.c = c;

    }

    public String getCurrentIP(){
        WifiManager wifiMan = (WifiManager) c.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        return String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
    }

    public void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket ss = new ServerSocket(SERVERPORT);

                    while (!end){
                        //Server is waiting for client here, if needed
                        Socket s = ss.accept();

                        ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

                        os.writeObject(pieces);

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    ss.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startClient(final String ip) {
        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //Replace below IP with the IP of that device in which server socket open.
                    //If you change port then change the port number in the server side code also.
                    Socket s = new Socket(ip, SERVERPORT);

                    ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

                    os.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
