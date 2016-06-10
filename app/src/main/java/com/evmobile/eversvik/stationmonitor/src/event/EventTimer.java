package com.evmobile.eversvik.stationmonitor.src.event;

import android.os.Handler;

import com.evmobile.eversvik.stationmonitor.src.util.ELog;
import com.evmobile.eversvik.stationmonitor.src.event.util.EventObservable;

import java.util.Observer;

/**
 * Created by eversvik on 11.04.2016.
 */
public class EventTimer {
    private final String TAG = "EventTimer";

    private final String id;
    EventObservable obs = new EventObservable();

    public EventTimer(String id)
    {
        this.id = id;
    }

    public String getId() { return id; }

    public void addTimeoutObserver(Observer observer)
    {
        obs.addObserver(observer);
    }

    public void startTimeout(int timeout)
    {
        Timer t = new Timer((long)timeout, obs);
        t.timerRunnable.run();
        ELog.Debug(TAG, "Starting timeout " + timeout + " " + id);
    }

    class Timer
    {
        long startTime;
        boolean dorun = true;
        EventObservable obs;
        public Timer(long startTime, EventObservable obs)
        {
            this.startTime = startTime;
            this.obs = obs;
        }
        //runs without a timer by reposting this handler at the end of the runnable

        Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {

            @Override
            public void run() {
                if(dorun)
                    timerHandler.postDelayed(this, startTime);
                else
                {
                    obs.setChanged();
                    obs.notifyObservers();
                    ELog.Debug(TAG, "Timer expired " + id + " observers " + obs.countObservers());
                }
                dorun = false;
            }
        };
    }
}
