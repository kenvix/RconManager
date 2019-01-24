// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.rcon.server.GeneralRconServer;
import com.kenvix.rconmanager.rcon.server.RconServer;
import com.kenvix.rconmanager.ui.addserver.AddServerActivity;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.rconmanager.ui.base.view.IconManager;
import com.kenvix.rconmanager.ui.main.view.servers.ServerAdapter;
import com.kenvix.rconmanager.utils.UITools;
import com.kenvix.rconmanager.utils.ViewAutoLoad;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MainActivity extends BaseActivity {
    @ViewAutoLoad
    private FloatingActionButton mainFab;

    @ViewAutoLoad
    private Toolbar mainToolbar;

    @ViewAutoLoad
    private RecyclerView mainServers;

    private ServerAdapter serverAdapter;

    private void initializeApplication() {
        IconManager.initialize(this);
    }

    @Override
    protected void initializeElements() {
        initializeApplication();

        setSupportActionBar(mainToolbar);

        List<RconServer> servers = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> servers.add(new GeneralRconServer("fuck", "sadsadsa", 11, "saddsa")));

        serverAdapter = new ServerAdapter(servers, this);
        serverAdapter.initializeRecyclerView(getWindow().getDecorView());

        mainFab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddServerActivity.class);
            startActivity(intent);
        });

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
}
