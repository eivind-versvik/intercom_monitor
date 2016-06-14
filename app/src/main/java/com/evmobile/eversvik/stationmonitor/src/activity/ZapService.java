package com.evmobile.eversvik.stationmonitor.src.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.evmobile.eversvik.stationmonitor.src.config.DeviceConfig;
import com.evmobile.eversvik.stationmonitor.src.config.ProjectConfig;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class ZapService extends Service {
    public ZapService() {
    }

    // TODO: Complete actions list
    // TODO: Complete notification list
    // TODO: Tab style actions / intercoms / notifications
    // TODO: Rewrite shown text: title above body/text

    public static String SUB_SPATH = "spath";
    public static String SUB_DATA = "data";
    public static String REPLY_DATA = "reply_data";
    public static String RPC_REFID = "ref_id";
    public static String TCP_CON = "tcp_con";
    public static String ZAP_DEVICE_ID = "id";

    public static ZapService sInstance = null;
    public static boolean isCreated = false;
    public static ZapService getInstance()
    {
        return sInstance;
    }

    ProjectConfig project = new ProjectConfig();


    public void saveConfig() {
        Log.d("ZapService", "saving config");

        FileOutputStream fos = null;
        try {
            Gson g = new Gson();
            fos = openFileOutput("projects.json", Context.MODE_PRIVATE);
            String jsons = g.toJson(project);
            Log.d("save json",jsons);
            fos.write(jsons.getBytes());
            fos.close();
            Log.d("testservice", "File json written");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void readConfig() {
        int size = 0;
        try {
            FileInputStream in = openFileInput("projects.json");
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            Log.d("readjson", sb.toString());

            Gson gson = new Gson();
            project = gson.fromJson(sb.toString(), ProjectConfig.class);
            size = project.getDevices().size();
            Log.d("readjson", "devices " + size);


        } catch (FileNotFoundException e) {
            Log.d("readxml", "Did not find file - expected first bootup when config hasn't been saved");
            //
            // e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < size; i++)
        {
            Log.d("readjson", "Created zap device " + i);
            DeviceConfig config = project.getDevices().get(i);
            ZapDevice d = createZapCon(config);
        }

    }

    public class LocalZapBinder extends Binder {
        ZapService getService() {
            return ZapService.this;
        }
    }

     @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("ZapService", "onStartCommand");
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        if(isCreated)
            return;

        sInstance = this;
        Log.d("ZapService", "onCreate");
        isCreated = true;
        readConfig();
    }

    private final IBinder mBinder = new LocalZapBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public ZapDevice getZapEndpoint(String id)
    {
        for(int i = 0; i < zapEndpoints.size(); i++)
        {
            Log.d("testservice", zapEndpoints.get(i).id + " " + id);
            if(zapEndpoints.get(i).id.equals(id))
                return zapEndpoints.get(i);
        }
        return null;
    }


    /**
     * Creates new zap device if it does not exist. Stores device id/ipaddress in a database.
     * @param ipaddress
     * @param id
     * @return
     */
    public ZapDevice createNewZapDevice(String ipaddress, String id) {
        for(int i = 0; i < zapEndpoints.size(); i++)
        {
            if(zapEndpoints.get(i).id.equals(id))
                return zapEndpoints.get(i);
        }
        DeviceConfig config = new DeviceConfig(id, ipaddress);

        ZapDevice d = createZapCon(config);

        project.addDevice(config);
        saveConfig();
        return d;
    }

    public ZapDevice getZapDevice(String id) {
        for (int i = 0; i < zapEndpoints.size(); i++) {
            if (zapEndpoints.get(i).id.equals(id))
                return zapEndpoints.get(i);
        }
        return null;
    }

    public void deleteZapDevice(String id)
    {
        ZapDevice device = getZapDevice(id);
        if(device != null)
        {
            project.removeDevice(device.config);
            zapEndpoints.remove(device);
            saveConfig();
            device.freeResources();
        }
    }

    ArrayList<ZapDevice> zapEndpoints = new ArrayList<ZapDevice>();
    private ZapDevice createZapCon(DeviceConfig config)
    {
        ZapDevice.OnEndpointUpdated callback = new ZapDevice.OnEndpointUpdated() {

            public void onSubscribeUpdated(String id, String spath, String data) {
                Log.d("testservice", "subupdated " + id + " spath " + spath + " data "+ data);
                Message reply = new Message();
                Bundle b = new Bundle();
                b.putString(ZapService.SUB_DATA, data);
                b.putString(ZapService.SUB_SPATH, spath);
                b.putString(ZapService.ZAP_DEVICE_ID, id);
                reply.setData(b);

                if(replyMessenger == null)
                    return;
                try
                {
                    replyMessenger.send(reply);
                }
                catch(android.os.RemoteException e)
                {
                    e.printStackTrace();
                    Log.d("testservice", e.toString());
                }
            }

            @Override
            public void onTcpUpdated(String id, String status) {
                Log.d("testservice", "tcpupdated " + id + " status " + status);
                Message reply = new Message();
                Bundle b = new Bundle();
                b.putString(ZapService.TCP_CON, status);
                b.putString(ZapService.ZAP_DEVICE_ID, id);
                reply.setData(b);
                if(replyMessenger == null)
                    return;
                try
                {
                    replyMessenger.send(reply);
                }
                catch(android.os.RemoteException e)
                {
                    e.printStackTrace();
                    Log.d("testservice", e.toString());
                }
            }

            @Override
            public void onReplyData(String id, String reply_data) {
                Log.d("testservice", "replydata " + id + " data " + reply_data);
                Message reply = new Message();
                Bundle b = new Bundle();
                b.putString(ZapService.REPLY_DATA, reply_data);
                b.putString(ZapService.ZAP_DEVICE_ID, id);
                reply.setData(b);
                if(replyMessenger == null)
                    return;
                try
                {
                    replyMessenger.send(reply);
                }
                catch(android.os.RemoteException e)
                {
                    e.printStackTrace();
                    Log.d("testservice", e.toString());
                }
            }
        };

        ZapDevice zapEnd = new ZapDevice(callback, config);
        zapEnd.createTcp();
        zapEnd.init();

        zapEndpoints.add(zapEnd);
        return zapEnd;
    }

    Messenger replyMessenger = null;
}
