package com.evmobile.eversvik.stationmonitor.src.device;

import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.*;

/**
 * Created by eversvik on 18.04.2016.
 */
public class CallingTest {

    Calling calling;
    MockZap zap;
    ZapSubscription zapsub;
    boolean callingUpdated;
    boolean subscribeSent;
    boolean desubscribeSent;

    @Before
    public void setUp() throws Exception {
        zapsub = null;
        callingUpdated = false;
        subscribeSent = false;
        desubscribeSent = false;
        zap = new MockZap();
        calling = new Calling(zap);

    }

    @Test
    public void zapData() throws Exception
    {
        calling.addObserver(zap);
        assertTrue(subscribeSent);

        zapsub.onDataChanged("<calling></calling>");
        assertEquals(calling.getCalls().size(), 0);

        zapsub.onDataChanged("<calling><call><call_id>5</call_id><username>Abc</username><display>Hello</display></call></calling>");
        assertEquals(calling.getCalls().size(), 1);
        assertTrue(callingUpdated); callingUpdated = false;

        Call call = calling.getCalls().get(0);
        assertEquals(call.getUsername(), "Abc");
        assertEquals(call.getDisplay(), "Hello");

        zapsub.onDataChanged("<calling></calling>");
        assertEquals(calling.getCalls().size(), 0);
        assertTrue(callingUpdated); callingUpdated = false;

        calling.removeObserver(zap);
        assertTrue(desubscribeSent);
    }


    class MockZap implements ZapDataIface, Observer {

        @Override
        public void sendZapRpc(ZapRpcIface text) {

        }

        @Override
        public void addSubscribe(ZapSubscription sub) {
            zapsub = sub;
            subscribeSent = true;
            assertEquals(sub.getPath(), "/state/calling");
        }

        @Override
        public void removeSubscribe(ZapSubscription sub) {
            assertTrue(sub == zapsub);
            desubscribeSent = true;
        }

        @Override
        public void update(Observable observable, Object data) {
            callingUpdated = true;
        }
    }
}