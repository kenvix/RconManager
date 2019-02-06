package com.kenvix.rconmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.kenvix.rconmanager.rcon.protocol.RconConnect;
import com.kenvix.rconmanager.rcon.meta.RconServer;

import java.util.HashMap;
import java.util.Map;

public class RconConnectionService extends Service {
    public static final String ExtraRconServer = "rcon_server";

    private Map<RconServer, RconConnect> connectPool;
    private Map<RconServer, Thread> threadPool;

    @Override
    public void onCreate() {
        super.onCreate();
        connectPool = new HashMap<>();
        threadPool = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RconServer rconServer = intent.getParcelableExtra(ExtraRconServer);

        if(rconServer == null)
            throw new IllegalArgumentException("ExtraRconServer CAN'T be null");

        if(!connectPool.containsKey(rconServer)) {
            RconConnect connect = new RconConnect(rconServer);

            connectPool.put(rconServer, connect);
            threadPool.put(rconServer, new Thread(connect::connect));
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
