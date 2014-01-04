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
 * Created by dublet on 23/12/13.
 */
public class LengthView extends ValueView<Float> {
    private final BaseTextWatcher _metricWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Util.parseFloat(s.toString())); }
    };
    private final BaseTextWatcher _imperialWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Converter.feetToMetres(Util.parseFloat(s.toString()))); }
    };
    private final BaseTextWatcher _imperialSimpsonsWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Converter.rodsToMetres(Util.parseFloat(s.toString()))); }
    };

    public LengthView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.length_view);

        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialText = ((EditableTextView)findViewById(R.id.imperial)),
                simpsonsText = ((EditableTextView)findViewById(R.id.imperial_simpsons));

        metricText.addCallback(_metricWatch);
        imperialText.addCallback(_imperialWatch);
        simpsonsText.addCallback(_imperialSimpsonsWatch);

        ((EditText)metricText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ((EditText)imperialText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ((EditText)simpsonsText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
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

    public void setValue(Float metres) {
        super.setValue(metres);
        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialText = ((EditableTextView)findViewById(R.id.imperial)),
                imperialSimpText = ((EditableTextView)findViewById(R.id.imperial_simpsons));

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        metricText.setText("" + nf.format(metres));
        imperialText.setText("" + nf.format(Converter.metresToFeet(metres)));
        imperialSimpText.setText("" + nf.format(Converter.metresToRods(metres)));
    }
}
