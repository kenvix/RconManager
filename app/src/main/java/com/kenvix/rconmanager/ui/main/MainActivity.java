// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.main;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kenvix.rconmanager.ApplicationEnvironment;
import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.database.dao.ServerModel;
import com.kenvix.rconmanager.rcon.meta.RconServer;
import com.kenvix.rconmanager.ui.addserver.AddServerActivity;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.rconmanager.ui.main.view.servers.ServerAdapter;
import com.kenvix.rconmanager.ui.setting.SettingActivity;
import com.kenvix.utils.annotation.ViewAutoLoad;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    public static final int ActivityRequestCode = 0xac02;

    public static final String ExtraPromptText = "prompt_text";
    public static final String ExtraRequestReload = "request_reload";

    @ViewAutoLoad public FloatingActionButton mainFab;
    @ViewAutoLoad public Toolbar mainToolbar;
    @ViewAutoLoad public RecyclerView mainServers;

    private ServerAdapter serverAdapter;
    private ServerModel serverModel;

    @Override
    protected void onInitialize() {
        ApplicationEnvironment.initializeApplication(this);

        setSupportActionBar(mainToolbar);

        mainFab.setOnClickListener(view -> AddServerActivity.startActivity(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        reloadServerRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && resultCode == RESULT_OK) {

            if(requestCode == AddServerActivity.ActivityRequestCode) {
                if(data.getBooleanExtra(AddServerActivity.ParamRequestReload, false))
                    reloadServerRecyclerView();
            }

            String promptText = data.getStringExtra(ExtraPromptText);
            if(!promptText.isEmpty())
                snackbar(promptText);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_action_settings) {
            SettingActivity.startActivity(this);
            return true;
        } else if(id == R.id.main_action_refresh) {
            reloadServerRecyclerView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected int getBaseContainer() {
        return R.id.main_container;
    }

    public void reloadServerRecyclerView() {
        List<RconServer> servers = new ArrayList<>();
        serverModel = new ServerModel(this);

        try (Cursor serverCursor = serverModel.getAll()) {

            while (serverCursor.moveToNext()) {
                servers.add(new RconServer(
                        serverCursor.getString(serverCursor.getColumnIndexOrThrow(ServerModel.FieldName)),
                        serverCursor.getString(serverCursor.getColumnIndexOrThrow(ServerModel.FieldHost)),
                        serverCursor.getInt(serverCursor.getColumnIndexOrThrow(ServerModel.FieldPort)),
                        serverCursor.getString(serverCursor.getColumnIndexOrThrow(ServerModel.FieldPassword))
                ).setSid(serverCursor.getInt(serverCursor.getColumnIndexOrThrow(ServerModel.FieldSid))));
            }

        } catch (Exception ex) {
            toast(getString(R.string.error_unable_load_servers) + ex.getLocalizedMessage());
            ex.printStackTrace();
        }

        serverAdapter = new ServerAdapter(servers, this);
        serverAdapter.initializeRecyclerView(getWindow().getDecorView());
    }

    public ServerModel getServerModel() {
        return serverModel;
    }
}
