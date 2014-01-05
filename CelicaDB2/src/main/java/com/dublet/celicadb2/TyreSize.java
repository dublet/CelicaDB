package com.dublet.celicadb2;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a tyre size.
 *
 * Created by dublet on 05/01/14.
 */
public class TyreSize implements Comparable<TyreSize> {
    public int tyreWidth;
    public int profileSize;
    public int alloySize;
    public int loadRating;
    public String speedRating;

    public TyreSize() {
        tyreWidth = profileSize = alloySize = loadRating = -1;
        speedRating = null;
    }
    public TyreSize(int width, int profile, int alloy, int loadRating, String speedRating) {
        tyreWidth = width; profileSize = profile; alloySize = alloy;
        this.loadRating = loadRating;
        this.speedRating = speedRating;
    }

    public String toString() {
        String s = tyreWidth + "/" + profileSize + "R" + alloySize;
        if (loadRating > -1 || speedRating != null) {
            s += " ";
            if (loadRating > -1)
                s += loadRating;
            else if (speedRating != null)
                s +=  speedRating;
        }
        return s;
    }

    public static TyreSize fromString(String s) {
        if (s == null)
            return null;
        String pattern = "(\\d{3})[/\\-](\\d{2}) ?([A-QS-Z]?)R ?(\\d{2})( ?(\\d{2})([A-Z]))?";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(s);

        if (m.matches()) {
            int loadRating = -1;
            String speedRating = null;
            if (m.group(3) != null && !m.group(3).equals(""))
                speedRating = m.group(3);
            else if (m.group(5) != null) {
                speedRating = m.group(7);
                loadRating = Integer.parseInt(m.group(6));
            }
            return new TyreSize(
                    Integer.parseInt(m.group(1)),
                    Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(4)),
                    loadRating,
                    speedRating);
        }
        Log.e("Uh-uh", s);
        return null;
    }

    /**
     * Wheel diameter is the alloy size in mm plus twice the sidewall.
     */
    public float totalWheelDiameter() { return Converter.inchToMm((float)alloySize)  + ((tyreWidth * (profileSize / 100f)) * 2); }
    public float circumference() { return (float)(totalWheelDiameter() * Math.PI); }
    public float differenceTo(TyreSize rhs) { return (1f - (circumference() / rhs.circumference())); }

    public int compareTo(TyreSize rhs) {
        if (tyreWidth < rhs.tyreWidth)
            return -1;
        if (tyreWidth > rhs.tyreWidth)
            return 1;
        if (profileSize < rhs.profileSize)
            return -1;
        if (profileSize > rhs.profileSize)
            return 1;
        if (alloySize < rhs.alloySize)
            return -1;
        if (alloySize > rhs.alloySize)
            return 1;
        if (loadRating < rhs.loadRating)
            return -1;
        if (loadRating > rhs.loadRating)
            return 1;
        return speedRating.compareTo(rhs.speedRating);
    }
}
