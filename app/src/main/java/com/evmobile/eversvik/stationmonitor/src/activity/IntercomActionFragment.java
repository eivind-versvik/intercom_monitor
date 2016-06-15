package com.evmobile.eversvik.stationmonitor.src.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.evmobile.eversvik.stationmonitor.R;
import com.evmobile.eversvik.stationmonitor.src.config.EventRuleConfig;
import com.evmobile.eversvik.stationmonitor.src.event.EventRule;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by eivind on 15.06.2016.
 */
public class IntercomActionFragment extends Fragment implements Observer {
    private ZapService zapService = null;


    ArrayList<EventRule> list = new ArrayList<EventRule>();
    ZapDeviceActionAdapter adapter;

    public void updateAdapter() {
        list.clear();
        adapter.clear();
        boolean addHelpText = true;

        for(int endps = 0; endps < zapService.zapEndpoints.size(); endps++)
        {
            ZapDevice endpoint = zapService.zapEndpoints.get(endps);
            for (int i = 0; i < endpoint.getRules().size(); i++) {
                EventRule rule = endpoint.getRules().get(i);
                if(rule.getMainType() == EventRuleConfig.MainType.ACTION)
                {
                    addHelpText = false;
                    list.add(rule);
                    rule.getDisplay().addDisplayObserver(this);
                }
            }
        }

        final TextView t = (TextView) rootView.findViewById(R.id.action_help);
        assert(t != null);
        if(addHelpText)
            t.setVisibility(View.VISIBLE);
        else
            t.setVisibility(View.GONE);
    }

    @Override
    public void update(Observable observable, Object data) {
        updateAdapter();
    }

    public void removeRule(EventRule rule)
    {
        for(int endps = 0; endps < zapService.zapEndpoints.size(); endps++) {
            ZapDevice endpoint = zapService.zapEndpoints.get(endps);
            endpoint.removeRule(rule);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final EventRule c = list.get(info.position);

            switch (item.getItemId()) {
                case R.id.action_devaction_delete:
                    removeRule(c);
                    updateAdapter();
                    ZapService.getInstance().saveConfig();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_device_action, menu);
    }


    public IntercomActionFragment() {
    }

    void startZapService() {
        if(ZapService.getInstance() != null)
            return;
        getActivity().startService(new Intent(getActivity(), ZapService.class));
    }

    Handler timerHandler = new Handler();
    boolean timerStarted = false;
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            Log.d("ZapDevActivity", "timer");
            if(timerStarted) {
                if(ZapService.getInstance() == null)
                {
                    Log.d("ZapDevActivity", "CANT FIND ZAPSERVICE!!");
                }
                else {
                    zapService = ZapService.getInstance();
                    updateAdapter();
                }

                return;
            }
            timerStarted = true;
            timerHandler.postDelayed(this, 10);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_zap_device_action, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.listview_deviceaction);
        assert(listView != null);
        registerForContextMenu(listView);

        adapter = new ZapDeviceActionAdapter(getActivity(), list);
        listView.setAdapter(adapter);


        if(ZapService.getInstance() != null)
        {
            zapService = ZapService.getInstance();
            updateAdapter();
        }
        else
        {
            startZapService();
            timerHandler.postDelayed(timerRunnable, 0);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                EventRule rule = list.get(position);
                rule.handleClick();
            }

        });
        return rootView;
    }
}
