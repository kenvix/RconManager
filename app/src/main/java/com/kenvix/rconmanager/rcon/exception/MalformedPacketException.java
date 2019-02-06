package com.kenvix.rconmanager.rcon.exception;

public class MalformedPacketException extends RuntimeException {
    public MalformedPacketException(String message) {
        super(message);
    }
}
