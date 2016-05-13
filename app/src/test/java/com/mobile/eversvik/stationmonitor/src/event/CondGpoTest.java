package com.mobile.eversvik.stationmonitor.src.event;

import com.mobile.eversvik.stationmonitor.src.device.Gpo;
import com.mobile.eversvik.stationmonitor.src.device.GpoResource;
import com.mobile.eversvik.stationmonitor.src.device.Gpos;
import com.mobile.eversvik.stationmonitor.src.device.ZapDataIface;
import com.mobile.eversvik.stationmonitor.src.device.ZapRpcIface;
import com.mobile.eversvik.stationmonitor.src.device.ZapSubscription;
import com.mobile.eversvik.stationmonitor.src.event.condition.CondGpo;
import com.mobile.eversvik.stationmonitor.src.config.CondGpoConfig;

import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.*;

/**
 * Created by eversvik on 09.04.2016.
 */
public class CondGpoTest {
    MockTest mock;
    String gpio1id = "gpio1";
    Gpo gpo1;

    boolean gpoCreated;
    boolean conditionWasActivated;

    @Before
    public void init() {
        mock = new MockTest();
        gpo1 = new Gpo(mock, gpio1id);
        gpoCreated = false;
        conditionWasActivated = false;
    }
    @Test
    public void testInitCondition() throws Exception {
        CondGpoConfig config = new CondGpoConfig(gpio1id, Gpo.State.set);
        CondGpo conditionGpo = new CondGpo(config, mock);
        gpo1.setState(Gpo.State.set);
        conditionGpo.addObserver(mock);

        assertTrue("Condition was not activated", conditionWasActivated);
        assertTrue("Gpo was not requested", gpoCreated);
    }

    @Test
    public void testCondition() throws Exception {
        CondGpoConfig config = new CondGpoConfig(gpio1id, Gpo.State.set);
        CondGpo conditionGpo = new CondGpo(config, mock);
        conditionGpo.addObserver(mock);

        gpo1.setState(Gpo.State.set);
        assertTrue("Condition was not activated", conditionWasActivated);

        conditionWasActivated = false;
        gpo1.setState(Gpo.State.clear);
        assertFalse("Condition was activated, but shouldn't have been", conditionWasActivated);

        conditionWasActivated = false;
        gpo1.setState(Gpo.State.set);
        assertTrue("Condition has changed - should activate", conditionWasActivated);
    }

    class MockTest implements GpoResource, ZapDataIface, Observer
    {

        @Override
        public void sendZapRpc(ZapRpcIface text) {
            // Not used in this test
        }

        @Override
        public void addSubscribe(ZapSubscription sub) {

        }

        @Override
        public void removeSubscribe(ZapSubscription sub) {

        }

        @Override
        public void update(Observable observable, Object data) {
            conditionWasActivated = true;
        }

        @Override
        public Gpos getGpos() {
            return null;
        }
    }
}