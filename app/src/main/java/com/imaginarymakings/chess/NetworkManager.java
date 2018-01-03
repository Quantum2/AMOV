package com.imaginarymakings.chess;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by rafaelfrancisco on 02/01/18.
 */

public class NetworkManager {
    private static final int SERVERPORT = 6000;

    private Context c;

    public NetworkManager(Context c) {
        this.c = c;
    }

    public String getCurrentIP(){
        WifiManager wifiMan = (WifiManager) c.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        return String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
    }
}
