package com.dublet.celicadb2.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;

import com.dublet.celicadb2.R;

/**
 * Created by dublet on 23/12/13.
 */
public class SuspensionLayout extends GridLayout {
    public SuspensionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.suspension_layout, this);
    }
}