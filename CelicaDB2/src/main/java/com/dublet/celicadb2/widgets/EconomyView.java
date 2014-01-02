package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dublet.celicadb2.Converter;
import com.dublet.celicadb2.R;
import com.dublet.celicadb2.Util;

import java.text.NumberFormat;

/**
 * Created by dublet on 22/12/13.
 */
public class EconomyView extends ValueView<Float> {
    private final TextWatcher _metricWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Util.parseFloat(s.toString())); }
    };
    private final TextWatcher _imperialUsWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Converter.mpgUSTolPer100km(Util.parseFloat(s.toString()))); }
    };
    private final TextWatcher _imperialUkWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Converter.mpgUkTolPer100km(Util.parseFloat(s.toString()))); }
    };

    public EconomyView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.economy_view);

        ((EditText)findViewById(R.id.metric)).addTextChangedListener(_metricWatch);
        ((EditText)findViewById(R.id.imperial_us)).addTextChangedListener(_imperialUsWatch);
        ((EditText)findViewById(R.id.imperial_uk)).addTextChangedListener(_imperialUkWatch);
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
        EditText metricText = ((EditText)findViewById(R.id.metric)),
                imperialUkText = ((EditText)findViewById(R.id.imperial_uk)),
                imperialUsText = ((EditText)findViewById(R.id.imperial_us)),
                imperialSimpText = ((EditText)findViewById(R.id.imperial_simpsons));
        /* Remove text watchers to prevent loops */
        metricText.removeTextChangedListener(_metricWatch);
        imperialUkText.removeTextChangedListener(_imperialUkWatch);
        imperialUsText.removeTextChangedListener(_imperialUsWatch);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        Float mpgUS = Converter.lPer100kmToMpgUS(litresPerHundredKm);
        Float mpgUK = Converter.lPer100kmToMpgUK(litresPerHundredKm);
        Float rph = Converter.lPer100kmToRodsPerHogstead(litresPerHundredKm);
        metricText.setText("" + nf.format(litresPerHundredKm));
        imperialUkText.setText("" + nf.format(mpgUK));
        imperialUsText.setText("" + nf.format(mpgUS));
        imperialSimpText.setText("" + nf.format(rph));

        /* Restore text watchers */
        metricText.addTextChangedListener(_metricWatch);
        imperialUkText.addTextChangedListener(_imperialUkWatch);
        imperialUsText.addTextChangedListener(_imperialUsWatch);
    }
}
