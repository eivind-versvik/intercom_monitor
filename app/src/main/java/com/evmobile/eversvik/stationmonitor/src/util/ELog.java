package com.evmobile.eversvik.stationmonitor.src.util;

import android.util.Log;

/**
 * Created by eversvik on 01.04.2016.
 * Android's Log does not work during unit testing
 * Created this class to be able to redirect logging in that case
 */
public class ELog {
    public static void Debug(String tag, String text)
    {
        Log.d(tag,text);
    }
}
