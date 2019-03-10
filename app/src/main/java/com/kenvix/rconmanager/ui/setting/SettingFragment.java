package com.kenvix.rconmanager.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.kenvix.rconmanager.DefaultPreferences;
import com.kenvix.rconmanager.R;

import java.util.Objects;

public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);

        findPreference(DefaultPreferences.KeyTerminalTextSize)
                .setOnPreferenceChangeListener((preference, o) -> !o.toString().equals("") && o.toString().matches("\\d*"));

        findPreference("app_name").setOnPreferenceClickListener(preference -> openURL("market://details?id=" + Objects.requireNonNull(getContext()).getPackageName()));
        findPreference("view_github").setOnPreferenceClickListener(preference -> openURL("https://github.com/kenvix/RconManager"));
        findPreference("author").setOnPreferenceClickListener(preference -> openURL("https://kenvix.com"));
    }

    public boolean openURL(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception ex) {
            Log.w("OpenURL","Exception occurred:" + ex);
        }
        return false;
    }
}
