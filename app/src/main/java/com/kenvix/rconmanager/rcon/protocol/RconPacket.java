package com.kenvix.rconmanager.rcon.protocol;

final class RconPacket {

    static final int SERVERDATA_EXECCOMMAND = 2;
    static final int SERVERDATA_AUTH = 3;

    private int requestId;
    private int type;
    private byte[] payload;

    RconPacket(int requestId, int type, byte[] payload) {
        this.requestId = requestId;
        this.type = type;
        this.payload = payload;
    }

    int getRequestId() {
        return requestId;
    }

    int getType() {
        return type;
    }

    byte[] getPayload() {
        return payload;
    }

}