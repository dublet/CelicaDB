package com.dublet.celicadb2.widgets;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dublet.celicadb2.R;
import com.dublet.celicadb2.TyreSize;

/**
 * Layout for tyre size.
 *
 * Created by dublet on 05/01/14.
 */
public class TyreSizeView extends ValueView<TyreSize> {
    public TyreSizeView(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.tyre_size_view);

        EditableTextView tyreWidthText = ((EditableTextView)findViewById(R.id.tyre_width));
        EditableTextView profileText = ((EditableTextView)findViewById(R.id.profile));
        EditableTextView alloyText = ((EditableTextView)findViewById(R.id.alloy_diameter));
        EditableTextView loadIndex = ((EditableTextView)findViewById(R.id.load_index));
        EditableTextView speedRating = ((EditableTextView)findViewById(R.id.speed_rating));
        /* Set input modes */
        ((EditText)tyreWidthText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER);
        ((EditText)profileText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER);
        ((EditText)alloyText.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER);
        ((EditText)loadIndex.findViewById(R.id.edit_view)).setInputType(InputType.TYPE_CLASS_NUMBER);
        /* Set callbacks */
        tyreWidthText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) { setTyreWidth(Integer.parseInt(s));}
        });
        profileText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) { setProfile(Integer.parseInt(s));}
        });
        alloyText.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) { setAlloySize(Integer.parseInt(s));}
        });
        loadIndex.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) { setLoadIndex(Integer.parseInt(s));}
        });
        speedRating.addCallback(new BaseTextWatcher() {
            public void textChanged(String s) { setSpeedRating(s);}
        });
    }
    private int getInt(EditableTextView etv) {
        EditText et = (EditText)etv.findViewById(R.id.edit_view);
        try {
            return Integer.parseInt(et.getText().toString());
        } catch (NumberFormatException nfe) { Log.e("NFE", nfe.getMessage()); }
        return -1;
    }

    private int getTyreWidth() {
        return getInt((EditableTextView) findViewById(R.id.tyre_width));
    }

    private int getProfile() {
        return getInt((EditableTextView)findViewById(R.id.profile));
    }

    private int getAlloySize() {
        return getInt((EditableTextView) findViewById(R.id.alloy_diameter));
    }

    private int getLoadIndex() {
        return getInt((EditableTextView) findViewById(R.id.load_index));
    }

    private String getSpeedRating() {
        EditableTextView etv = (EditableTextView) findViewById(R.id.speed_rating);
        EditText et = (EditText)etv.findViewById(R.id.edit_view);
        return et.getText().toString();
    }

    private void setTyreWidth(int tyreWidth) {
        setValue(new TyreSize(tyreWidth, getProfile(), getAlloySize(), getLoadIndex(), getSpeedRating()));
    }

    private void setProfile(int profileWidth) {
        setValue(new TyreSize(getTyreWidth(), profileWidth, getAlloySize(), getLoadIndex(), getSpeedRating()));
    }

    private void setAlloySize(int alloySize) {
        setValue(new TyreSize(getTyreWidth(), getProfile(), alloySize, getLoadIndex(), getSpeedRating()));
    }

    private void setLoadIndex(int loadIndex) {
        setValue(new TyreSize(getTyreWidth(), getProfile(), getAlloySize(), loadIndex, getSpeedRating()));
    }

    private void setSpeedRating(String speedRating) {
        setValue(new TyreSize(getTyreWidth(), getProfile(), getAlloySize(), getLoadIndex(), speedRating));
    }

    public void setValue(TyreSize tyreSize) {
        super.setValue(tyreSize);
        EditableTextView tyreWidthView = (EditableTextView)findViewById(R.id.tyre_width),
                profileView = (EditableTextView)findViewById(R.id.profile),
                alloyView = (EditableTextView)findViewById(R.id.alloy_diameter),
                loadIndex = (EditableTextView)findViewById(R.id.load_index),
                speedRating = (EditableTextView)findViewById(R.id.speed_rating);
        if (tyreSize == null) {
            tyreWidthView.setText("");
            profileView.setText("");
            alloyView.setText("");
            loadIndex.setText("");
            speedRating.setText("");
            loadIndex.setVisibility(View.GONE);
            speedRating.setVisibility(View.GONE);
        } else {
            tyreWidthView.setText("" + tyreSize.tyreWidth);
            profileView.setText("" + tyreSize.profileSize);
            alloyView.setText("" + tyreSize.alloySize);
            loadIndex.setText("" + tyreSize.loadRating);
            speedRating.setText(tyreSize.speedRating);

            loadIndex.setVisibility(tyreSize.loadRating > -1 ? View.VISIBLE : View.GONE);
            speedRating.setVisibility(tyreSize.speedRating != null ? View.VISIBLE : View.GONE);
        }
    }
}