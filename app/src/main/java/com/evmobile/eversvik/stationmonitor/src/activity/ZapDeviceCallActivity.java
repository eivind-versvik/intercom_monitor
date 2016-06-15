package com.evmobile.eversvik.stationmonitor.src.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.evmobile.eversvik.stationmonitor.R;
import com.evmobile.eversvik.stationmonitor.src.device.Call;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ZapDeviceCallActivity extends AppCompatActivity {
    ArrayList<Call> list = new ArrayList<>();
    ZapDeviceCallAdapter deviceCallAdapter;
    ZapDevice endpoint;

    Obs obs = new Obs();
    private final String TAG = "CallActivity";

    public void updateCalls()
    {
        boolean addHelpText = true;
        if(endpoint.getCalling().getCalls().size() > 0)
        {
            addHelpText = false;
        }
        final TextView t = (TextView) findViewById(R.id.call_help);
        assert(t != null);
        if(addHelpText)
        {
            t.setVisibility(View.VISIBLE);
        }
        else
        {
            t.setVisibility(View.GONE);
        }
        deviceCallAdapter.clear();
        deviceCallAdapter.addAll(endpoint.getCalling().getCalls());
        deviceCallAdapter.notifyDataSetChanged();
    }

    class Obs implements Observer
    {
        @Override
        public void update(Observable observable, Object data) {
            updateCalls();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        assert(intent.getStringExtra(ZapService.ZAP_DEVICE_ID) != null);
        Log.d(TAG, "create" + intent.getStringExtra(ZapService.ZAP_DEVICE_ID));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zap_device_call);

        endpoint = ZapService.getInstance().getZapDeviceOrLast(intent.getStringExtra(ZapService.ZAP_DEVICE_ID));
        assert(endpoint != null);

        // Get ListView object from xml
        final ListView listView = (ListView) findViewById(R.id.listview_devicecall);
        assert(listView != null);

        registerForContextMenu(listView);

        deviceCallAdapter = new ZapDeviceCallAdapter(this, list);
        listView.setAdapter(deviceCallAdapter);

        updateCalls();
        endpoint.getCalling().addObserver(obs);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        endpoint.getCalling().removeObserver(obs);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        endpoint.getCalling().addObserver(obs);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        endpoint.getCalling().removeObserver(obs);
    }
/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_zap_device_call, menu);

    }*/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
           /* case R.id.action_hangup:
                Call c = list.get(info.position);
                endpoint.getCalling().hangupCall(c.getCallId());
                return true;*/
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_zap_device_call, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_create_call) {
            Intent intent = new Intent(this, ZapDeviceCallNewActivity.class);
            intent.putExtra(ZapService.ZAP_DEVICE_ID, endpoint.id);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
