package com.evmobile.eversvik.stationmonitor.src.config;

import com.evmobile.eversvik.stationmonitor.src.device.Gpo;

/**
 * Created by eversvik on 07.04.2016.
 */
public class CondGpoConfig {
    private Gpo.State state = Gpo.State.UNKNOWN;
    private String id = "";

    public CondGpoConfig(String id, Gpo.State state)
    {
        this.state = state;
        this.id = id;
    }

    public Gpo.State getState() {
        return state;
    }

    public String getId() {
        return id;
    }
}
