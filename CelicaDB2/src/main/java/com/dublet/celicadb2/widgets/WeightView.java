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
import java.util.Arrays;

/**
 * Created by dublet on 23/12/13.
 */
public class WeightView extends FloatView {
    private TextWatcher _metricWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Util.parseFloat(s.toString())); }
    };
    private TextWatcher _imperialWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Converter.poundsToKg(Util.parseFloat(s.toString()))); }
    };
    private TextWatcher _imperialSimpsonsWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Converter.bagsOfCementToKg(Util.parseFloat(s.toString()))); }
    };

    public WeightView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.weight_view,
                Arrays.asList(R.id.metric, R.id.imperial, R.id.imperial_simpsons));
    }

    public void applyPreferences() {
        try {
            int visibility = showImperial() ? View.VISIBLE : View.GONE;
            findViewById(R.id.imperial).setVisibility(visibility);
            findViewById(R.id.imperial_unit).setVisibility(visibility);
            findViewById(R.id.imperial_simpsons).setVisibility(visibility);
            findViewById(R.id.imperial_simpsons_unit).setVisibility(visibility);
        }
        catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
    }

    public void setValue(Float kg) {
        super.setValue(kg);
        EditText metricText = ((EditText)findViewById(R.id.metric)),
                imperialText = ((EditText)findViewById(R.id.imperial)),
                imperialSimpText = ((EditText)findViewById(R.id.imperial_simpsons));

        /* Remove text watchers to prevent loops */
        metricText.removeTextChangedListener(_metricWatch);
        imperialText.removeTextChangedListener(_imperialWatch);
        imperialSimpText.removeTextChangedListener(_imperialSimpsonsWatch);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        metricText.setText("" + nf.format(kg));
        imperialText.setText("" + nf.format(Converter.kgToPounds(kg)));
        imperialSimpText.setText("" + nf.format(Converter.kgToBagsOfCement(kg)));

        /* Restore text watchers */
        metricText.addTextChangedListener(_metricWatch);
        imperialText.addTextChangedListener(_imperialWatch);
        imperialSimpText.addTextChangedListener(_imperialSimpsonsWatch);
    }
}
