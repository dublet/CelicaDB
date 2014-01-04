package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.dublet.celicadb2.R;
import com.dublet.celicadb2.Util;

import java.text.NumberFormat;

/**
 * Created by dublet on 01/01/14.
 */
public class CompressionView extends ValueView<Float> {
    private final BaseTextWatcher _compressionRatioWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Util.parseFloat(s.toString())); }
    };
    public CompressionView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.compression_view);

        EditableTextView compressionRatioText = ((EditableTextView)findViewById(R.id.car_detail_engine_compression_ratio));
        compressionRatioText.addCallback(_compressionRatioWatch);
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
        EditableTextView compressionRatioText = ((EditableTextView)findViewById(R.id.car_detail_engine_compression_ratio));

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        compressionRatioText.setText("" + nf.format(litres));
    }
}
