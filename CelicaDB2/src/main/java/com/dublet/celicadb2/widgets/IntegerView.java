package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dublet.celicadb2.R;

/**
 * Created by dublet on 02/01/14.
 */
public class IntegerView extends ValueView<Integer> {
    private final BaseTextWatcher _intWatch = new BaseTextWatcher() {
        public void textChanged(String s) {  try { setValue(Integer.parseInt(s.toString())); } catch (NumberFormatException e) {  /* Do Nothing */} }
    };
    public IntegerView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.number_view);

        EditableTextView intText = ((EditableTextView)findViewById(R.id.number_value));
        ((EditText)findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER);
        intText.addCallback(_intWatch);
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
        EditableTextView intText = ((EditableTextView)findViewById(R.id.number_value));

        intText.setText("" + metres);
    }
}