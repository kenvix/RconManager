package com.kenvix.rconmanager.ui.main;

import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.database.dao.QuickCommandModel;
import com.kenvix.rconmanager.meta.QuickCommand;
import com.kenvix.rconmanager.ui.addquickcommand.AddQuickCommandActivity;
import com.kenvix.rconmanager.ui.base.BaseFragment;
import com.kenvix.rconmanager.ui.main.view.quickcommands.QuickCommandsAdapter;
import com.kenvix.utils.annotation.ViewAutoLoad;

import java.util.ArrayList;
import java.util.List;

public class QuickCommandsFragment extends BaseFragment {
    @ViewAutoLoad
    public FloatingActionButton mainQuickCommandsFab;
    @ViewAutoLoad public RecyclerView mainQuickCommands;

    private QuickCommandsAdapter quickCommandsAdapter;
    private QuickCommandModel quickCommandModel;

    @Override
    protected void onInitialize(View v) {
        reloadQuickCommandRecyclerView();
        mainQuickCommandsFab.setOnClickListener(view -> AddQuickCommandActivity.startActivity(getActivity()));
    }

    @Override
    protected int getFragmentContentLayout() {
        return R.layout.fragment_quick_commands;
    }

    @Override
    protected int getBaseActivityContainer() {
        return R.id.main_fragment_container;
    }

    public void reloadQuickCommandRecyclerView() {
        List<QuickCommand> quickCommands;
        quickCommandModel = new QuickCommandModel(getContext());

        try {
            quickCommands = quickCommandModel.getAllAsList();

            quickCommandsAdapter = new QuickCommandsAdapter(quickCommands, getContext());
            quickCommandsAdapter.initializeRecyclerView(getBaseActivity().getWindow().getDecorView());
        } catch (Exception ex) {
            getBaseActivity().toast(getString(R.string.error_unable_load_servers) + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    public QuickCommandModel getQuickCommandModel() {
        return quickCommandModel;
    }
}
