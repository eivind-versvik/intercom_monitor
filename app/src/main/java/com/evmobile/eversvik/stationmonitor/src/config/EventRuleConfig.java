package com.evmobile.eversvik.stationmonitor.src.config;

import java.util.ArrayList;

/**
 * Created by eversvik on 08.04.2016.
 */
public class EventRuleConfig {
    public enum SubType {
        UNKNOWN,
        TIMEOUT_GPO,
        TOGGLE_GPO,
        NOTIFY_GPO,
        NOTIFY_GPI
    }

    public enum MainType {
        UNKNOWN,
        ACTION,
        NOTIFICATION
    }


    public ArrayList<EventItemConfig> getItem() {
        return item;
    }

    ArrayList<EventItemConfig> item = new ArrayList<>();
    SubType sub_type = SubType.UNKNOWN;
    MainType main_type = MainType.UNKNOWN;

    String title = "";
    String id = "";

    public EventRuleConfig(MainType main_type, SubType sub_type, String id, String title)
    {
        this.main_type = main_type;
        this.sub_type = sub_type;
        this.id = id;
        this.title = title;
    }

    public void addItem(EventItemConfig c)
    {
        item.add(c);
    }

    public MainType getMain_type() {
        return main_type;
    }

    public SubType getSub_type() {
        return sub_type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() { return title; }
}
