package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import com.dublet.celicadb2.R;
import com.dublet.celicadb2.Util;

import java.text.NumberFormat;

/**
 * Created by dublet on 01/01/14.
 */
public class CompressionView extends ValueView<Float> {
    public CompressionView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.compression_view);

        EditableTextView compressionRatioText = ((EditableTextView)findViewById(R.id.car_detail_engine_compression_ratio));
        compressionRatioText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) {
                setValue(Util.parseFloat(s));
            }
        });

        ((EditText)compressionRatioText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    public void applyPreferences() {
    }

    public void setValue(Float litres) {
        super.setValue(litres);
        EditableTextView compressionRatioText = ((EditableTextView)findViewById(R.id.car_detail_engine_compression_ratio));

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        compressionRatioText.setText("" + nf.format(litres));
    }
}
