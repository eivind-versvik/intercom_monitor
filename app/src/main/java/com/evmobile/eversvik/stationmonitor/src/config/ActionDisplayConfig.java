package com.evmobile.eversvik.stationmonitor.src.config;

/**
 * Created by eversvik on 09.04.2016.
 */
public class ActionDisplayConfig {
    String text;
    int color;

    public ActionDisplayConfig(String text, int color)
    {
        this.text = text;
        this.color = color;
    }

    public String getText() { return text; }
    public int getColor() {
        return color;
    }
}
