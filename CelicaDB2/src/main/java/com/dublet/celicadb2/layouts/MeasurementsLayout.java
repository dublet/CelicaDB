package com.dublet.celicadb2.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;

import com.dublet.celicadb2.R;
import com.dublet.celicadb2.widgets.LengthView;
import com.dublet.celicadb2.widgets.VolumeView;
import com.dublet.celicadb2.widgets.WeightView;

/**
 * Created by dublet on 23/12/13.
 */
public class MeasurementsLayout extends GridLayout {
    public MeasurementsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.measurements_layout, this);
    }
    public void applyPreferences() {
        ((VolumeView)findViewById(R.id.car_detail_measurement_oil_capacity)).applyPreferences();
        ((VolumeView)findViewById(R.id.car_detail_measurement_coolant_capacity)).applyPreferences();
        ((VolumeView)findViewById(R.id.car_detail_measurement_fuel_capacity)).applyPreferences();
        ((LengthView)findViewById(R.id.car_detail_measurement_length)).applyPreferences();
        ((LengthView)findViewById(R.id.car_detail_measurement_width)).applyPreferences();
        ((LengthView)findViewById(R.id.car_detail_measurement_height)).applyPreferences();
        ((LengthView)findViewById(R.id.car_detail_measurement_wheel_base)).applyPreferences();
        ((LengthView)findViewById(R.id.car_detail_measurement_track_width_front)).applyPreferences();
        ((LengthView)findViewById(R.id.car_detail_measurement_track_width_rear)).applyPreferences();
        ((LengthView)findViewById(R.id.car_detail_measurement_length)).applyPreferences();
        ((LengthView)findViewById(R.id.car_detail_measurement_length)).applyPreferences();
        ((WeightView)findViewById(R.id.car_detail_measurement_mass)).applyPreferences();
    }
}