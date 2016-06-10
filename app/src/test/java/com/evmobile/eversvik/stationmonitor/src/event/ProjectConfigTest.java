package com.evmobile.eversvik.stationmonitor.src.event;

import com.evmobile.eversvik.stationmonitor.src.config.DeviceConfig;
import com.evmobile.eversvik.stationmonitor.src.config.ProjectConfig;
import com.evmobile.eversvik.stationmonitor.src.config.EventConfigFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by eversvik on 09.04.2016.
 */
public class ProjectConfigTest {

    @Test
    public void serialize_deserialize()
    {

        ProjectConfig project = new ProjectConfig();
        DeviceConfig device = new DeviceConfig("name1", "10.10.10.10");
        project.addDevice(device);

        device.addRule(EventConfigFactory.createToggleGpo("gpio1", "gpio1 set", "gpio1 cleared", "title"));

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        System.out.println(gson.toJson(project));

        ProjectConfig project2 = gson.fromJson(gson.toJson(project), ProjectConfig.class);
        assertEquals(project2.getDevices().size(), 1);
        System.out.println(gson.toJson(project2));
    }


}
