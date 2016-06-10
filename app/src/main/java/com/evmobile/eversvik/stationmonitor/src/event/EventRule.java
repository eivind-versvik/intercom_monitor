package com.evmobile.eversvik.stationmonitor.src.event;

import com.evmobile.eversvik.stationmonitor.src.device.DeviceResource;
import com.evmobile.eversvik.stationmonitor.src.config.EventRuleConfig;

import java.util.ArrayList;

/**
 * Created by eversvik on 09.04.2016.
 */
public class EventRule implements EventResource {
    private final String TAG = "EventRule";
    ArrayList<EventItem> item = new ArrayList<>();
    ArrayList<EventTimer> timer = new ArrayList<>();

    EventDisplay display = new EventDisplay();
    EventRuleConfig config;
    public EventRule(EventRuleConfig config, DeviceResource resource)
    {
        this.config = config;
        for(int i = 0; i < config.getItem().size(); i++)
        {
            item.add(new EventItem(config.getItem().get(i), resource, this));
        }

        // After adding all event items, then init condition in case it is already true
        for(int i = 0; i < item.size(); i++)
        {
            EventItem evtitem = item.get(i);
            evtitem.init();
        }
    }

    public EventRuleConfig getConfig() { return this.config; }

    public EventRuleConfig.MainType getMainType() { return config.getMain_type(); }

    public void handleClick()
    {
        display.handleClick();
    }

    public EventDisplay getDisplay() { return this.display; }

    public String getTitle() { return this.config.getTitle(); }

    @Override
    public EventTimer getOrCreateTimer(String id) {
        for(int i = 0; i < timer.size(); i++)
        {
            EventTimer ti = timer.get(i);
            if(ti.getId().equals(id))
                return ti;
        }
        EventTimer ti = new EventTimer(id);
        timer.add(ti);
        return ti;
    }
}
