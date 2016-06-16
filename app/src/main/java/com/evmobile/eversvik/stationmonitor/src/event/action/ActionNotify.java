package com.evmobile.eversvik.stationmonitor.src.event.action;

import com.evmobile.eversvik.stationmonitor.src.activity.IntercomStartActivity;
import com.evmobile.eversvik.stationmonitor.src.activity.ZapDeviceNotifyActivity;
import com.evmobile.eversvik.stationmonitor.src.activity.ZapNotify;
import com.evmobile.eversvik.stationmonitor.src.activity.ZapService;
import com.evmobile.eversvik.stationmonitor.src.config.ActionNotifyConfig;

/**
 * Created by eversvik on 13.05.2016.
 */
public class ActionNotify implements EventActionIface {
    private ActionNotifyConfig config;
    private String id;

    public ActionNotify(ActionNotifyConfig config, String id) {

        this.config = config;
        this.id = id;
    }

    @Override
    public void activate() {
        // TODO notify id!
        ZapNotify.createNotify(ZapService.getInstance(), config.getTitle(), config.getBody(), IntercomStartActivity.class, id, "1", 0);
    }
}
