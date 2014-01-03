package com.dublet.celicadb2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

/**
 * Activity for corrections.
 */
public class TyreSizeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tyre_size_tree);

        // get the listview
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.tyre_size_tree);

        assert(listView != null);

        SharedPreferences settings = getSharedPreferences(Preferences.PREFS_NAME, 0);
        String selectedId = settings.getString(CarListActivity.SETTING_SELECTION, "");
        Car c = CarFactory.getInstance().getCar(selectedId);
        if (c != null) {
            // setting list adapter
            listView.setAdapter(new TyreSizeAdapter(this, c.tyre_size().getValue()));
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
