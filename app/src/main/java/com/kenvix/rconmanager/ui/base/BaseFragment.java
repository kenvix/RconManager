// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenvix.rconmanager.utils.Invoker;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getActivity() == null)
            throw new IllegalArgumentException("Activity cant be null");

        if(!(getActivity() instanceof BaseActivity))
            throw new IllegalArgumentException("All Activity must extends BaseActivity");

        return inflater.inflate(getFragmentContentLayout(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Invoker.invokeViewAutoLoader(this, view);
        onInitialize(view);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected abstract void onInitialize(View view);
    protected abstract int getFragmentContentLayout();
    protected abstract int getBaseActivityContainer();
}
