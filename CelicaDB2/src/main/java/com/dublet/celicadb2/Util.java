package com.dublet.celicadb2;

import android.os.Environment;

/**
 * Some helper functions.
 *
 * Created by dublet on 23/12/13.
 */
public class Util {

    public static Float parseFloat(String stringVal) {
        return Float.parseFloat(stringVal.replaceAll(",", ""));
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

}
