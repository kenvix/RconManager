// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.main.view.servers;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.rcon.server.RconServer;
import com.kenvix.rconmanager.ui.base.view.IconManager;
import com.kenvix.rconmanager.ui.base.view.base.BaseHolder;
import com.kenvix.utils.annotation.ViewAutoLoad;

public class ServerHolder extends BaseHolder<RconServer> {
    public ImageView imageView;
    @ViewAutoLoad public TextView serverName;
    @ViewAutoLoad public TextView serverAddress;
    @ViewAutoLoad public LinearLayout serverItem;

    public ServerHolder(@NonNull View itemView) {
        super(itemView);
        imageView = IconManager.getInstance().getIcon(R.drawable.ic_server);
        //serverNameView = itemView.findViewById(R.id.server_name);
        //serverAddressView = itemView.findViewById(R.id.server_address);
        //serverItem =
    }

    @Override
    public void bindView(RconServer server) {
        serverName.setText(server.getName());
        serverAddress.setText(server.getHostAndPort());
    }
}
