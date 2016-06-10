package com.evmobile.eversvik.stationmonitor.src.event.action;

import android.support.annotation.NonNull;

import com.evmobile.eversvik.stationmonitor.src.device.Gpo;
import com.evmobile.eversvik.stationmonitor.src.config.ActionGpoConfig;

/**
 * Created by eversvik on 31.03.2016.
 * Handles doing GPO operations according to configuration.
 */
public class ActionGpo implements EventActionIface {
    private static final String TAG = "ActionGpo";

    public enum Operation {
        UNKNOWN, SET, CLEAR, TOGGLE
    }

    private Gpo gpo;
    private ActionGpoConfig config;

    public ActionGpo(@NonNull ActionGpoConfig config, @NonNull Gpo gpo)
    {
        this.config = config;
        this.gpo = gpo;
    }

    @Override
    public void activate() {
        switch(config.getOperation())
        {
            case UNKNOWN:
                break;
            case SET:
                gpo.doAction(Gpo.State.set);
                break;
            case CLEAR:
                gpo.doAction(Gpo.State.clear);
                break;
            case TOGGLE:
                if(gpo.getState() == Gpo.State.set)
                    gpo.doAction(Gpo.State.clear);
                else
                    gpo.doAction(Gpo.State.set);
                break;
        }
    }
}
