// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.base;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.utils.Invoker;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        Invoker.invokeViewAutoLoader(this);

        initializeElements();
    }

    protected void makeSimpleToast(String text, int toastLength) {
        Toast.makeText(this, text, toastLength).show();
    }

    protected void makeSimpleToast(String text) {
        makeSimpleToast(text, Toast.LENGTH_LONG);
    }

    protected void makeSimpleSnackbar(View container, String text, int snackLength) {
        Snackbar.make(container, text, snackLength).show();
    }

    protected void makeExceptionPrompt(Throwable throwable) {
        makeSimpleToast(getString(R.string.error_operation_failed) + throwable.getLocalizedMessage());
        Log.w("Global Exception Prompt", "Operation FAILED: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    protected abstract void initializeElements();
    protected abstract int getLayout();
}
