package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dublet.celicadb2.R;
import com.dublet.celicadb2.Util;

import java.text.NumberFormat;

/**
 * Created by dublet on 01/01/14.
 */
public class CompressionView extends ValueView<Float> {
    private final TextWatcher _compressionRatioWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Util.parseFloat(s.toString())); }
    };
    public CompressionView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.compression_view);
    }

    public void applyPreferences() {
        try {
            int visibility = showImperial() ? View.VISIBLE : View.GONE;
            findViewById(R.id.car_detail_engine_compression_ratio).setVisibility(visibility);
            findViewById(R.id.car_detail_engine_compression_ratio_to1).setVisibility(visibility);
        }
        catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
    }

    public void setValue(Float litres) {
        super.setValue(litres);
        EditText compressionRatioText = ((EditText)findViewById(R.id.car_detail_engine_compression_ratio));

        /* Remove text watchers to prevent loops */
        compressionRatioText.removeTextChangedListener(_compressionRatioWatch);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        compressionRatioText.setText("" + nf.format(litres));

        /* Restore text watchers */
        compressionRatioText.addTextChangedListener(_compressionRatioWatch);
    }
}
