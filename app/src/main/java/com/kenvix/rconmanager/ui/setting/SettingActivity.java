package com.kenvix.rconmanager.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.utils.annotation.ViewAutoLoad;

public class SettingActivity extends BaseActivity {
    public static final int ActivityRequestCode = 0xac01;

    @ViewAutoLoad public  Toolbar         settingToolbar;

    @Override
    protected void onInitialize() {
        setSupportActionBar(settingToolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.setting_fragment_container, new SettingFragment());
        settingToolbar.setNavigationOnClickListener(view -> finish());
        transaction.commit();
    }

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected int getBaseContainer() {
        return R.id.setting_container;
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivityForResult(intent, ActivityRequestCode);
    }
}
