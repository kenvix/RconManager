// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.rcon.server;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.URI;

public class GeneralRconServer extends RconServer {

    public GeneralRconServer(@NonNull String name, @NonNull String host, int port,  @NonNull String password) {
        super(name, host, port, password);
    }

    @Override
    public void connect() throws IOException {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void runCommand(String command) throws IOException {

    }
}
