// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kenvix.rconmanager.ApplicationEnvironment;
import com.kenvix.rconmanager.BuildConfig;
import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.database.dao.ServerModel;
import com.kenvix.rconmanager.rcon.meta.RconServer;
import com.kenvix.rconmanager.ui.addserver.AddServerActivity;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.rconmanager.ui.main.view.servers.ServerAdapter;
import com.kenvix.rconmanager.ui.setting.SettingActivity;
import com.kenvix.rconmanager.ui.setting.SettingFragment;
import com.kenvix.utils.annotation.ViewAutoLoad;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    public static final int ActivityRequestCode = 0xac02;

    private ServersFragment serversFragment;
    private QuickCommandsFragment quickCommandsFragment;

    public static final String ExtraPromptText = "prompt_text";
    public static final String ExtraRequestReload = "request_reload";

    @ViewAutoLoad
    public Toolbar mainToolbar;
    @ViewAutoLoad
    public NavigationView mainNavView;
    @ViewAutoLoad
    public DrawerLayout mainDrawerLayout;

    @Override
    protected void onInitialize() {
        ApplicationEnvironment.initializeApplication(this);

        setSupportActionBar(mainToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainDrawerLayout, mainToolbar, R.string.action_open, R.string.action_close);
        mainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        quickCommandsFragment = new QuickCommandsFragment();
        serversFragment = new ServersFragment();

        mainNavView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        setForegroundFragment(R.id.main_fragment_container, serversFragment);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && resultCode == RESULT_OK) {

            if (requestCode == AddServerActivity.ActivityRequestCode) {
                if (data.getBooleanExtra(AddServerActivity.ParamRequestReload, false)) {
                    reloadCurrentRecyclerView();
                }
            }

            String promptText = data.getStringExtra(ExtraPromptText);
            if (!promptText.isEmpty())
                snackbar(promptText);
        }
    }

    private void reloadCurrentRecyclerView() {
        if(serversFragment.isVisible())
            reloadServerRecyclerView();

        if(quickCommandsFragment.isVisible())
            reloadQuickCommandRecyclerView();
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
        } else if (id == R.id.main_action_refresh) {
            reloadCurrentRecyclerView();
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

    private boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        super.onOptionsItemSelected(item);

        try {
            //noinspection SimplifiableIfStatement
            switch (id) {
                case R.id.nav_server_list:
                    setForegroundFragment(R.id.main_fragment_container, serversFragment);
                    setTitle(getString(R.string.title_main));
                    break;

                case R.id.nav_quick_commands:
                    setForegroundFragment(R.id.main_fragment_container, quickCommandsFragment);
                    setTitle(getString(R.string.title_quick_command));
                    break;

                case R.id.nav_settings:
                    SettingActivity.startActivity(this);
                    break;
            }
        } catch (Exception ex) {
            exceptionSnackbarPrompt(ex);
        }

        mainDrawerLayout.closeDrawers();
        return true;
    }

    public void reloadServerRecyclerView() {
        serversFragment.reloadServerRecyclerView();
    }

    public void reloadQuickCommandRecyclerView() {
        quickCommandsFragment.reloadQuickCommandRecyclerView();
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
