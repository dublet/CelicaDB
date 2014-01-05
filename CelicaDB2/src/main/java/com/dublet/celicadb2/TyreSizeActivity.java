package com.dublet.celicadb2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

/**
 * Activity for corrections.
 */
public class TyreSizeActivity extends Activity {
    private final String SETTING_TYRE_DISCLAIMER = "tyre_disclaimer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tyre_size_tree);

        // get the listview
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.tyre_size_tree);

        assert(listView != null);

        final SharedPreferences settings = getSharedPreferences(Preferences.PREFS_NAME, 0);

        String acceptedDisclaimer = settings.getString(SETTING_TYRE_DISCLAIMER, "false");
        if (acceptedDisclaimer.equals("true")) {
            findViewById(R.id.disclaimer).setVisibility(View.GONE);
        } else {
            Button disclaimerButton = (Button)findViewById(R.id.disclaimer_accepted);
            disclaimerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settings.edit().putString(SETTING_TYRE_DISCLAIMER, "true").commit();
                    view.findViewById(R.id.disclaimer).setVisibility(View.GONE);
                }
            });
        }

        String selectedId = settings.getString(CarListActivity.SETTING_SELECTION, "");
        Car c = CarFactory.getInstance().getCar(selectedId);
        if (c != null) {
            // setting list adapter
            try {
                listView.setAdapter(new TyreSizeAdapter(this, c.tyre_size().orig));
            } catch (NullPointerException e) { }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onPause () {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }
}
