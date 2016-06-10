package com.evmobile.eversvik.stationmonitor.src.config;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by eversvik on 09.04.2016.
 */
public class DeviceConfig {

    private String name = "";
    private String ipaddress = "";
    private String uuid;
    private ArrayList<EventRuleConfig> event = new ArrayList<>();

    public DeviceConfig(String name, String ipaddress)
    {
        this.name = name;
        this.ipaddress = ipaddress;
        uuid = UUID.randomUUID().toString();
    }

    public String getIpaddress()
    {
        return ipaddress;
    }

    public String getName()
    {
        return name;
    }

    public void addRule(EventRuleConfig c)
    {
        event.add(c);
    }

    public void removeRule(EventRuleConfig c)
    {
        event.remove(c);
    }

    public ArrayList<EventRuleConfig> getRules() { return this.event; }
}
