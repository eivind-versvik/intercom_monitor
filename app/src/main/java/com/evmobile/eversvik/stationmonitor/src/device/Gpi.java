package com.evmobile.eversvik.stationmonitor.src.device;

import android.support.annotation.NonNull;

import com.evmobile.eversvik.stationmonitor.src.event.util.EventObservable;
import com.evmobile.eversvik.stationmonitor.src.util.ELog;

import java.util.Observer;

/**
 * Created by eversvik on 13.05.2016.
 */
public class Gpi implements Comparable<Gpi> {
    private static String TAG = "gpi";

    public enum State {
        UNKNOWN, active, inactive
    }

    EventObservable obs = new EventObservable();
    State state = State.UNKNOWN;
    String id;
    ZapDataIface zapData;

    public Gpi(@NonNull ZapDataIface zapData, @NonNull String id) {
        this.zapData = zapData;
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public void setState(State state) {
        if (this.state != state)
            obs.setChanged();
        this.state = state;

        if (obs.hasChanged()) {
            ELog.Debug(TAG, "Gpi " + id + " state changed to " + state.toString());
            obs.notifyObservers(this);
        }
    }

    public void addObserver(Observer observer) {
        this.obs.addObserver(observer);
    }

    public static State textToState(String text) {
        if (text.equals("true"))
            return State.active;
        if (text.equals("false"))
            return State.inactive;
        return State.UNKNOWN;
    }

    @Override
    public int compareTo(Gpi another) {
        return another.getId().compareTo(getId())*-1;
    }
}
