package com.dublet.celicadb2.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.dublet.celicadb2.R;

import java.util.Arrays;

/**
 * Created by dublet on 23/12/13.
 */
public class DrivetrainLayout extends RelativeLayout {
    public DrivetrainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.drivetrain_layout, this);

         /* Spinners */
        for (Integer i : Arrays.asList(R.id.car_detail_drivetrain_gears, R.id.car_detail_drivetrain_transmission, R.id.car_detail_drivetrain_drive)) {
            findViewById(i).setEnabled(false);
        }
    }
}
