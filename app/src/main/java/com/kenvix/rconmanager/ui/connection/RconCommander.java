package com.kenvix.rconmanager.ui.connection;

import com.kenvix.rconmanager.rcon.meta.RconCommandResult;
import com.kenvix.rconmanager.rcon.protocol.RconConnect;
import com.kenvix.rconmanager.ui.base.BaseAsyncTask;

import java.lang.ref.WeakReference;

class RconCommanderAsyncTask extends BaseAsyncTask<String, Void, RconCommandResult> {
    private final WeakReference<ConnectionActivity> activityWeakReference;
    private final WeakReference<RconConnect> rconConnectWeakReference;

    public RconCommanderAsyncTask(RconConnect connect, ConnectionActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
        rconConnectWeakReference = new WeakReference<>(connect);
    }

    @Override
    protected RconCommandResult doInBackground(String... commands) {
        RconConnect rconConnect = rconConnectWeakReference.get();

        try {
            return rconConnect.command(commands[0]);
        } catch (RuntimeException ex) {
            setException(ex);
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(RconCommandResult rconCommandResult) {
        super.onPostExecute(rconCommandResult);
    }

}
