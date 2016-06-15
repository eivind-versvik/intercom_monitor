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

import java.util.ArrayList;

/**
 * Created by eivind on 15.06.2016.
 */
public class IntercomListFragment extends Fragment {
    private ZapService zapService = null;

    /** Flag indicating whether we have called bind on the service. */
    ArrayList<ZapDevice> list = new ArrayList<ZapDevice>();
    ZapDevice1Adapter zapDeviceAdapter;

    public void updateAdapter()
    {
        Log.d("ZapDevice1Activity", "Updating adapter " + zapService.zapEndpoints.size());
        boolean addHelpText = true;
        if(zapService.zapEndpoints.size() > 0)
            addHelpText = false;
        final TextView t = (TextView) rootView.findViewById(R.id.device_help);
        assert(t != null);

        if(addHelpText)
        {
            t.setVisibility(View.VISIBLE);
        }
        else
        {
            t.setVisibility(View.GONE);
        }
        list.clear();
        list.addAll(zapService.zapEndpoints);
        zapService.replyMessenger = replyMessenger;
        zapDeviceAdapter.notifyDataSetChanged();
    }

    Messenger replyMessenger = new Messenger(new HandlerReplyMsg());
    class HandlerReplyMsg extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle b = msg.getData();
            if(b.getString(ZapService.TCP_CON) != null)
            {
                updateAdapter();
            }
        }
    }

    public IntercomListFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_zap_device, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_create_zap_device) {
            Intent intent = new Intent(getActivity(), ZapDeviceCreateActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_list, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.listview);
        assert(listView != null);
        registerForContextMenu(listView);

        zapDeviceAdapter = new ZapDevice1Adapter(getActivity(), list);
        listView.setAdapter(zapDeviceAdapter);


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
                ZapDevice itemValue = (ZapDevice) listView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ZapDevice2MenuActivity.class);
                intent.putExtra(ZapService.ZAP_DEVICE_ID, itemValue.id);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        Log.d("testservice", "creating context menu");
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_device, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case R.id.action_remove_device:
                    ZapDevice c = list.get(info.position);
                    ZapService.getInstance().deleteZapDevice(c.id);
                    updateAdapter();

                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return false;

    }
}
