package com.dublet.celicadb2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.dublet.celicadb2.layouts.EconomyLayout;
import com.dublet.celicadb2.layouts.MeasurementsLayout;

/**
 * An activity representing a single Car detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CarListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link CarDetailFragment}.
 */
public class CorrectionActivity extends Activity /*FragmentActivity*/ {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_correction_list);

        // get the listview
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.correction_tree);

        assert(listView != null);

        // setting list adapter
        listView.setAdapter(new CorrectionAdapter(this));
    }
}
