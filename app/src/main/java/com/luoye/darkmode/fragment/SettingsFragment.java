package com.luoye.darkmode.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luoye.darkmode.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class SettingsFragment extends PreferenceFragment {
    private CheckBoxPreference openPreference;
    private ListPreference modePreference;
    private Properties properties;
    public final static   File PROP_FILE=new File("/sdcard/darkmode/settings.ini");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addPreferencesFromResource(R.xml.settings);
        openPreference=(CheckBoxPreference) findPreference("switch");
        modePreference=(ListPreference) findPreference("mode");
        if(!PROP_FILE.exists()){
            PROP_FILE.getParentFile().mkdir();
        }
        properties=new Properties();

        try {
            properties.load(new FileReader(PROP_FILE));
            if(properties.getProperty("open").equals("false")){
                openPreference.setChecked(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        openPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean open=(boolean)o;
                properties.setProperty("open",open+"");
                try {
                    properties.store(new FileOutputStream(PROP_FILE),"open");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        modePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String mode=(String)o;
                properties.setProperty("mode",mode+"");
                try {
                    properties.store(new FileOutputStream(PROP_FILE),"open");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
