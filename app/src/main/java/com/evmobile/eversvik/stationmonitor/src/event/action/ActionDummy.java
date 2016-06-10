package com.evmobile.eversvik.stationmonitor.src.event.action;

import com.evmobile.eversvik.stationmonitor.src.util.ELog;

/**
 * Created by eversvik on 01.04.2016.
 * Dummy rpc used instead of Null object.
 */
public class ActionDummy implements EventActionIface {
    private static final String TAG = "ActionDummy";

    @Override
    public void activate() {
        ELog.Debug(TAG, "Error - Activated unknown rpc");
    }

}
