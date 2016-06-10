package com.evmobile.eversvik.stationmonitor.src.event.util;

import java.util.Observable;

/**
 * Created by eversvik on 09.04.2016.
 * Created so all classes don't have to subclass observable for calling setChanged
 */
public class EventObservable extends Observable {

    public void setChanged()
    {
        super.setChanged();
    }
}
