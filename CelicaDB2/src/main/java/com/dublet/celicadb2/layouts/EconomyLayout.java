package com.dublet.celicadb2.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;

import com.dublet.celicadb2.R;
import com.dublet.celicadb2.widgets.EconomyView;

/**
 * Created by dublet on 23/12/13.
 */
public class EconomyLayout extends GridLayout {
    public EconomyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.economy_layout, this);
    }

    public void applyPreferences() {
        ((EconomyView)findViewById(R.id.car_detail_economy_overall)).applyPreferences();
        ((EconomyView)findViewById(R.id.car_detail_economy_city)).applyPreferences();
        ((EconomyView)findViewById(R.id.car_detail_economy_motorway)).applyPreferences();
    }
}
