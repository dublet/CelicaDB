package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dublet.celicadb2.R;

/**
 * Created by dublet on 27/12/13.
 */
public class DateRangeView extends LinearLayout {
    public DateRangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.date_range_view, this);
    }

    public void setValue(Integer fromValue, Integer toValue) {
        ((TextView)findViewById(R.id.car_detail_release_date)).setText("" + fromValue);
        ((TextView)findViewById(R.id.car_detail_decease_date)).setText("" + toValue);
    }

}
