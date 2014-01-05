package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.dublet.celicadb2.R;

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
