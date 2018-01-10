package com.imaginarymakings.chess.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.content.LocalBroadcastManager;

import com.imaginarymakings.chess.Activities.MainActivity;
import com.imaginarymakings.chess.Logic.GameInfo;
import com.imaginarymakings.chess.Logic.Player;
import com.imaginarymakings.chess.Logic.SpaceAdapter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by rafaelfrancisco on 02/01/18.
 */

public class NetworkManager {
    private static final int SERVER_PORT = 6000;
    private boolean end = false;

    private Context c;
    private SpaceAdapter adapter;

    public GameInfo gameInfo = null;

    public void endGame(){
        end = true;
    }

    public NetworkManager(Context c, SpaceAdapter adapter) {
        this.c = c;
        this.adapter = adapter;
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
                    ServerSocket ss = new ServerSocket(SERVER_PORT);
                    Socket s = ss.accept();

                    Intent temp = new Intent("connected");
                    LocalBroadcastManager.getInstance(c).sendBroadcast(temp);

                    ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                    ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

                    while (!end){
                        while (gameInfo == null)
                            Thread.sleep(150);
                        os.writeObject(gameInfo);
                        gameInfo = null;
                        GameInfo gm = (GameInfo) is.readObject();

                        Intent intent = new Intent("refresh");
                        intent.putExtra("gameInfo", gm);
                        LocalBroadcastManager.getInstance(c).sendBroadcast(intent);
                    }

                    s.close();
                    ss.close();
                } catch (InterruptedException | IOException | ClassNotFoundException e) {
                    e.printStackTrace();

                    Intent intent = new Intent(c, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("error", e.toString());
                    c.startActivity(intent);
                }
            }
        }).start();
    }

    public void startClient(final String ip) {
        adapter.whoAmI = Player.BLACK;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket s = new Socket(ip, SERVER_PORT);

                    Intent temp = new Intent("connected");
                    LocalBroadcastManager.getInstance(c).sendBroadcast(temp);

                    ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                    ObjectInputStream is = new ObjectInputStream(s.getInputStream());

                    while (!end){
                        GameInfo gm = (GameInfo) is.readObject();

                        Intent intent = new Intent("refresh");
                        intent.putExtra("gameInfo", gm);
                        LocalBroadcastManager.getInstance(c).sendBroadcast(intent);

                        while (gameInfo == null)
                            Thread.sleep(150);
                        os.writeObject(gameInfo);
                        gameInfo = null;
                    }

                    os.close();
                    s.close();
                } catch (InterruptedException | IOException | ClassNotFoundException e) {
                    e.printStackTrace();

                    Intent intent = new Intent(c, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("error", e.toString());
                    c.startActivity(intent);
                }
            }
        }).start();
    }
}