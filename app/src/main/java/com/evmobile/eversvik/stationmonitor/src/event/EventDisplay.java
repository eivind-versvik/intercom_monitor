package com.evmobile.eversvik.stationmonitor.src.event;

import com.evmobile.eversvik.stationmonitor.src.event.util.EventObservable;

import java.util.Observer;

/**
 * Created by eversvik on 09.04.2016.
 */
public class EventDisplay {
    String text;
    int color;
    EventObservable clickObserver = new EventObservable();
    EventObservable displayObserver = new EventObservable();

    public void handleClick()
    {
        clickObserver.setChanged();
        clickObserver.notifyObservers();
    }

    public void setDisplayData(String text, int color)
    {
        this.text = text;
        this.color = color;
        displayObserver.setChanged();
        displayObserver.notifyObservers();
    }

    public int getColor() { return color; }
    public String getText() { return text; }

    public void addClickObserver(Observer observer) {
        clickObserver.addObserver(observer);
    }

    public void removeClickObserver(Observer observer)
    {
        clickObserver.deleteObserver(observer);
    }

    public void addDisplayObserver(Observer observer)
    {
        displayObserver.addObserver(observer);
    }
}
