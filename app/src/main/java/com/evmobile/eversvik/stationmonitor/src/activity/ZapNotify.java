package com.evmobile.eversvik.stationmonitor.src.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.evmobile.eversvik.stationmonitor.R;

import java.util.HashMap;

/**
 * Created by eversvik on 17.02.2016.
 * Create notifications
 */
public class ZapNotify {

    private static HashMap<String, Integer> ids = new HashMap<>();
    private static int idcounter = 0;
    public static void createNotify(Service service, String title, String text, Class<?> cls, String zapid, String notifyId, int priority)
    {
        Integer id = ids.get(notifyId);
        if(id == null)
        {
            id = idcounter++;
            ids.put(notifyId, id);
        }

        Log.d("testservice", "creating notifcation");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(service);

        mBuilder.setSmallIcon(R.mipmap.ic_notify)
                .setContentTitle(title)
                .setContentText(text);

        Intent resultIntent = new Intent(service, cls);
        resultIntent.putExtra(ZapService.ZAP_DEVICE_ID, zapid);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(service);

        // stackBuilder.addParentStack(ZapDevice1Activity.class);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);

// mId allows you to update the notification later on.
        mNotificationManager.notify(id, mBuilder.build());
    }
}
