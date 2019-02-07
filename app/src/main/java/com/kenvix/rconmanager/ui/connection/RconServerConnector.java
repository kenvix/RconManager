package com.kenvix.rconmanager.ui.connection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.rcon.protocol.RconConnect;

import java.lang.ref.WeakReference;

class RconServerConnector extends AsyncTask<Void, Void, RconConnect> {
    private ProgressDialog progressDialog;
    private RuntimeException exception = null;
    private WeakReference<ConnectionActivity> activityWeakReference;

    RconServerConnector(ConnectionActivity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    protected RconConnect doInBackground(Void... voids) {
        RconConnect connect = new RconConnect(activityWeakReference.get().getRconServer());

        try {
            connect.connect();
        } catch (RuntimeException ex) {
            exception = ex;
        }

        return connect;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ConnectionActivity activity = activityWeakReference.get();

        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setMessage(activity.getString(R.string.prompt_connecting, activity.getRconServer().getName(), activity.getRconServer().getHostAndPort()));
        progressDialog.setCancelable(true);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(R.string.action_cancel), ((dialog, which) -> this.cancel(true)));
        progressDialog.setOnCancelListener(dialog -> this.cancel(true));
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(RconConnect rconConnect) {
        super.onPostExecute(rconConnect);
        ConnectionActivity activity = activityWeakReference.get();

        if(progressDialog.isShowing())
            progressDialog.dismiss();

        if(getException() != null) {
            activity.alertDialog(activity.getString(R.string.prompt_connection_failed, activity.getRconServer().getHostAndPort(), getException().toString()), result -> exitForError());
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        exitForError();
    }

    public RuntimeException getException() {
        return exception;
    }

    private void exitForError() {
        ConnectionActivity activity = activityWeakReference.get();

        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }
}