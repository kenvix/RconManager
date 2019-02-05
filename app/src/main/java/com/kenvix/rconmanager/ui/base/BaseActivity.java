// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.utils.Invoker;

import java.util.function.Consumer;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        Invoker.invokeViewAutoLoader(this);

        initializeElements();
    }

    public void toast(String text, int toastLength) {
        Toast.makeText(this, text, toastLength).show();
    }

    public void toast(String text) {
        toast(text, Toast.LENGTH_LONG);
    }

    public void snackbar(View container, String text, int snackLength) {
        Snackbar.make(container, text, snackLength).show();
    }

    public void exceptionPrompt(Throwable throwable) {
        toast(getString(R.string.error_operation_failed) + throwable.getLocalizedMessage());
        Log.w("Global Exception Prompt", "Operation FAILED: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public void confirmDialog(String text, @Nullable String title, @Nullable Consumer<Boolean> callback) {
        getConfirmBuilder(text, title, callback).show();
    }

    public void confirmDialog(String text, @Nullable Consumer<Boolean> callback) {
        getConfirmBuilder(text, null, callback).show();
    }

    public void alertDialog(String text, @Nullable String title, @Nullable Consumer<Boolean> callback) {
        getAlertBuilder(text, title, callback).show();
    }

    public void alertDialog(String text, @Nullable Consumer<Boolean> callback) {
        getAlertBuilder(text, null, callback).show();
    }

    public void alertDialog(String text) {
        getAlertBuilder(text, null, null).show();
    }

    public AlertDialog.Builder getAlertBuilder(String text, @Nullable String title, @Nullable Consumer<Boolean> callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(text)
                .setPositiveButton(getString(R.string.action_ok), (dialog, which) -> {
                    if (callback != null)
                        callback.accept(true);
                });

        if (title == null)
            return builder;
        else
            return builder.setTitle(title);
    }

    public AlertDialog.Builder getConfirmBuilder(String text, @Nullable String title, @Nullable Consumer<Boolean> callback) {
        return getAlertBuilder(text, title, callback)
                .setNegativeButton(getString(R.string.action_cancel),(dialog, which) -> {
                    if(callback != null)
                        callback.accept(false);
                });
    }

    protected abstract void initializeElements();
    protected abstract int getLayout();
}
