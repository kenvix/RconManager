// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.rcon.server;

import android.support.annotation.NonNull;

import com.kenvix.rconmanager.utils.StringTools;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public abstract class RconServer {
    @NonNull
    private String host;

    @NonNull
    private String password;

    @NonNull
    private String name;

    private int port;

    public RconServer(@NonNull String name, @NonNull String host, int port,  @NonNull String password) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getHost() {
        return host;
    }

    public String getHostAndPort() {
        return host + ":" + port;
    }

    public abstract void connect() throws IOException;
    public abstract void disconnect();

    public abstract void runCommand(String command) throws IOException;
}
