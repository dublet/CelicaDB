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
public class VolumeView extends FloatView {
    private TextWatcher _metricWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Util.parseFloat(s.toString())); }
    };
    private TextWatcher _imperialUsWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Converter.galUsToLitres(Util.parseFloat(s.toString()))); }
    };
    private TextWatcher _imperialUkWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Converter.galUKToLitres(Util.parseFloat(s.toString()))); }
    };
    private TextWatcher _imperialSimpsonsWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Converter.hogsheadToLitres(Util.parseFloat(s.toString()))); }
    };
    public VolumeView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.volume_view,
                Arrays.asList(R.id.metric, R.id.imperial_uk, R.id.imperial_us, R.id.imperial_simpsons));

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

    public void setValue(Float litres) {
        super.setValue(litres);
        EditText metricText = ((EditText)findViewById(R.id.metric)),
                imperialUsText = ((EditText)findViewById(R.id.imperial_us)),
                imperialUkText = ((EditText)findViewById(R.id.imperial_uk)),
                imperialSimpText = ((EditText)findViewById(R.id.imperial_simpsons));

        /* Remove text watchers to prevent loops */
        metricText.removeTextChangedListener(_metricWatch);
        imperialUsText.removeTextChangedListener(_imperialUsWatch);
        imperialUkText.removeTextChangedListener(_imperialUkWatch);
        imperialSimpText.removeTextChangedListener(_imperialSimpsonsWatch);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        metricText.setText("" + nf.format(litres));
        imperialUkText.setText("" + nf.format(Converter.litresToGalUK(litres)));
        imperialUsText.setText("" + nf.format(Converter.litresToGalUs(litres)));
        imperialSimpText.setText("" + nf.format(Converter.litresToHogshead(litres)));

        /* Restore text watchers */
        metricText.addTextChangedListener(_metricWatch);
        imperialUsText.addTextChangedListener(_imperialUsWatch);
        imperialUkText.addTextChangedListener(_imperialUkWatch);
        imperialSimpText.addTextChangedListener(_imperialSimpsonsWatch);
    }
}
