package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dublet.celicadb2.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dublet on 27/12/13.
 */
public class WeightPerTorqueView  extends FloatView {
    public WeightPerTorqueView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.weight_per_torque,
                new ArrayList<Integer>(/* Not editable */));
    }

    public void applyPreferences() {
       try {
            int visibility = showImperial() ? View.VISIBLE : View.GONE;
            findViewById(R.id.imperial).setVisibility(visibility);
            findViewById(R.id.imperial_unit).setVisibility(visibility);
            /* findViewById(R.id.imperial_us).setVisibility(visibility);
            findViewById(R.id.imperial_us_unit).setVisibility(visibility);
            findViewById(R.id.imperial_simpsons).setVisibility(visibility);
            findViewById(R.id.imperial_simpsons_unit).setVisibility(visibility); */
        }
        catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
    }
    final private Float lbPerLbFtMultiplier = 2.20462f * 0.737562149f;

    public void setValue(Float kgPerKw) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        ((TextView)findViewById(R.id.metric)).setText("" + nf.format(kgPerKw));
        ((TextView)findViewById(R.id.imperial)). setText("" + nf.format(kgPerKw * lbPerLbFtMultiplier));
       /*((EditText)findViewById(R.id.imperial_simpsons)). setText("" + nf.format(litres / hogsheadMultiplier));*/

    }
}