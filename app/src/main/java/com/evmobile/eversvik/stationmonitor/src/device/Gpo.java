package com.evmobile.eversvik.stationmonitor.src.device;

import android.support.annotation.NonNull;

import com.evmobile.eversvik.stationmonitor.src.event.util.EventObservable;
import com.evmobile.eversvik.stationmonitor.src.util.ELog;

import java.util.Observer;

/**
 * Created by eversvik on 31.03.2016.
 * Represents a General Purpose Output (GPO) on a ZAP device.
 * Can set and get state using ZAP.
 */
public class Gpo implements Comparable<Gpo> {
    private static String TAG = "gpo";

    public static String RPC_NAME = "gpo_operate";

    @Override
    public int compareTo(Gpo another) {
        return another.getId().compareTo(id)*-1;
    }

    public enum State {
        UNKNOWN, set, clear
    }

    EventObservable obs = new EventObservable();
    State state = State.UNKNOWN;
    String id;
    ZapDataIface zapData;

    public Gpo(@NonNull ZapDataIface zapData, @NonNull String id) {
        this.zapData = zapData;
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public void setState(State state) {
        if (this.state != state)
            obs.setChanged();
        this.state = state;

        if (obs.hasChanged()) {
            ELog.Debug(TAG, "Gpo " + id + " state changed to " + state.toString());
            obs.notifyObservers(this);
        }
    }

    public void addObserver(Observer observer) {
        this.obs.addObserver(observer);
    }

    public void removeObserver(Observer observer)
    {
        this.obs.deleteObserver(observer);
    }

    public static State textToState(String text) {
        if (text.equals("set"))
            return State.set;
        if (text.equals("clear"))
            return State.clear;
        return State.UNKNOWN;
    }

    public void doAction(State action) {
        if (action == State.UNKNOWN) {
            ELog.Debug(TAG, "Gpo Error Cant set gpo to unknown");
            return;
        }

        zapData.sendZapRpc(new ZapRpcGpoOperate(id, action));
    }

    public class ZapRpcGpoOperate implements ZapRpcIface {
        private final String kid;
        private final State operation;

        public ZapRpcGpoOperate(String kid, State operation) {
            this.kid = kid;
            this.operation = operation;
        }

        public String getKid() {
            return kid;
        }

        public State getOperation() {
            return this.operation;
        }

        @Override
        public String toXmlString() {
            return "<rpc><gpo_operate><kid>" + kid + "</kid><operation>" + operation.toString() + "</operation></gpo_operate></rpc>";
        }
    }
}
