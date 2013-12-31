package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dublet.celicadb2.Car;
import com.dublet.celicadb2.Preferences;
import com.dublet.celicadb2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dublet on 27/12/13.
 */
public class CorrectionView  extends LinearLayout {

    public CorrectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.correction_view, this);
    }
}
