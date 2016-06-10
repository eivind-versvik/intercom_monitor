package com.evmobile.eversvik.stationmonitor.src.config;

/**
 * Created by eversvik on 11.04.2016.
 */
public class ActionTimerConfig {
    private String id;
    private int timeout;

    public ActionTimerConfig(String id, int timeout)
    {
        this.id = id;
        this.timeout = timeout;
    }

    public int getTimeout() {return timeout; }
    public String getId() { return id; }
}
