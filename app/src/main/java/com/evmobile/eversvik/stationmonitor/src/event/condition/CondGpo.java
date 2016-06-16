package com.evmobile.eversvik.stationmonitor.src.event.condition;

import android.support.annotation.NonNull;

import com.evmobile.eversvik.stationmonitor.src.event.util.CompareItem;
import com.evmobile.eversvik.stationmonitor.src.config.CondGpoConfig;
import com.evmobile.eversvik.stationmonitor.src.event.util.EventObservable;
import com.evmobile.eversvik.stationmonitor.src.device.Gpo;
import com.evmobile.eversvik.stationmonitor.src.device.GpoResource;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by eversvik on 01.04.2016.
 */
public class CondGpo implements CondIface {

    private EventObservable obs = new EventObservable();
    private CondGpoConfig config;
    private Gpo.State state;
    private GpoObserver gpoObserver = new GpoObserver();
    private Gpo gpo;

    public CondGpo(@NonNull CondGpoConfig config, @NonNull GpoResource resource)
    {
        this.config = config;
        this.state = this.config.getState();
        this.gpo = resource.getGpos().createOrGetGpo(config.getId());
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

    @Override
    public void close() {
        this.gpo.removeObserver(gpoObserver);
    }

    boolean isConditionTrue()
    {
        if(config.isInverted())
        {
            return state.compareTo(gpo.getState()) != 0;
        }
        else
            return state.compareTo(gpo.getState()) == 0;
    }

    class GpoObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            if(isConditionTrue())
                obs.setChanged();

            obs.notifyObservers();
        }
    }
}
