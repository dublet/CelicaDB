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
public class WeightView extends ValueView<Float> {
    public WeightView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.weight_view);

        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialText = ((EditableTextView)findViewById(R.id.imperial)),
                simpsonsText = ((EditableTextView)findViewById(R.id.imperial_simpsons));

        metricText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) { setValue(Util.parseFloat(s)); }
        });
        imperialText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) { setValue(Converter.poundsToKg(Util.parseFloat(s))); }
        });
        simpsonsText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) { setValue(Converter.bagsOfCementToKg(Util.parseFloat(s))); }
        });

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

    public void setValue(Float kg) {
        super.setValue(kg);
        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialText = ((EditableTextView)findViewById(R.id.imperial)),
                imperialSimpText = ((EditableTextView)findViewById(R.id.imperial_simpsons));

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        metricText.setText("" + nf.format(kg));
        imperialText.setText("" + nf.format(Converter.kgToPounds(kg)));
        imperialSimpText.setText("" + nf.format(Converter.kgToBagsOfCement(kg)));
    }
}
