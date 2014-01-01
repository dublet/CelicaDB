package com.dublet.celicadb2;

import android.util.Log;

/**
 * A helper class to keep track of whether an element is corrected or not.
 *
 * Created by dublet on 30/12/13.
 */
public class CorrectableData<T extends Comparable<T>> {
    public T orig, corrected;
    public String tag;

    public CorrectableData(String tag, T orig) {
        this.tag = tag;
        this.orig = orig;
    }

    public CorrectableData(String tag, T orig, T corrected) {
        this.tag = tag;
        this.orig = orig;
        this.corrected = corrected;
    }

    public T getValue() {
        if (isCorrected())
            return corrected;
        return orig;
    }

    public boolean isCorrected() {
        if (orig == null) {
            Log.e("Uninitialised data? ", tag);
            return false;
        }
        if (corrected == null)
            return false;
        return !orig.equals(corrected);
    }

    public void setCorrected(T corrected) {
        this.corrected = corrected;
    }

    public void removeCorrection() { this.corrected = null; }

    public String toXML(String identifier) {
        if (isCorrected()) {
            return "<correction><element>" + identifier + "</element><orig>" + orig + "</orig><value>" + corrected + "</value></correction>";
        }
        return "";
    }
}
