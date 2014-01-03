package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dublet.celicadb2.Preferences;

import java.util.ArrayList;

/**
 * Created by dublet on 27/12/13.
 */
public class ValueView<T> extends LinearLayout  {
    private class ForEachEditText { public void doSomething(EditText editText) { } }

    final ArrayList<ValueChangeListener> _listeners = new ArrayList<ValueChangeListener>();

    public ValueView(Context context, AttributeSet attrs, int layoutResource) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(layoutResource, this);
        setEditMode(false);
        applyPreferences();
    }

    public  void applyPreferences() {
    }

    public  void setValue(T newValue) {
        for (ValueChangeListener listener : _listeners) {
            listener.valueChanged(newValue);
        }
    }

    public int getMaxDecimalPlaces() {
        int maxDecimalPlaces = 12;
        try {
            SharedPreferences settings = getContext().getSharedPreferences(Preferences.PREFS_NAME, 0);
            maxDecimalPlaces = Integer.parseInt(settings.getString("decimal_places", "12"));
        }
        catch (ClassCastException e) { }
        catch (Exception e) { Log.e("NPE", e.getMessage()); }
        return maxDecimalPlaces;
    }

    public void setEditMode(final boolean newEditMode) {
        doJobForEachEditText(new ForEachEditText() {
            @Override
            public void doSomething(EditText editText) {
                int disabledType = InputType.TYPE_NULL;
                int enabledType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                int newInputType = newEditMode ? enabledType : disabledType;
                editText.setInputType(newInputType);
            }
        });
    }

    public boolean showImperial() {
        try {
            SharedPreferences settings = getContext().getSharedPreferences(Preferences.PREFS_NAME, 0);
            return settings.getBoolean("enable_imperial", false);
        } catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
        return false;
    }

    public void setChangeListener(ValueChangeListener newListener) {
        _listeners.clear();
        _listeners.add(newListener);
    }

    private void doJobForEachEditText(ForEachEditText feet) {
        if (getChildCount() != 1)
            return;

        ViewGroup layout = (ViewGroup)getChildAt(0);
        if (layout == null)
            return;

        for (int i = 0; i < layout.getChildCount(); ++i) {
            View child = layout.getChildAt(i);
            if (child instanceof EditText)
            {
                EditText editText = (EditText) child;
                feet.doSomething(editText);
            }
        }
    }

    public void setCorrected() {
        doJobForEachEditText(new ForEachEditText() {
            @Override
            public void doSomething(EditText editText) {
                editText.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
            }
        });
    }
}