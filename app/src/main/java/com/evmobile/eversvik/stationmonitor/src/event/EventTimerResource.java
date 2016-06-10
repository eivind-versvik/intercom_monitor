package com.evmobile.eversvik.stationmonitor.src.event;

/**
 * Created by eversvik on 11.04.2016.
 */
public interface EventTimerResource {
    public EventTimer getOrCreateTimer(String id);
}
