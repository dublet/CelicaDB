package com.dublet.celicadb2.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.dublet.celicadb2.R;

/**
 * Created by dublet on 23/12/13.
 */
public class PerformanceLayout extends RelativeLayout {
    public PerformanceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.performance_layout, this);
    }
}