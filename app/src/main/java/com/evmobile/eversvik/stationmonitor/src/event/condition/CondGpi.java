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
    private CompareItem<String, Gpi.State> state;
    private GpoObserver gpoObserver = new GpoObserver();
    private Gpi gpo;

    public CondGpi(@NonNull CondGpiConfig config, @NonNull GpiResource resource)
    {
        this.config = config;
        this.state = new CompareItem<>(this.config.getState());
        this.gpo = resource.getGpis().createOrGetGpi(config.getId());
        this.gpo.addObserver(gpoObserver);
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
        return state.compare(gpo.getState());
    }

    class GpoObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            if(state.evaluate(gpo.getId(), gpo.getState()))
                obs.setChanged();

            obs.notifyObservers();
        }
    }
}
