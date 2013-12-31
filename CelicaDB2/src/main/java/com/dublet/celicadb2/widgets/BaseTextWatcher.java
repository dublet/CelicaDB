package com.dublet.celicadb2.widgets;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Convenience as we rarely want to implement all three.
 *
 * Created by dublet on 30/12/13.
 */
public class  BaseTextWatcher implements TextWatcher {
    public void afterTextChanged(Editable s) {
        /* Intentionally left blank */
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        /* Intentionally left blank */
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        /* Intentionally left blank */
    }
}
