package com.kenvix.rconmanager.ui.main;

import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.database.dao.ServerModel;
import com.kenvix.rconmanager.rcon.meta.RconServer;
import com.kenvix.rconmanager.ui.addserver.AddServerActivity;
import com.kenvix.rconmanager.ui.base.BaseFragment;
import com.kenvix.rconmanager.ui.main.view.servers.ServerAdapter;
import com.kenvix.utils.annotation.ViewAutoLoad;

import java.util.ArrayList;
import java.util.List;

public class ServersFragment extends BaseFragment {
    @ViewAutoLoad public FloatingActionButton mainFab;
    @ViewAutoLoad public RecyclerView mainServers;

    private ServerAdapter serverAdapter;
    private ServerModel serverModel;

    @Override
    protected void onInitialize(View v) {
        reloadServerRecyclerView();
        mainFab.setOnClickListener(view -> AddServerActivity.startActivity(getActivity()));
    }

    @Override
    protected int getFragmentContentLayout() {
        return R.layout.fragment_servers;
    }

    @Override
    protected int getBaseActivityContainer() {
        return R.id.main_fragment_container;
    }

    public void reloadServerRecyclerView() {
        List<RconServer> servers = new ArrayList<>();
        serverModel = new ServerModel(getContext());

        try (Cursor serverCursor = serverModel.getAllAsCursor()) {

            while (serverCursor.moveToNext()) {
                servers.add(new RconServer(
                        serverCursor.getString(serverCursor.getColumnIndexOrThrow(ServerModel.FieldName)),
                        serverCursor.getString(serverCursor.getColumnIndexOrThrow(ServerModel.FieldHost)),
                        serverCursor.getInt(serverCursor.getColumnIndexOrThrow(ServerModel.FieldPort)),
                        serverCursor.getString(serverCursor.getColumnIndexOrThrow(ServerModel.FieldPassword))
                ).setSid(serverCursor.getInt(serverCursor.getColumnIndexOrThrow(ServerModel.FieldSid))));
            }

        } catch (Exception ex) {
            getBaseActivity().toast(getString(R.string.error_unable_load_servers) + ex.getLocalizedMessage());
            ex.printStackTrace();
        }

        serverAdapter = new ServerAdapter(servers, getContext());


        serverAdapter.initializeRecyclerView(getBaseActivity().getWindow().getDecorView());
    }

    public ServerModel getServerModel() {
        return serverModel;
    }
}
