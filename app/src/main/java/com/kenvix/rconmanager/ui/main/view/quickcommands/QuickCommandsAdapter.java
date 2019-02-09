// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.main.view.quickcommands;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.meta.QuickCommand;
import com.kenvix.rconmanager.ui.base.view.base.BaseAdapter;

import java.util.List;

public class QuickCommandsAdapter extends BaseAdapter<QuickCommandsHolder, QuickCommand> {

    public QuickCommandsAdapter(List<QuickCommand> quickCommands, Context context) {
        super(quickCommands, context);
    }

    @Override
    @NonNull
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    protected int getRecyclerViewLayoutId() {
        return R.layout.item_quick_command;
    }

    @Override
    protected int getRecyclerViewControlId() {
        return R.id.main_quick_commands;
    }

    @NonNull
    @Override
    protected Class<QuickCommandsHolder> getHolderClass() {
        return QuickCommandsHolder.class;
    }
}
