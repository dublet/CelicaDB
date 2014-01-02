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
 * Created by dublet on 23/12/13.
 */
public class SpeedView extends ValueView<Float> {
    private final TextWatcher _metricWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Util.parseFloat(s.toString())); }
    };
    private final TextWatcher _imperialWatch = new BaseTextWatcher() { public void afterTextChanged(Editable s) { setValue(Converter.mphToKmph(Util.parseFloat(s.toString()))); }};
    private final TextWatcher _imperialSimpsonsWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Converter.furlongPerFortnightToKmph(Util.parseFloat(s.toString()))); }
    };

    public SpeedView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.speed_view);

        ((EditText)findViewById(R.id.metric)).addTextChangedListener(_metricWatch);
        ((EditText)findViewById(R.id.imperial)).addTextChangedListener(_imperialWatch);
        ((EditText)findViewById(R.id.imperial_simpsons)).addTextChangedListener(_imperialSimpsonsWatch);
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

    public void setValue(Float kmph) {
        super.setValue(kmph);
        EditText metricText = ((EditText)findViewById(R.id.metric)),
                imperialText = ((EditText)findViewById(R.id.imperial)),
                imperialSimpText = ((EditText)findViewById(R.id.imperial_simpsons));

        /* Remove text watchers to prevent loops */
        metricText.removeTextChangedListener(_metricWatch);
        imperialText.removeTextChangedListener(_imperialWatch);
        imperialSimpText.removeTextChangedListener(_imperialSimpsonsWatch);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        metricText.setText("" + nf.format(kmph));
        imperialText.setText("" + nf.format(Converter.kmphToMph(kmph)));
        imperialSimpText.setText("" + nf.format(Converter.kmphToFurlongPerFortnight(kmph)));

        /* Restore text watchers */
        metricText.addTextChangedListener(_metricWatch);
        imperialText.addTextChangedListener(_imperialWatch);
        imperialSimpText.addTextChangedListener(_imperialSimpsonsWatch);
    }
}
