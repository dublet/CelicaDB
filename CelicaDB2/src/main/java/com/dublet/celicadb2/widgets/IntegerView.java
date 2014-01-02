package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dublet.celicadb2.R;

/**
 * Created by dublet on 02/01/14.
 */
public class IntegerView extends ValueView<Integer> {
    private final TextWatcher _intWatch = new BaseTextWatcher() {
        public void afterTextChanged(Editable s) { setValue(Integer.parseInt(s.toString())); }
    };
    public IntegerView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.number_view);

        ((EditText)findViewById(R.id.number_value)).setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void applyPreferences() {
        try {
            int visibility = showImperial() ? View.VISIBLE : View.GONE;
            findViewById(R.id.number_value).setVisibility(visibility);
        }
        catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
    }

    public void setValue(Integer metres) {
        super.setValue(metres);
        EditText intText = ((EditText)findViewById(R.id.number_value));

        intText.removeTextChangedListener(_intWatch);

        intText.setText("" + metres);

        intText.addTextChangedListener(_intWatch);
    }
}