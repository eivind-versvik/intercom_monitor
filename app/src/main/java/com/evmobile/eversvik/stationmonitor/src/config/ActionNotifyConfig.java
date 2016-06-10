package com.evmobile.eversvik.stationmonitor.src.config;

/**
 * Created by eversvik on 13.05.2016.
 */
public class ActionNotifyConfig {
    private String title = "";
    private String body = "";

    public ActionNotifyConfig(String title, String body)
    {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
