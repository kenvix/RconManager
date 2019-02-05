// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.rcon.server;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;

public abstract class RconServer implements Cloneable, Serializable {
    private int sid = -1;
    private String host;
    private String password;
    private String name;
    private int port;

    public RconServer(String name, String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.name = name;
    }

    public RconServer() {
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

    public int getSid() {
        return sid;
    }

    public RconServer setSid(int sid) {
        this.sid = sid;
        return this;
    }

    public String getHostAndPort() {
        return host + ":" + port;
    }

    public abstract void connect() throws IOException;
    public abstract void disconnect();
    public abstract void runCommand(String command) throws IOException;

    @Override
    public RconServer clone() throws CloneNotSupportedException {
       return (RconServer) super.clone();
    }

    public void setHost(@NonNull String host) {
        this.host = host;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
