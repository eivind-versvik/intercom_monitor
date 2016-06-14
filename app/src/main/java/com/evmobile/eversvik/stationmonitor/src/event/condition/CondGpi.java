package com.evmobile.eversvik.stationmonitor.src.event.condition;

import android.support.annotation.NonNull;

import com.evmobile.eversvik.stationmonitor.src.config.CondGpiConfig;
import com.evmobile.eversvik.stationmonitor.src.device.Gpi;
import com.evmobile.eversvik.stationmonitor.src.device.GpiResource;
import com.evmobile.eversvik.stationmonitor.src.event.util.CompareItem;
import com.evmobile.eversvik.stationmonitor.src.event.util.EventObservable;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by eversvik on 13.05.2016.
 */
public class CondGpi implements CondIface {
    private EventObservable obs = new EventObservable();
    private CondGpiConfig config;
    private Gpi.State state;
    private Gpi gpi;

    public CondGpi(@NonNull CondGpiConfig config, @NonNull GpiResource resource)
    {
        this.config = config;
        this.state = this.config.getState();
        this.gpi = resource.getGpis().createOrGetGpi(config.getId());
        GpiObserver gpiObserver = new GpiObserver();
        this.gpi.addObserver(gpiObserver);
    }

    @Override
    public void addObserver(Observer er) {
        obs.addObserver(er);
    }

    @Override
    public void init() {
        if(isConditionTrue())
        {
            obs.setChanged();
            obs.notifyObservers();
        }
    }

    boolean isConditionTrue()
    {
        if(config.isInverted())
        {
            return state.compareTo(gpi.getState()) != 0;
        }
        else
            return state.compareTo(gpi.getState()) == 0;
    }

    class GpiObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            if(isConditionTrue())
                obs.setChanged();
            obs.notifyObservers();
        }
    }
}
