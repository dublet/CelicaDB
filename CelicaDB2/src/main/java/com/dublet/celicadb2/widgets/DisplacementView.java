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
 * Created by dublet on 01/01/14.
 */
public class DisplacementView extends ValueView<Float> {
    private final BaseTextWatcher _metricWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Util.parseFloat(s)); }
    };
    private final BaseTextWatcher _imperialWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Converter.cubicInchToCubicCm(Util.parseFloat(s))); }
    };

    public DisplacementView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.displacement_view);

        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialText = ((EditableTextView)findViewById(R.id.imperial));
        metricText.addCallback(_metricWatch);
        imperialText.addCallback(_imperialWatch);
    }

    public void applyPreferences() {
        try {
            int visibility = showImperial() ? View.VISIBLE : View.GONE;
            findViewById(R.id.imperial).setVisibility(visibility);
            findViewById(R.id.imperial_unit).setVisibility(visibility);
        }
        catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
    }

    public void setValue(Float cubicCm) {
        super.setValue(cubicCm);
        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialText = ((EditableTextView)findViewById(R.id.imperial));

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        metricText.setText("" + nf.format(cubicCm));
        imperialText.setText("" + nf.format(Converter.cubicCmToCubicInch(cubicCm)));
    }
}
