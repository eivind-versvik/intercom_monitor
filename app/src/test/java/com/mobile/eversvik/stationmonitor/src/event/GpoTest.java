package com.mobile.eversvik.stationmonitor.src.event;

import com.mobile.eversvik.stationmonitor.src.device.Gpo;
import com.mobile.eversvik.stationmonitor.src.device.ZapDataIface;
import com.mobile.eversvik.stationmonitor.src.device.ZapRpcIface;
import com.mobile.eversvik.stationmonitor.src.device.ZapSubscription;

import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.*;

/**
 * Created by eversvik on 09.04.2016.
 */
public class GpoTest {

    MockTest mock;
    Gpo gpo;
    boolean observedWasCalled;
    boolean rpcused;

    @Before
    public void init() {
        mock = new MockTest();
        gpo = new Gpo(mock, "gpio1");
        observedWasCalled = false;
        rpcused = false;
    }

    @Test
    public void testSetState() throws Exception {
        gpo.addObserver(mock);
        gpo.setState(Gpo.State.clear);
        assertTrue("Updated called", observedWasCalled);

        observedWasCalled = false;
        gpo.setState(Gpo.State.clear);
        assertFalse("Updated called when no change", observedWasCalled);
    }

    @Test
    public void testTextToState() throws Exception {
        assertEquals(Gpo.textToState("set"), Gpo.State.set);
        assertEquals(Gpo.textToState("clear"), Gpo.State.clear);
        assertEquals(Gpo.textToState("UNKNOWN"), Gpo.State.UNKNOWN);
    }

    @Test
    public void testGpoAction() throws Exception {
        gpo.doAction(Gpo.State.set);
        assertTrue(rpcused);

        rpcused = false;
        gpo.doAction(Gpo.State.UNKNOWN);
        assertFalse(rpcused);
    }

    class MockTest implements ZapDataIface, Observer {

        @Override
        public void sendZapRpc(ZapRpcIface rpc) {
            Gpo.ZapRpcGpoOperate operate = (Gpo.ZapRpcGpoOperate)rpc;
            rpcused = true;
            assertTrue(operate.getKid().equals("gpio1"));
            assertEquals(operate.getOperation(), Gpo.State.set);
        }

        @Override
        public void addSubscribe(ZapSubscription sub) {

        }

        @Override
        public void removeSubscribe(ZapSubscription sub) {

        }

        @Override
        public void update(Observable observable, Object data) {
            observedWasCalled = true;
        }
    }
}