package com.evmobile.eversvik.stationmonitor.src.event.condition;

import java.util.Observer;

/**
 * Created by eversvik on 04.04.2016.
 */
public interface CondIface {
    void addObserver(Observer er);
    void init();
    void close();
}
