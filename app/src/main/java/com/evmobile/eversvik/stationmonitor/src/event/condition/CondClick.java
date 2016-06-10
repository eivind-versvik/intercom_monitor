package com.evmobile.eversvik.stationmonitor.src.event.condition;

import android.support.annotation.NonNull;

import com.evmobile.eversvik.stationmonitor.src.config.CondClickConfig;
import com.evmobile.eversvik.stationmonitor.src.event.EventDisplay;
import com.evmobile.eversvik.stationmonitor.src.event.util.EventObservable;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by eversvik on 09.04.2016.
 */
public class CondClick implements CondIface {

    EventObservable obs = new EventObservable();
    CondClickConfig config;
    ClickObserver clickObserver = new ClickObserver();

    public CondClick(@NonNull CondClickConfig config, @NonNull EventDisplay display)
    {
        this.config = config;
        display.addClickObserver(clickObserver);
    }

    @Override
    public void addObserver(Observer er) {
        obs.addObserver(er);
    }

    @Override
    public void init() {

    }

    class ClickObserver implements Observer {
        @Override
        public void update(Observable observable, Object data) {
            obs.setChanged();
            obs.notifyObservers();
        }
    }
}

