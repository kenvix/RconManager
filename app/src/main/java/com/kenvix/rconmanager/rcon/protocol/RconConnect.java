// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.rcon.protocol;

import com.kenvix.rconmanager.rcon.meta.RconCommandResult;
import com.kenvix.rconmanager.rcon.meta.RconServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RconConnect {
    private RconServer rconServer;
    private List<RconCommandResult> commandResults;
    private Status status = Status.Ready;

    public List<RconCommandResult> getCommandResults() {
        return commandResults;
    }

    public Status getStatus() {
        return status;
    }

    public Status recheckConnectionStatus() {
        return status;
    }

    public enum Status { Ready, Connected, Working, Disconnected }

    public RconConnect(RconServer rconServer) {
        this.rconServer = rconServer;
        commandResults = new ArrayList<>();
    }


    public void connect() {

    }

    public void connectWithEventLoop() {

    }

    public void disconnect() {

    }

    public void runCommand(String command) throws IOException {

    }

    @Override
    public int hashCode() {
        return 0xFA02 + rconServer.hashCode() ^ commandResults.hashCode();
    }
}
