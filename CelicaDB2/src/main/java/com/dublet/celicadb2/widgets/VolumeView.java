package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dublet.celicadb2.Converter;
import com.dublet.celicadb2.R;
import com.dublet.celicadb2.Util;

import java.text.NumberFormat;

/**
 * Created by dublet on 23/12/13.
 */
public class VolumeView extends ValueView<Float> {
    public VolumeView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.volume_view);

        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialUsText = ((EditableTextView)findViewById(R.id.imperial_us)),
                imperialUkText = ((EditableTextView)findViewById(R.id.imperial_uk)),
                imperialSimpText = ((EditableTextView)findViewById(R.id.imperial_simpsons));
        metricText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) { setValue(Util.parseFloat(s)); }
        });
        imperialUsText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) { setValue(Converter.galUsToLitres(Util.parseFloat(s))); }
        });
        imperialUkText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) { setValue(Converter.galUKToLitres(Util.parseFloat(s))); }
        });
        imperialSimpText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) { setValue(Converter.hogsheadToLitres(Util.parseFloat(s))); }
        });

        ((EditText)metricText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ((EditText)imperialUsText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ((EditText)imperialUkText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ((EditText)imperialSimpText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    public void applyPreferences() {
        try {
            int visibility = showImperial() ? View.VISIBLE : View.GONE;
            findViewById(R.id.imperial_uk).setVisibility(visibility);
            findViewById(R.id.imperial_uk_unit).setVisibility(visibility);
            findViewById(R.id.imperial_us).setVisibility(visibility);
            findViewById(R.id.imperial_us_unit).setVisibility(visibility);
            findViewById(R.id.imperial_simpsons).setVisibility(visibility);
            findViewById(R.id.imperial_simpsons_unit).setVisibility(visibility);
        }
        catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
    }

    public void setValue(Float litres) {
        super.setValue(litres);
        EditableTextView metricText = ((EditableTextView)findViewById(R.id.metric)),
                imperialUsText = ((EditableTextView)findViewById(R.id.imperial_us)),
                imperialUkText = ((EditableTextView)findViewById(R.id.imperial_uk)),
                imperialSimpText = ((EditableTextView)findViewById(R.id.imperial_simpsons));

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(getMaxDecimalPlaces());
        metricText.setText("" + nf.format(litres));
        imperialUkText.setText("" + nf.format(Converter.litresToGalUK(litres)));
        imperialUsText.setText("" + nf.format(Converter.litresToGalUs(litres)));
        imperialSimpText.setText("" + nf.format(Converter.litresToHogshead(litres)));
    }
}
