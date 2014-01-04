package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dublet.celicadb2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dublet on 03/01/14.
 */
public class EditableTextView extends LinearLayout {
    final List<BaseTextWatcher> _callbacks = new ArrayList<BaseTextWatcher>();

    public EditableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.editable_text_view, this);
        EditText editText = (EditText)findViewById(R.id.edit_view);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE: /* FALLTHROUGH */
                    case EditorInfo.IME_ACTION_NEXT:
                        invokeCallbacks(textView.getText().toString());
                        break;
                }

                return false;
            }
        });
    }

    public void setEditMode(boolean newEditMode) {
        TextView textView = (TextView)findViewById(R.id.text_view);
        EditText editText = (EditText)findViewById(R.id.edit_view);
        if (newEditMode) {
            editText.setText(textView.getText());
        } else {
            textView.setText(editText.getText());
        }
        textView.setVisibility(newEditMode ? View.GONE : View.VISIBLE);
        editText.setVisibility(newEditMode ? View.VISIBLE : View.GONE);
    }

    public void setText(String s) {
        ((TextView)findViewById(R.id.text_view)).setText(s);
        EditText editText = (EditText)findViewById(R.id.edit_view);
        editText.setText(s);
    }

    public void addCallback(BaseTextWatcher btw) {
        _callbacks.add(btw);
    }

    private void invokeCallbacks(String s) {
        for (BaseTextWatcher callback : _callbacks)
            callback.textChanged(s);
    }

    public void removeCallback(BaseTextWatcher btw) {
        _callbacks.remove(btw);
    }

    public void setCorrected(final boolean isCorrected) {
        TextView textView = (TextView)findViewById(R.id.text_view);
        EditText editText = (EditText)findViewById(R.id.edit_view);
        if (isCorrected) {
            textView.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
            editText.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        } else {
            textView.setTypeface(Typeface.DEFAULT, 0);
            editText.setTypeface(Typeface.DEFAULT, 0);
        }
    }
}