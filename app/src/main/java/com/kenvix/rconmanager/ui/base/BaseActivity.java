// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kenvix.rconmanager.utils.UITools;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        UITools.initializeViewFields(this);

        initializeElements();
    }

    protected abstract void initializeElements();
    protected abstract int getLayout();
}
