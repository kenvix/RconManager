// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.rcon.meta;

import android.support.annotation.NonNull;

public class RconCommandResult {
    @NonNull
    private String command;
    @NonNull
    private String result;
    private int ping;

    public RconCommandResult(@NonNull String command, @NonNull String result, int ping) {
        this.command = command;
        this.ping = ping;
        this.result = result;
    }

    public int getPing() {
        return ping;
    }

    @NonNull
    public String getResult() {
        return result;
    }

    @NonNull
    public String getCommand() {
        return command;
    }

    @NonNull
    @Override
    public String toString() {
        return command;
    }
}
