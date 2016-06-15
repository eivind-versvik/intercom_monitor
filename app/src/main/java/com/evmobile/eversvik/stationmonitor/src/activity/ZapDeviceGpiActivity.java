package com.evmobile.eversvik.stationmonitor.src.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.evmobile.eversvik.stationmonitor.R;
import com.evmobile.eversvik.stationmonitor.src.config.EventConfigFactory;
import com.evmobile.eversvik.stationmonitor.src.device.Gpi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

public class ZapDeviceGpiActivity extends AppCompatActivity implements Observer {
    ZapDevice endpoint;
    ArrayList<Gpi> list = new ArrayList<Gpi>();
    ZapDeviceGpiAdapter adapter;

    public void updateAdapter()
    {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void update(Observable observable, Object data) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_device_gpi, menu);
    }

    public enum notify_gpi_type {
        gpiset, gpiclear
    }

    void createNotifyGpi(final Gpi c, final Gpi.State type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(type == Gpi.State.active)
            builder.setTitle(R.string.gpi_create_notify);
        else
            builder.setTitle(R.string.gpi_create_notify_clear);

        View view = getLayoutInflater().inflate(R.layout.dialog_notify_gpi, null);
        builder.setView(view);

        final EditText title = (EditText)view.findViewById(R.id.dialog_notify_title);
        final EditText body_change = (EditText)view.findViewById(R.id.dialog_notify_title_gpi_change);
        assert(body_change != null);
        assert(title != null);
        // final EditText title_clear = (EditText)view.findViewById(R.id.dialog_notify_title_gpo_clear);

        builder.setPositiveButton(R.string.gpi_add_notify, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                endpoint.createRule(EventConfigFactory.createRuleNotifyGpi(c.getId(), type, body_change.getText().toString(), title.getText().toString()));
                ZapService.getInstance().saveConfig();
                updateAdapter();

                Toast toast = Toast.makeText(getApplicationContext(), R.string.added_new_notify, Toast.LENGTH_SHORT);
                toast.show();
            }

        });
        builder.setNegativeButton(R.string.gpi_cancel_add_notify, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Gpi gpi = list.get(info.position);

        switch (item.getItemId()) {
            case R.id.action_notify_gpi_active:
                createNotifyGpi(gpi, Gpi.State.active);
                return true;
            case R.id.action_notify_gpi_inactive:
                createNotifyGpi(gpi, Gpi.State.inactive);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zap_device_gpi);
        Intent intent = getIntent();
        assert(intent.getStringExtra(ZapService.ZAP_DEVICE_ID) != null);

        endpoint = ZapService.getInstance().getZapDeviceOrLast(intent.getStringExtra(ZapService.ZAP_DEVICE_ID));
        assert(endpoint != null);

        final ListView listView = (ListView) findViewById(R.id.listview_devicegpi);
        assert(listView != null);
        registerForContextMenu(listView);

        list = new ArrayList<>(endpoint.getGpis().getGpiList().values());
        Collections.sort(list);
        adapter = new ZapDeviceGpiAdapter(this, list, endpoint);
        listView.setAdapter(adapter);
        for(int i = 0 ; i < list.size(); i++)
        {
            Gpi gpi = list.get(i);
            gpi.addObserver(this);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }

}
