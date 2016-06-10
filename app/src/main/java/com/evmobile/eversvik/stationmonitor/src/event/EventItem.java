package com.evmobile.eversvik.stationmonitor.src.event;

import com.evmobile.eversvik.stationmonitor.src.device.DeviceResource;
import com.evmobile.eversvik.stationmonitor.src.event.action.EventActionIface;
import com.evmobile.eversvik.stationmonitor.src.event.condition.CondIface;
import com.evmobile.eversvik.stationmonitor.src.config.ActionConfig;
import com.evmobile.eversvik.stationmonitor.src.config.EventItemConfig;
import com.evmobile.eversvik.stationmonitor.src.event.util.EventObservable;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by eversvik on 01.04.2016.
 */
public class EventItem {
    private ArrayList<EventActionIface> rpc = new ArrayList<>();
    EventObservable obs = new EventObservable();

    CondIface condition;

    public EventItem(EventItemConfig config, DeviceResource resource, EventResource ruleResource)
    {
        condition = EventFactory.createCondFromConfig(config.condition, resource, ruleResource);
        condition.addObserver(new ItemObserver());

        for(int i = 0; i < config.rpc.size(); i++)
        {
            ActionConfig actionConfig = config.rpc.get(i);
            this.rpc.add(EventFactory.createRpcFromConfig(actionConfig, resource, ruleResource));
        }
    }

    public void activateRpcs()
    {
        for(int i = 0; i < rpc.size(); i++)
        {
            EventActionIface rpc = this.rpc.get(i);
            rpc.activate();
        }

        // Must be called after doing rpc's, so the view can update after changes
        obs.setChanged();
        obs.notifyObservers(this);
    }

    public void init()
    {
        condition.init();
    }

    public void addObserver(Observer observer)
    {
        obs.addObserver(observer);
    }

    class ItemObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            activateRpcs();
        }
    }

}
