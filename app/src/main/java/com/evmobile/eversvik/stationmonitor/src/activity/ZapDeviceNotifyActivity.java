package com.evmobile.eversvik.stationmonitor.src.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
 * Created by eversvik on 13.05.2016.
 */
public class ZapDeviceNotifyActivity extends AppCompatActivity implements Observer {
    ZapDevice endpoint;
    ArrayList<EventRule> list = new ArrayList<EventRule>();
    ZapDeviceActionAdapter adapter;

    public void updateAdapter() {
        list.clear();
        adapter.clear();

        boolean addHelpText = true;
        for (int i = 0; i < endpoint.getRules().size(); i++) {
            EventRule rule = endpoint.getRules().get(i);
            if(rule.getMainType() == EventRuleConfig.MainType.NOTIFICATION)
            {
                addHelpText = false;
                list.add(rule);
                rule.getDisplay().addDisplayObserver(this);
            }
        }
        final TextView t = (TextView) findViewById(R.id.action_help);
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final EventRule c = list.get(info.position);

        switch (item.getItemId()) {
            case R.id.action_devaction_delete:
                endpoint.removeRule(c);
                updateAdapter();
                ZapService.getInstance().saveConfig();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_device_action, menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zap_device_notify);
        Intent intent = getIntent();
        Log.d("zapactivity", "create zap notify " + intent.getStringExtra(ZapService.ZAP_DEVICE_ID));

        endpoint = ZapService.getInstance().getZapDeviceOrLast(intent.getStringExtra(ZapService.ZAP_DEVICE_ID));
        final ListView listView = (ListView) findViewById(R.id.listview_devicenotify);
        assert(listView != null);

        adapter = new ZapDeviceActionAdapter(this, list);
        listView.setAdapter(adapter);
        updateAdapter();

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                EventRule rule = list.get(position);
                rule.handleClick();
            }

        });
    }
}
