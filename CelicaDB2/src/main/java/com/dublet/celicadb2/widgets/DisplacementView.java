package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dublet.celicadb2.Converter;
import com.dublet.celicadb2.R;
import com.dublet.celicadb2.Util;

import java.text.NumberFormat;

/**
 * Created by dublet on 01/01/14.
 */
public class DisplacementView extends ValueView<Float> {

    public DisplacementView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.displacement_view);

        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialText = ((EditableTextView)findViewById(R.id.imperial));
        metricText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) {
                setValue(Util.parseFloat(s));
            }
        });
        imperialText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) {
                setValue(Converter.cubicInchToCubicCm(Util.parseFloat(s)));
            }
        });

        ((EditText)metricText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ((EditText)imperialText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
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
