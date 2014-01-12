package com.dublet.celicadb2;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * A parent activity that provites an editing mode.
 * Created by dublet on 31/12/13.
 */
public class EditableActivity extends FragmentActivity {
    private static final int MENU_SETTINGS = 1,
            MENU_ABOUT = 2, MENU_FILTER = 3,
            MENU_SORT = 4, MENU_CORRECTIONS = 5, MENU_TYRE_SIZE = 6;

    private boolean mEditMode = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.option_item_settings:
                startActivityForResult(new Intent(this, Preferences.class), MENU_SETTINGS);
                break;
            case R.id.option_item_about:
                startActivityForResult(new Intent(this, About.class),  MENU_ABOUT);
                break;
            case R.id.option_item_corrections:
                startActivityForResult(new Intent(this, CorrectionActivity.class),  MENU_CORRECTIONS);
                break;

            case R.id.option_item_sort:
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Sorting")
                        .setItems(R.array.sortingOptions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentTransaction transaction = getFragmentManager ().beginTransaction();
                                ((CarListFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.car_list)).sortList(which + 1);
                                transaction.commit();

                                SharedPreferences settings = getSharedPreferences(Preferences.PREFS_NAME, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("sorting", "" + which);
                                editor.commit();

                            }
                        });
                builder.create().show();

                break;
            case R.id.option_item_edit:
                if (mEditMode) {
                    CarFactory.getInstance().saveCorrections();
                }
                mEditMode = !mEditMode;

                /* Change UI to/from */
                View listView = findViewById(R.id.car_list);
                if (listView != null)
                    listView.setVisibility(mEditMode ? View.GONE : View.VISIBLE);
                item.setTitle(mEditMode ? R.string.menu_edit_done : R.string.menu_edit);

                try {
                ((CarDetailFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.car_detail_container)).setEditMode(mEditMode);
                } catch (NullPointerException npe) { Log.e("NPE", npe.getMessage()); }

                if (!mEditMode) {
                    /* Hide keyboard */
                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    } catch (NullPointerException e) {}
                }
                break;

            case R.id.option_item_tyre_size:
                startActivityForResult(new Intent(this, TyreSizeActivity.class),  MENU_TYRE_SIZE);
                break;
            /*case R.id.option_item_filter:
                break;*/


        }

        return true;
    }
}
