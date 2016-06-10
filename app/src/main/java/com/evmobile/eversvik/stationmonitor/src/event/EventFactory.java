package com.evmobile.eversvik.stationmonitor.src.event;

import com.evmobile.eversvik.stationmonitor.src.device.DeviceResource;
import com.evmobile.eversvik.stationmonitor.src.event.action.ActionDisplay;
import com.evmobile.eversvik.stationmonitor.src.event.action.ActionDummy;
import com.evmobile.eversvik.stationmonitor.src.event.action.ActionGpo;
import com.evmobile.eversvik.stationmonitor.src.event.action.ActionNotify;
import com.evmobile.eversvik.stationmonitor.src.event.action.ActionTimer;
import com.evmobile.eversvik.stationmonitor.src.event.action.EventActionIface;
import com.evmobile.eversvik.stationmonitor.src.event.condition.CondClick;
import com.evmobile.eversvik.stationmonitor.src.event.condition.CondDummy;
import com.evmobile.eversvik.stationmonitor.src.event.condition.CondGpi;
import com.evmobile.eversvik.stationmonitor.src.event.condition.CondGpo;
import com.evmobile.eversvik.stationmonitor.src.event.condition.CondIface;
import com.evmobile.eversvik.stationmonitor.src.event.condition.CondTimer;
import com.evmobile.eversvik.stationmonitor.src.config.ActionConfig;
import com.evmobile.eversvik.stationmonitor.src.config.CondConfig;

public class EventFactory {


    public static CondIface createCondFromConfig(CondConfig config, DeviceResource resource, EventResource ruleResource)
    {
        if(config.gpo != null)
        {
            return new CondGpo(config.gpo, resource);
        }
        if(config.click != null)
        {
            return new CondClick(config.click, ruleResource.getDisplay());
        }
        if(config.timer != null)
        {
            return new CondTimer(config.timer, ruleResource);
        }
        if(config.gpi != null)
        {
            return new CondGpi(config.gpi, resource);
        }

        return new CondDummy();

    }

    public static EventActionIface createRpcFromConfig(ActionConfig config, DeviceResource resource, EventResource ruleResource)
    {
        if(config.gpo != null)
        {
            return new ActionGpo(config.gpo, resource.getGpos().createOrGetGpo(config.gpo.getId()));
        }
        if(config.display != null)
        {
            return new ActionDisplay(config.display, ruleResource.getDisplay());
        }
        if(config.timer != null)
        {
            return new ActionTimer(config.timer, ruleResource);
        }
        if(config.notify != null)
        {
            return new ActionNotify(config.notify, resource.getId());
        }

        return new ActionDummy();
    }
}
