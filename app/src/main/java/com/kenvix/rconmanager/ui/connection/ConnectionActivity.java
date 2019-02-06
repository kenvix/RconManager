package com.kenvix.rconmanager.ui.connection;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.rcon.server.RconServer;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.utils.annotation.ViewAutoLoad;

import java.io.ObjectInputStream;

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
    protected void initializeElements() {
        Intent intent = getIntent();
        rconServer = intent.getParcelableExtra(ExtraRconServer);
        connectionToolbar.setTitle(rconServer.getName());
        setSupportActionBar(connectionToolbar);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(getString(R.string.prompt_connectied_to) + rconServer.getName())
                .setContentText(rconServer.getHostAndPort())
                .setCategory(Notification.CATEGORY_SERVICE)
                .setWhen(System.currentTimeMillis())
                .build();

        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_FOREGROUND_SERVICE;

        notificationManager.notify();

    }

    @Override
    protected void onStart() {
        super.onStart();

        toast("start");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        toast("restart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        toast("stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toast("destroy");
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
