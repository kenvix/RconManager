package com.kenvix.rconmanager.ui.base;

import android.os.AsyncTask;

public abstract class BaseAsyncTask<T, U, X> extends AsyncTask<T, U, X> {
    private RuntimeException exception = null;

    public RuntimeException getException() {
        return exception;
    }

    public void setException(RuntimeException exception) {
        this.exception = exception;
    }
}

