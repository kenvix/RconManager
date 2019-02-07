package com.kenvix.rconmanager.rcon.exception;

import java.io.IOException;

public class ConnectionException extends RuntimeException {
    public IOException getIoException() {
        return ioException;
    }

    private IOException ioException;

    public ConnectionException() {
    }

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, IOException cause) {
        super(message, cause);
        ioException = cause;
    }

    public ConnectionException(IOException cause) {
        super(cause);
        ioException = cause;
    }

    public ConnectionException(String message, IOException cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        ioException = cause;
    }
}
