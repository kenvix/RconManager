package com.kenvix.rconmanager.ui.connection.view.commands;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.kenvix.rconmanager.ui.base.view.base.BaseAdapter;

import java.util.List;

public class CommandResultAdapter extends BaseAdapter {
    public CommandResultAdapter(List data, Context context) {
        super(data, context);
    }

    @NonNull
    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    protected int getRecyclerViewLayoutId() {
        return 0;
    }

    @Override
    protected int getRecyclerViewControlId() {
        return 0;
    }

    @NonNull
    @Override
    protected Class getHolderClass() {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }
}
