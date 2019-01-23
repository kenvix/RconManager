// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.main.view.servers;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenvix.rconmanager.ui.base.view.base.BaseHolder;
import com.kenvix.rconmanager.utils.ViewAutoLoad;

public class ServerHolder extends BaseHolder {
    @ViewAutoLoad
    private ImageView imageView;

    @ViewAutoLoad
    private TextView textView;

    public ServerHolder(@NonNull View itemView) {
        super(itemView);
    }


}
