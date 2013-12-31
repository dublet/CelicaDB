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
 * Created by dublet on 30/12/13.
 */
public class NumberView extends FloatView {
    public NumberView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.number_view,
                Arrays.asList(R.id.float_value));
    }

    public void applyPreferences() {
        try {
            int visibility = showImperial() ? View.VISIBLE : View.GONE;
            findViewById(R.id.float_value).setVisibility(visibility);
        }
        catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
    }

    public void setValue(Float metres) {
        super.setValue(metres);
        EditText metricText = ((EditText)findViewById(R.id.float_value));

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        metricText.setText("" + nf.format(metres));
    }
}
