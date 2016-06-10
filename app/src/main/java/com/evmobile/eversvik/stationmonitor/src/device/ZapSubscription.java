package com.evmobile.eversvik.stationmonitor.src.device;

import java.util.UUID;

/**
 * Created by eversvik on 15.04.2016.
 */
public class ZapSubscription  {

    private final String path;
    private final ZapSubscriber subscriber;
    private final String id;
    private String subId;

    public ZapSubscription(String path, ZapSubscriber subscriber) {
        this.path = path;
        this.subscriber = subscriber;
        this.id = UUID.randomUUID().toString();
    }

    public String getPath() {
        return path;
    }

    public String getRefId() { return id; }

    public void onDataChanged(String data) {
        subscriber.onDataChanged(data);
    }

    public String subString() {
        return "<request><ref>" + id + "</ref><rpc><subscribe><change><path>"+ path +"</path><send_now>true</send_now></change></subscribe></rpc></request>";
    }

    public String unsubString() {
        return "<rpc><unsubscribe><subId>" + subId + "</subId></unsubscribe></rpc>";
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public interface ZapSubscriber {
        void onDataChanged(String data);
    }
}
