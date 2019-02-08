package com.kenvix.rconmanager.ui.connection;

import com.kenvix.rconmanager.rcon.meta.RconCommandResult;
import com.kenvix.rconmanager.rcon.protocol.RconConnect;
import com.kenvix.rconmanager.ui.base.BaseAsyncTask;

import java.lang.ref.WeakReference;

class RconCommanderAsyncTask extends BaseAsyncTask<String, Void, RconCommandResult> {
    private final WeakReference<ConnectionActivity> activityWeakReference;
    private final RconConnect rconConnect;

    public RconCommanderAsyncTask(RconConnect connect, ConnectionActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
        rconConnect = connect;
    }

    @Override
    protected RconCommandResult doInBackground(String... commands) {

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
        if(getException() == null) {
            if(rconCommandResult != null) {
                activityWeakReference.get().appendCommandResult(rconCommandResult.getResult());
            }
        } else {
            activityWeakReference.get().appendCommandResult("[ERROR] Failed to run command : " + getException().toString());
        }
    }

}
