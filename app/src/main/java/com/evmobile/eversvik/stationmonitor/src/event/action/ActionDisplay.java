package com.evmobile.eversvik.stationmonitor.src.event.action;

import com.evmobile.eversvik.stationmonitor.src.event.EventDisplay;
import com.evmobile.eversvik.stationmonitor.src.config.ActionDisplayConfig;

/**
 * Created by eversvik on 09.04.2016.
 */
public class ActionDisplay implements EventActionIface {

    ActionDisplayConfig config;
    EventDisplay display;

    public ActionDisplay(ActionDisplayConfig config, EventDisplay display)
    {
        this.config = config;
        this.display = display;
    }

    @Override
    public void activate() {
        display.setDisplayData(config.getText(), config.getColor());
    }
}
