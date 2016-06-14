package com.evmobile.eversvik.stationmonitor.src.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by eversvik on 13.06.2016.
 * Used to auto-start ZapService
 */
public class ServiceAutoStart extends BroadcastReceiver {
    public void onReceive(Context arg0, Intent arg1)
    {
        Log.d("ServiceAutoStart", "Starting ZapService");
        Intent intent = new Intent(arg0,ZapService.class);
        arg0.startService(intent);
    }
}
