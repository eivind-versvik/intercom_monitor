package com.evmobile.eversvik.stationmonitor.src.event.condition;

import android.support.annotation.NonNull;

import com.evmobile.eversvik.stationmonitor.src.config.CondTimerConfig;
import com.evmobile.eversvik.stationmonitor.src.event.util.EventObservable;
import com.evmobile.eversvik.stationmonitor.src.event.EventTimer;
import com.evmobile.eversvik.stationmonitor.src.event.EventTimerResource;

import java.util.Observer;

public class CondTimer implements CondIface {

    private final String TAG = CondTimer.class.toString();
    EventObservable obs = new EventObservable();
    CondTimerConfig config;


    EventTimer timer;
    public CondTimer(@NonNull CondTimerConfig config, @NonNull EventTimerResource timer)
    {
        this.config = config;
        this.timer = timer.getOrCreateTimer(config.getId());

    }

    @Override
    public void addObserver(Observer er) {
        timer.addTimeoutObserver(er);
    }

    @Override
    public void init() {
        // Dont do anything
    }
}
