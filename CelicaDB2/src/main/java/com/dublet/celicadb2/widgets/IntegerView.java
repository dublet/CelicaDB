package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import com.dublet.celicadb2.R;

/**
 * Created by dublet on 02/01/14.
 */
public class IntegerView extends ValueView<Integer> {
    public IntegerView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.number_view);

        EditableTextView intText = ((EditableTextView)findViewById(R.id.number_value));
        ((EditText)intText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER);
        intText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) {  try { setValue(Integer.parseInt(s)); } catch (NumberFormatException e) {  /* Do Nothing */} }
        });
    }

    public void applyPreferences() {

    }

    public void setValue(Integer metres) {
        super.setValue(metres);
        EditableTextView intText = ((EditableTextView)findViewById(R.id.number_value));

        intText.setText("" + metres);
    }
}