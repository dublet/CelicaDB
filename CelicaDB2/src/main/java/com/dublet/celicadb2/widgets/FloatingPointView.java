package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dublet.celicadb2.R;
import com.dublet.celicadb2.Util;

import java.text.NumberFormat;

/**
 * Created by dublet on 30/12/13.
 */
public class FloatingPointView extends ValueView<Float> {
    private final BaseTextWatcher _floatWatch = new BaseTextWatcher() {
        public void textChanged(String s) { setValue(Util.parseFloat(s.toString())); }
    };
    public FloatingPointView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.number_view);

        EditableTextView floatText = ((EditableTextView)findViewById(R.id.number_value));
        ((EditText)findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        floatText.addCallback(_floatWatch);
    }

    public void applyPreferences() {
        try {
            int visibility = showImperial() ? View.VISIBLE : View.GONE;
            findViewById(R.id.number_value).setVisibility(visibility);
        }
        catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
    }

    public void setValue(Float metres) {
        super.setValue(metres);
        EditableTextView floatText = ((EditableTextView)findViewById(R.id.number_value));

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        floatText.setText("" + nf.format(metres));
    }
}
