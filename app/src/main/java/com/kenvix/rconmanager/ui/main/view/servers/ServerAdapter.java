// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.main.view.servers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.rcon.server.RconServer;
import com.kenvix.rconmanager.ui.base.view.base.BaseAdapter;

import java.util.List;

public class ServerAdapter extends BaseAdapter<ServerHolder, RconServer> {

    public ServerAdapter(List<RconServer> servers, Context context) {
        super(servers, context);
    }

    @Override
    @NonNull
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    protected int getRecyclerViewLayoutId() {
        return R.layout.item_server;
    }

    @Override
    protected int getRecyclerViewControlId() {
        return R.id.main_servers;
    }

    @NonNull
    @Override
    protected Class<ServerHolder> getHolderClass() {
        return ServerHolder.class;
    }
}
