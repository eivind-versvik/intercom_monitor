package com.evmobile.eversvik.stationmonitor.src.device;

import android.support.annotation.NonNull;

/**
 * Created by eversvik on 18.04.2016.
 * Represents a single ZAP call
 */
public class Call {
    private final String call_id;
    private String username;
    private String display;
    private String state;

    public Call(@NonNull String call_id) {
        this.call_id = call_id;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCallId() {
        return call_id;
    }
}
