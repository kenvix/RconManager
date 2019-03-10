package com.kenvix.rconmanager.rcon.protocol;

final class RconPacket {

    /**
     * SERVERDATA_EXECCOMMAND
     * SERVERDATA_AUTH_RESPONSE
     */
    static final int PacketExecCommand = 2;
    /**
     * SERVERDATA_AUTH
     */
    static final int PacketAuth = 3;

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