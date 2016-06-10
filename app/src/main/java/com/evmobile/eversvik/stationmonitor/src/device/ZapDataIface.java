package com.evmobile.eversvik.stationmonitor.src.device;

/**
 * Created by eversvik on 31.03.2016.
 */
public interface ZapDataIface {
    void sendZapRpc(ZapRpcIface rpc);

    void addSubscribe(ZapSubscription sub);
    void removeSubscribe(ZapSubscription sub);
}
