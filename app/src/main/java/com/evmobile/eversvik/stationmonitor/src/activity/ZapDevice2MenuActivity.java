package com.evmobile.eversvik.stationmonitor.src.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.evmobile.eversvik.stationmonitor.R;

public class ZapDevice2MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String zapid = intent.getStringExtra(ZapService.ZAP_DEVICE_ID);
        assert(zapid != null);
        Log.d("testservice", "createmenu " + zapid);

        setContentView(R.layout.activity_zap_device_menu);

        final ListView listView = (ListView) findViewById(R.id.device_menu_list);
        assert(listView != null);

        String[] list = {/*getString(R.string.menu_activity_action), getString(R.string.menu_activity_notify),*/ getString(R.string.menu_activity_gpo), getString(R.string.menu_activity_gpi), getString(R.string.menu_activity_call)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /*if(position == 0)
                {
                    Intent intent = new Intent(ZapDevice2MenuActivity.this, ZapDeviceActionActivity.class);
                    intent.putExtra(ZapService.ZAP_DEVICE_ID, zapid);
                    startActivity(intent);
                }
                else if(position == 1)
                {
                    Intent intent = new Intent(ZapDevice2MenuActivity.this, ZapDeviceNotifyActivity.class);
                    intent.putExtra(ZapService.ZAP_DEVICE_ID, zapid);
                    startActivity(intent);
                }
                else */if(position == 0)
                {
                    Intent intent = new Intent(ZapDevice2MenuActivity.this, ZapDeviceGpoActivity.class);
                    intent.putExtra(ZapService.ZAP_DEVICE_ID, zapid);
                    startActivity(intent);
                }
                else if(position == 1)
                {
                    Intent intent = new Intent(ZapDevice2MenuActivity.this, ZapDeviceGpiActivity.class);
                    intent.putExtra(ZapService.ZAP_DEVICE_ID, zapid);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(ZapDevice2MenuActivity.this, ZapDeviceCallActivity.class);
                    intent.putExtra(ZapService.ZAP_DEVICE_ID, zapid);
                    startActivity(intent);

                }
            }

        });
    }
}
