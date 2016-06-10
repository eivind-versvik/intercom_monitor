package com.evmobile.eversvik.stationmonitor.src.event.action;

import android.support.annotation.NonNull;

import com.evmobile.eversvik.stationmonitor.src.config.ActionTimerConfig;
import com.evmobile.eversvik.stationmonitor.src.event.EventTimer;
import com.evmobile.eversvik.stationmonitor.src.event.EventTimerResource;

/**
 * Created by eversvik on 11.04.2016.
 */
public class ActionTimer implements EventActionIface {

    private EventTimer timer;
    private ActionTimerConfig config;

    public ActionTimer(@NonNull ActionTimerConfig config, @NonNull EventTimerResource timer)
    {
        this.config = config;
        this.timer = timer.getOrCreateTimer(config.getId());
    }

    @Override
    public void activate() {
        timer.startTimeout(config.getTimeout());
    }
}

