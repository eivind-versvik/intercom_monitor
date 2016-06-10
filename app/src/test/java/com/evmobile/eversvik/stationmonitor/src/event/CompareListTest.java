package com.evmobile.eversvik.stationmonitor.src.event;

import com.evmobile.eversvik.stationmonitor.src.event.util.CompareList;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by eversvik on 08.04.2016.
 * Compare EventRule State
 */
public class CompareListTest {

    @Test
    public void testHasChanged() throws Exception {
        ArrayList<String> testConfig = new ArrayList<String>();
        CompareList<Integer, String> list = new CompareList<>(testConfig);

        assertTrue("Should change first time", list.hasChanged(1, "a"));
        assertFalse("Should not change second time", list.hasChanged(1, "a"));
        assertTrue("Should change, new value", list.hasChanged(1, "b"));
        assertFalse("Should not change second time", list.hasChanged(1, "b"));
    }

    @Test
    public void testEvaluate() throws Exception {
        ArrayList<String> testConfig = new ArrayList<String>();
        CompareList<Integer, String> list = new CompareList<>(testConfig);
        assertFalse("Evaluate when no added config",list.evaluate(1, "a"));

        testConfig.add("a");
        testConfig.add("b");
        list = new CompareList<>(testConfig);

        assertTrue("Evaluate with added correct config",list.evaluate(1, "a"));
        assertFalse("Evaluate second time with added correct config",list.evaluate(1, "a"));
        assertFalse("Evaluate with wrong value",list.evaluate(1, "c"));
        assertTrue("Evaluate with added correct config after wrong value",list.evaluate(1, "a"));
        assertTrue("Evaluate with different id correct value",list.evaluate(2, "a"));
        assertFalse("Evaluate with different id wrong value",list.evaluate(3, "d"));
    }
}