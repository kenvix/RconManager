// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.base.view.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenvix.rconmanager.ui.main.view.servers.ServerHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class BaseAdapter<T extends BaseHolder, U> extends RecyclerView.Adapter<T> {
    private Context context;
    private List<U> data;
    private RecyclerView recyclerView;

    public BaseAdapter(List<U> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract int getRecyclerViewLayoutId();

    protected abstract int getRecyclerViewControlId();

    @NonNull
    protected abstract Class<T> getHolderClass();

    public List<U> getData() {
        return data;
    }

    protected Context getContext() {
        return context;
    }

    public RecyclerView initializeRecyclerView(View view) {
        recyclerView = view.findViewById(getRecyclerViewControlId());
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setAdapter(this);
        return recyclerView;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(getRecyclerViewLayoutId(), viewGroup, false);
        try {
            return getHolderClass().getConstructor(View.class).newInstance(view);
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("Illegal Holder Argument: matched constructor not found: " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Illegal Holder Argument: matched constructor inaccessible: " + ex.getMessage());
        } catch (InstantiationException ex) {
            throw new IllegalArgumentException("Illegal Holder Argument: " + ex.getMessage());
        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException("Illegal Holder Argument: " + ex.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull T holder, int i) {
        holder.bindView(data.get(i));
    }
}