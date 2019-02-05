package com.kenvix.rconmanager;

import android.support.v7.app.AppCompatActivity;

import com.kenvix.rconmanager.ui.base.view.IconManager;
import com.kenvix.rconmanager.utils.Invoker;

public final class ApplicationEnvironment {
    public static void initializeApplication(AppCompatActivity baseActivity) {
        if(Invoker.getBaseActivityInvocation() == null) {
            IconManager.initialize(baseActivity);
            Invoker.setBaseActivityInvocation(baseActivity);
        }
    }
}
