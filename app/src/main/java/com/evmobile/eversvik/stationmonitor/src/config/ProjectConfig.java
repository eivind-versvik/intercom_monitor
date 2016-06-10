package com.evmobile.eversvik.stationmonitor.src.config;

import java.util.ArrayList;

/**
 * Created by eversvik on 09.04.2016.
 */
public class ProjectConfig {
    ArrayList<SubProject> project = new ArrayList<>();

    public ProjectConfig()
    {
        if(project.size() == 0)
            project.add(new SubProject());
    }

    public void addDevice(DeviceConfig device)
    {
        project.get(0).device.add(device);
    }
    public void removeDevice(DeviceConfig device)
    {
        project.get(0).device.remove(device);
    }
    public ArrayList<DeviceConfig> getDevices() { return project.get(0).device; }

    class SubProject {
        // Created to future proofing -  to add possibilities of several projects and the user can switch between them
        ArrayList<DeviceConfig> device;
        String name;
        public SubProject(){
            device = new ArrayList<>();
            name = "default";
        }

    }

}
