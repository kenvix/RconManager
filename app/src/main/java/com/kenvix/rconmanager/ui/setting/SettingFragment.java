package com.kenvix.rconmanager.ui.setting;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.kenvix.rconmanager.DefaultPreferences;
import com.kenvix.rconmanager.R;

public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);

        findPreference(DefaultPreferences.KeyTerminalTextSize)
                .setOnPreferenceChangeListener((preference, o) -> !o.toString().equals("") && o.toString().matches("\\d*"));

    }

}
