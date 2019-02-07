package com.kenvix.rconmanager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.kenvix.rconmanager.rcon.protocol.RconConnect;
import com.kenvix.rconmanager.rcon.meta.RconServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RconConnectionService extends Service {
    public static final String ExtraRconConnect = "rcon_connect";

    private Map<RconServer, RconConnect> connectPool;
    private ExecutorService threadPool;

    @Override
    public void onCreate() {
        super.onCreate();
        connectPool = new HashMap<>();
        threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RconConnect rconConnect = intent.getParcelableExtra(ExtraRconConnect);

        if(rconConnect == null)
            throw new IllegalArgumentException("ExtraRconConnect CAN'T be null");
        if(rconConnect.getStatus() != RconConnect.Status.Connected || rconConnect.getStatus() != RconConnect.Status.Working)
            throw new IllegalArgumentException("RconConnect MUST be connected or working");

        RconServer rconServer = rconConnect.getRconServer();

        if(!connectPool.containsKey(rconServer))
            connectPool.put(rconServer, rconConnect);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
