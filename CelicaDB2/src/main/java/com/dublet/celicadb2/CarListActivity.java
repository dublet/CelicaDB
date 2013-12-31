package com.dublet.celicadb2;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * An activity representing a list of Cars. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CarDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link CarListFragment} and the item details
 * (if present) is a {@link CarDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link CarListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class CarListActivity extends EditableActivity
        implements CarListFragment.Callbacks {

    private static final String SETTING_SELECTION = "current_selection",
            SETTING_LAST_OPENED_VERSION = "last_opened_version";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        if (findViewById(R.id.car_detail_container) != null) {
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((CarListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.car_list))
                    .setActivateOnItemClick(true);

            // We also restore the selection in two pane mode.
            try {
                SharedPreferences settings = getSharedPreferences(Preferences.PREFS_NAME, 0);
                String selectedId = settings.getString(SETTING_SELECTION, "");
                if (selectedId != null && !selectedId.isEmpty()) {
                    Bundle arguments = new Bundle();
                    arguments.putString(CarDetailFragment.ARG_ITEM_ID, selectedId);
                    CarDetailFragment fragment = new CarDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.car_detail_container, fragment)
                            .commit();
                }
            }
            catch (Exception e) { Log.e("NPE", e.getMessage()); }
        }


        showWhatsNewDialog();

        // TODO: If exposing deep links into your app, handle intents here.
    }

    private void showWhatsNewDialog() {
        final int version;
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return;
        }

        final SharedPreferences settings = getSharedPreferences(Preferences.PREFS_NAME, 0);
        if (settings.contains(SETTING_LAST_OPENED_VERSION)) {
            int lastOpenedVersion = Integer.parseInt(settings.getString(SETTING_LAST_OPENED_VERSION, ""));
            if (version <= lastOpenedVersion)
                return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.whats_new)
                .setMessage(R.string.changelog)
                .setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        settings.edit().putString(SETTING_LAST_OPENED_VERSION, "" + version).commit();
                    }
                });
        builder.create().show();
    }

    /**
     * Callback method from {@link CarListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(CarDetailFragment.ARG_ITEM_ID, id);
            CarDetailFragment fragment = new CarDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.car_detail_container, fragment)
                    .commit();
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, CarDetailActivity.class);
            detailIntent.putExtra(CarDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }

        SharedPreferences settings = getSharedPreferences(Preferences.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SETTING_SELECTION, id).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.getItem(3).setVisible(mTwoPane);
        return true;
    }

}
