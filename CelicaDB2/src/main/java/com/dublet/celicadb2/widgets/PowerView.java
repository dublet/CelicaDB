package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.dublet.celicadb2.R;

/**
 * Created by dublet on 01/01/14.
 */
public class PowerView extends LinearLayout {
    public PowerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.power_view, this);
    }
}
