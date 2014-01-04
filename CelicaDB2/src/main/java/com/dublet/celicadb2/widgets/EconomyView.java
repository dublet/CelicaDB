package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.dublet.celicadb2.Converter;
import com.dublet.celicadb2.R;
import com.dublet.celicadb2.Util;

import java.text.NumberFormat;

/**
 * Created by dublet on 22/12/13.
 */
public class EconomyView extends ValueView<Float> {
    private final BaseTextWatcher _metricWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Util.parseFloat(s.toString())); }
    };
    private final BaseTextWatcher _imperialUsWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Converter.mpgUSTolPer100km(Util.parseFloat(s.toString()))); }
    };
    private final BaseTextWatcher _imperialUkWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Converter.mpgUkTolPer100km(Util.parseFloat(s.toString()))); }
    };
    /*private final BaseTextWatcher _imperialSimpsonsWatchWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Converter.mpgUkTolPer100km(Util.parseFloat(s.toString()))); }
    };*/

    public EconomyView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.economy_view);

        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialUsText = ((EditableTextView)findViewById(R.id.imperial_us)),
                imperialUkText = ((EditableTextView)findViewById(R.id.imperial_uk)),
                imperialSimpText = ((EditableTextView)findViewById(R.id.imperial_simpsons));
        metricText.addCallback(_metricWatch);
        imperialUsText.addCallback(_imperialUsWatch);
        imperialUkText.addCallback(_imperialUkWatch);
        //imperialSimpText.addCallback(_imperialSimpsonsWatch);
    }

    public void applyPreferences() {
        try {
            int visibility = showImperial() ? View.VISIBLE : View.GONE;
            findViewById(R.id.imperial_uk).setVisibility(visibility);
            findViewById(R.id.imperial_uk_unit).setVisibility(visibility);
            findViewById(R.id.imperial_us).setVisibility(visibility);
            findViewById(R.id.imperial_us_unit).setVisibility(visibility);
            findViewById(R.id.imperial_simpsons).setVisibility(visibility);
            findViewById(R.id.imperial_simpsons_unit).setVisibility(visibility);
        }
        catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
    }



    public void setValue(Float litresPerHundredKm) {
        super.setValue(litresPerHundredKm);
        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialUkText = ((EditableTextView)findViewById(R.id.imperial_uk)),
                imperialUsText = ((EditableTextView)findViewById(R.id.imperial_us)),
                imperialSimpText = ((EditableTextView)findViewById(R.id.imperial_simpsons));

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        Float mpgUS = Converter.lPer100kmToMpgUS(litresPerHundredKm);
        Float mpgUK = Converter.lPer100kmToMpgUK(litresPerHundredKm);
        Float rph = Converter.lPer100kmToRodsPerHogstead(litresPerHundredKm);
        metricText.setText("" + nf.format(litresPerHundredKm));
        imperialUkText.setText("" + nf.format(mpgUK));
        imperialUsText.setText("" + nf.format(mpgUS));
        imperialSimpText.setText("" + nf.format(rph));
    }
}
