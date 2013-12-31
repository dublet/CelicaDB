package com.dublet.celicadb2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by dublet on 23/12/13.
 */
public class About extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        try {
            ((TextView)findViewById(R.id.about_build_number)).setText("" +
                    getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
        } catch (Exception e) { }
    }
}
