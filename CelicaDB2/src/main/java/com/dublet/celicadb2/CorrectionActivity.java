package com.dublet.celicadb2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

/**
 * Activity for corrections.
 */
public class CorrectionActivity extends Activity {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.corrections_menu, menu);
        return true;
    }

    @Override
    public void onPause () {
        super.onPause();
        CarFactory.getInstance().saveCorrections();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_item_corrections_submit:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"celicadbcorrections@dublet.org"});
                i.putExtra(Intent.EXTRA_SUBJECT, "[CelicaDB] corrections");
                i.putExtra(Intent.EXTRA_TEXT   , CarFactory.getInstance().getCorrectionsXML());
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(CorrectionActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.option_item_corrections_clear:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(R.string.are_you_sure)
                        .setMessage(R.string.destroy)
                        .setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CarFactory cf = CarFactory.getInstance();
                                cf.clearCorrections();
                                cf.saveCorrections();
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.oh_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /* Do nothing */
                            }
                        }).create().show();
                break;
        }
        return true;
    }
}
