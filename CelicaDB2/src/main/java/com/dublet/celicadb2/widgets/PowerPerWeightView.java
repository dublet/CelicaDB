package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dublet.celicadb2.R;

import java.text.NumberFormat;

/**
 * Created by dublet on 27/12/13.
 */
public class PowerPerWeightView  extends ValueView<Float> {
    public PowerPerWeightView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.power_per_weight);
    }

    public void applyPreferences() {
       try {
            int visibility = showImperial() ? View.VISIBLE : View.GONE;
            findViewById(R.id.imperial).setVisibility(visibility);
            findViewById(R.id.imperial_unit).setVisibility(visibility);/*
            findViewById(R.id.imperial_simpsons).setVisibility(visibility);
            findViewById(R.id.imperial_simpsons_unit).setVisibility(visibility);*/
        }
        catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
    }
    final private Float bhpPerTonMultiplier = 1.10231f * 1.34048257373f;

    public void setValue(Float kWPerTonne) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        ((TextView)findViewById(R.id.metric)).setText("" + nf.format(kWPerTonne));
        ((TextView)findViewById(R.id.imperial)). setText("" + nf.format(kWPerTonne * bhpPerTonMultiplier));
         /*((EditText)findViewById(R.id.imperial_simpsons)). setText("" + nf.format(litres / hogsheadMultiplier));*/

    }
}
