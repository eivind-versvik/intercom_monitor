package com.evmobile.eversvik.stationmonitor.src.config;

import com.evmobile.eversvik.stationmonitor.src.device.Gpo;

/**
 * Created by eversvik on 07.04.2016.
 */
public class CondGpoConfig {
    private Gpo.State state = Gpo.State.UNKNOWN;
    private String id = "";
    private boolean inverted = false; // Condition is true if Gpi is not similar as state


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
    public void setInverted()
    {
        inverted = true;
    }

    public boolean isInverted() { return inverted; }
}
