package com.evmobile.eversvik.stationmonitor.src.config;

import com.evmobile.eversvik.stationmonitor.src.device.Gpi;

/**
 * Created by eversvik on 13.05.2016.
 */
public class CondGpiConfig {
    private Gpi.State state = Gpi.State.UNKNOWN;
    private String id = "";

    public CondGpiConfig(String id, Gpi.State state)
    {
        this.state = state;
        this.id = id;
    }

    public Gpi.State getState() {
        return state;
    }

    public String getId() {
        return id;
    }
}
