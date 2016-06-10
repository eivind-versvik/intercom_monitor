package com.evmobile.eversvik.stationmonitor.src.config;

import com.evmobile.eversvik.stationmonitor.src.event.action.ActionGpo.Operation;

/**
 * Created by eversvik on 07.04.2016.
 */
public class ActionGpoConfig {

    private String id = "";
    private Operation operation = Operation.UNKNOWN;

    public ActionGpoConfig(String id, Operation operation)
    {
        this.operation = operation;
        this.id = id;
    }

    public Operation getOperation() {
        return operation;
    }


    public String getId() {
        return id;
    }


}
