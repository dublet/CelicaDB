package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dublet.celicadb2.Preferences;

import java.util.ArrayList;

/**
 * Created by dublet on 27/12/13.
 */
public class ValueView<T> extends LinearLayout  {
    private class ForEachEditableText { public void doSomething(EditableTextView editText) { } }

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

    @SuppressWarnings("unchecked")
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
        doJobForEachEditableTextField(new ForEachEditableText() {
            @Override
            public void doSomething(EditableTextView editText) {
                super.doSomething(editText);
                editText.setEditMode(newEditMode);
            }});
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

    private void doJobForEachEditableTextField(ForEachEditableText feet) {
        if (getChildCount() != 1)
            return;

        ViewGroup layout = (ViewGroup)getChildAt(0);
        if (layout == null)
            return;

        for (int i = 0; i < layout.getChildCount(); ++i) {
            View child = layout.getChildAt(i);
            if (child instanceof EditableTextView)
            {
                EditableTextView editText = (EditableTextView) child;
                feet.doSomething(editText);
            }
        }
    }

    public void setCorrected(final boolean isCorrected) {
        doJobForEachEditableTextField(new ForEachEditableText() {
            @Override
            public void doSomething(EditableTextView editText) {
                editText.setCorrected(isCorrected);
            }
        });
    }
}
