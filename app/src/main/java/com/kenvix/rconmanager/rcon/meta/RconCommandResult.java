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

    public RconCommandResult(@NonNull String command, @NonNull String result) {
        this.command = command;
        this.result = result;
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
