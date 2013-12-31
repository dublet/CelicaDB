package com.dublet.celicadb2;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by dublet on 23/12/13.
 */
public class Util {

    public static Float parseFloat(String stringVal) {
        return Float.parseFloat(stringVal.replaceAll(",", ""));
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
