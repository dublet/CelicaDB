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
public class SmallLengthView extends ValueView<Float> {
    private final BaseTextWatcher _metricWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Util.parseFloat(s.toString())); }
    };
    private final BaseTextWatcher _imperialWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Converter.inchToMm(Util.parseFloat(s.toString()))); }
    };
    /*private BaseTextWatcher _imperialSimpsonsWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Converter.rodsToMetres(Util.parseFloat(s.toString()))); }
    };*/

    public SmallLengthView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.small_length_view);

        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialText = ((EditableTextView)findViewById(R.id.imperial)),
                simpsonsText = ((EditableTextView)findViewById(R.id.imperial_simpsons));

        metricText.addCallback(_metricWatch);
        imperialText.addCallback(_imperialWatch);
        //((EditText)findViewById(R.id.imperial_simpsons)).addTextChangedListener(_imperialSimpsonsWatch);

        ((EditText)metricText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ((EditText)imperialText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //((EditText)simpsonsText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    public void applyPreferences() {
        try {
            int visibility = showImperial() ? View.VISIBLE : View.GONE;
            findViewById(R.id.imperial).setVisibility(visibility);
            findViewById(R.id.imperial_unit).setVisibility(visibility);
            /*findViewById(R.id.imperial_simpsons).setVisibility(visibility);
            findViewById(R.id.imperial_simpsons_unit).setVisibility(visibility);*/
        }
        catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
    }

    public void setValue(Float millimetres) {
        super.setValue(millimetres);
        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialText = ((EditableTextView)findViewById(R.id.imperial))/*,
                imperialSimpText = ((EditableTextView)findViewById(R.id.imperial_simpsons))*/;

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        metricText.setText("" + nf.format(millimetres));
        imperialText.setText("" + nf.format(Converter.mmToInch(millimetres)));
        //imperialSimpText.setText("" + nf.format(Converter.metresToRods(millimetres)));
    }
}
