package com.kenvix.rconmanager.ui.connection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kenvix.rconmanager.ApplicationEnvironment;
import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.rcon.meta.RconServer;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.utils.annotation.ViewAutoLoad;

public class ConnectionActivity extends BaseActivity {
    public static final String ExtraRconServer = "rcon_server";
    private RconServer rconServer;

    @ViewAutoLoad public Button connectionCommandPrev;
    @ViewAutoLoad public Button connectionCommandNext;
    @ViewAutoLoad public Button connectionCommandRun;
    @ViewAutoLoad public Button connectionCommandHistory;
    @ViewAutoLoad public Button connectionQuickCommand;
    @ViewAutoLoad public TextView connectionCommandArea;
    @ViewAutoLoad public EditText connectionCommandText;
    @ViewAutoLoad public Toolbar connectionToolbar;

    @Override
    protected void onInitialize() {

        try {
            Intent intent = getIntent();
            rconServer = intent.getParcelableExtra(ExtraRconServer);

            if(rconServer == null)
                throw new IllegalArgumentException("ExtraRconServer CAN'T be null");

            connectionToolbar.setTitle(rconServer.getName());

            setSupportActionBar(connectionToolbar);
        } catch (Exception ex) {
            exceptionToastPrompt(ex);
            //setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notifyCode = rconServer.hashCode();

        Intent openActivityIntent = new Intent(this, ConnectionActivity.class);
        openActivityIntent.putExtra(ExtraRconServer, rconServer);
        PendingIntent openActivityPendingIntent = PendingIntent.getActivity(this, 0, openActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        Notification notification = new NotificationCompat.Builder(this, ApplicationEnvironment.NotificationChannelName.RconConnection)
                .setContentTitle(getString(R.string.prompt_connectied_to) + rconServer.getName())
                .setContentText(rconServer.getHostAndPort())
                .setContentIntent(openActivityPendingIntent)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.ic_server_3)
                .setWhen(System.currentTimeMillis())
                .build();

        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        notificationManager.notify(ApplicationEnvironment.NotificationChannelName.RconConnection, notifyCode, notification);
        Log.d("Rcon Connection", "Frontend paused, hashcode: " + notifyCode);
    }

    @Override
    protected void onStart() {
        super.onStart();

        int notifyCode = rconServer.hashCode();

        Log.d("Rcon Connection", "Frontend started, hashcode: " + notifyCode);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ApplicationEnvironment.NotificationChannelName.RconConnection, notifyCode);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.connection_command_area:
                return true;

            case R.id.connection_exit:
                return true;

            case R.id.connection_back:
                return true;

            case R.id.connection_clean_command_history:
                return true;

            case R.id.connection_connection_info:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected int getBaseLayout() {
        return R.layout.activity_connection;
    }

    @Override
    protected int getBaseContainer() {
        return 0;
    }
}
