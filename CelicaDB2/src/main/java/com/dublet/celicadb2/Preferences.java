package com.dublet.celicadb2;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by dublet on 23/12/13.
 */
public class Preferences extends PreferenceActivity {

    public static final String PREFS_NAME = "cardb";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(PREFS_NAME);
        addPreferencesFromResource(R.xml.preferences);

    }
}
