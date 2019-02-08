// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.main.view.servers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.database.dao.ServerModel;
import com.kenvix.rconmanager.rcon.meta.RconServer;
import com.kenvix.rconmanager.ui.addserver.AddServerActivity;
import com.kenvix.rconmanager.ui.base.view.IconManager;
import com.kenvix.rconmanager.ui.base.view.base.BaseHolder;
import com.kenvix.rconmanager.ui.connection.ConnectionActivity;
import com.kenvix.rconmanager.ui.main.MainActivity;
import com.kenvix.utils.annotation.ViewAutoLoad;

public class ServerHolder extends BaseHolder<RconServer> implements View.OnCreateContextMenuListener {
    public ImageView imageView;
    private RconServer rconServer;
    @ViewAutoLoad public TextView serverName;
    @ViewAutoLoad public TextView serverAddress;
    @ViewAutoLoad public LinearLayout serverItem;

    public ServerHolder(@NonNull View itemView) {
        super(itemView);
        imageView = IconManager.getInstance().getIcon(R.drawable.ic_server);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void bindView(RconServer server) {
        this.rconServer = server;
        serverName.setText(server.getName());
        serverAddress.setText(server.getHostAndPort());

        serverItem.setOnClickListener(view -> ConnectionActivity.startActivity(getActivityByView(view), rconServer));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MainActivity activity = (MainActivity) getActivityByView(v);
        activity.getMenuInflater().inflate(R.menu.menu_server_item, menu);
        ServerModel serverModel = activity.getServerModel();

        menu.findItem(R.id.action_server_item_delete).setOnMenuItemClickListener(view -> {
            activity.confirmDialog(activity.getString(R.string.confirm_server_delete, rconServer.getName()), result -> {
                if(result) {
                    try {
                        serverModel.deleteBySid(rconServer.getSid());
                        activity.snackbar(activity.getString(R.string.prompt_deleted, rconServer.getName()));
                        activity.reloadServerRecyclerView();
                    } catch (Exception ex) {
                        activity.exceptionSnackbarPrompt(ex);
                    }
                }
            });
            return true;
        });

        menu.findItem(R.id.action_server_item_duplicate).setOnMenuItemClickListener(view -> {
            try {
                RconServer newInstance = rconServer.clone();
                newInstance.setName(newInstance.getName() + activity.getString(R.string.name_duplicated));
                serverModel.add(newInstance);

                activity.snackbar(activity.getString(R.string.success_duplicated, rconServer.getName()));
                activity.reloadServerRecyclerView();
            } catch (Exception ex) {
                activity.exceptionSnackbarPrompt(ex);
            }
            return true;
        });

        menu.findItem(R.id.action_server_item_edit).setOnMenuItemClickListener(view -> {
            AddServerActivity.startActivity(activity, rconServer.getSid());
            return true;
        });

        menu.findItem(R.id.action_server_item_share).setOnMenuItemClickListener(view -> {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.prompt_share_server));
            shareIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.text_share_server, rconServer.getName(), rconServer.getHost(), rconServer.getPort(), rconServer.getPassword()));

            Intent shareIntentChooser = Intent.createChooser(shareIntent, activity.getString(R.string.prompt_share_server));
            activity.startActivity(shareIntentChooser);
            return true;
        });
    }
}
