package com.kenvix.rconmanager.ui.main;

import android.view.View;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.ui.base.BaseFragment;

public class QuickCommandsFragment extends BaseFragment {

    @Override
    protected void onInitialize(View view) {

    }

    @Override
    protected int getFragmentContentLayout() {
        return R.layout.fragment_quick_commands;
    }

    @Override
    protected int getBaseActivityContainer() {
        return R.id.main_fragment_container;
    }
}
