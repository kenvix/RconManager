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

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.database.dao.ServerModel;
import com.kenvix.rconmanager.rcon.server.GeneralRconServer;
import com.kenvix.rconmanager.rcon.server.RconServer;
import com.kenvix.rconmanager.ui.addserver.AddServerActivity;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.rconmanager.ui.base.view.IconManager;
import com.kenvix.rconmanager.ui.main.view.servers.ServerAdapter;
import com.kenvix.utils.annotation.ViewAutoLoad;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    public static final int StartAddServerActivityRequestCode = 0xac00;

    @ViewAutoLoad public FloatingActionButton mainFab;
    @ViewAutoLoad public Toolbar mainToolbar;
    @ViewAutoLoad public RecyclerView mainServers;

    private ServerAdapter serverAdapter;
    private ServerModel serverModel;

    private void initializeApplication() {
        IconManager.initialize(this);
    }

    @Override
    protected void initializeElements() {
        initializeApplication();

        setSupportActionBar(mainToolbar);
        loadServerRecyclerView();

        mainFab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddServerActivity.class);
            startActivityForResult(intent, StartAddServerActivityRequestCode);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && resultCode == RESULT_OK) {

            if(requestCode == StartAddServerActivityRequestCode) {
                if(data.getBooleanExtra(AddServerActivity.ParamRequestReload, false))
                    loadServerRecyclerView();
            }

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    private void loadServerRecyclerView() {
        List<RconServer> servers = new ArrayList<>();
        serverModel = new ServerModel(this);

        try (Cursor serverCursor = serverModel.getAll()) {

            while (serverCursor.moveToNext()) {
                servers.add(new GeneralRconServer(
                        serverCursor.getString(serverCursor.getColumnIndexOrThrow(ServerModel.FieldName)),
                        serverCursor.getString(serverCursor.getColumnIndexOrThrow(ServerModel.FieldHost)),
                        serverCursor.getInt(serverCursor.getColumnIndexOrThrow(ServerModel.FieldPort)),
                        serverCursor.getString(serverCursor.getColumnIndexOrThrow(ServerModel.FieldPassword))
                ));
            }

        } catch (Exception ex) {
            makeSimpleToast(getString(R.string.error_unable_load_servers) + ex.getLocalizedMessage());
            ex.printStackTrace();
        }

        serverAdapter = new ServerAdapter(servers, this);
        serverAdapter.initializeRecyclerView(getWindow().getDecorView());
    }
}
