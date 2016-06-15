package com.evmobile.eversvik.stationmonitor.src.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.evmobile.eversvik.stationmonitor.R;
import com.evmobile.eversvik.stationmonitor.src.config.EventConfigFactory;
import com.evmobile.eversvik.stationmonitor.src.config.EventRuleConfig;
import com.evmobile.eversvik.stationmonitor.src.device.Gpo;
import com.evmobile.eversvik.stationmonitor.src.util.TextItemPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

public class ZapDeviceGpoActivity extends AppCompatActivity implements Observer {
    ZapDevice endpoint;
    ArrayList<Gpo> list = new ArrayList<Gpo>();
    ZapDeviceGpoAdapter adapter;

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
        inflater.inflate(R.menu.context_menu_device_gpo, menu);
    }

    public static EventRuleConfig createRuleToggle(String id, String title_clear, String title_set, String title)
    {
        return EventConfigFactory.createToggleGpo(id, title_clear, title_set, title);
    }

    public static EventRuleConfig createRuleSetClearOnTimeout(String id, long timeout, String title_clear, String title_set, String title)
    {
        return EventConfigFactory.createTimeoutGpo(id, (int)timeout, title_clear, title_set, title);
    }

    void createActionGpo(final Gpo c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_action_gpo, null);
        builder.setView(view);

        builder.setTitle(R.string.gpo_create_action);

        final Spinner actiontype = (Spinner) view.findViewById(R.id.dialog_choices);
        {
            actiontype.setPrompt(getString(R.string.gpo_action_prompt));
            ArrayList<TextItemPair<String>> items = new ArrayList<>();
            items.add(new TextItemPair<String>(getString(R.string.dialog_spinner_gpo_toggle), "gpo_toggle"));
            items.add(new TextItemPair<String>(getString(R.string.dialog_spinner_gpo_set_3_timeout), "gpo_set_3_timeout"));
            items.add(new TextItemPair<String>(getString(R.string.dialog_spinner_gpo_set_6_timeout), "gpo_set_6_timeout"));
            ArrayAdapter<TextItemPair<String>> adapter = new ArrayAdapter<TextItemPair<String>>(this, android.R.layout.simple_list_item_1, items);
            actiontype.setAdapter(adapter);
        }

        final EditText title = (EditText)view.findViewById(R.id.dialog_notify_title);
        final EditText title_set = (EditText)view.findViewById(R.id.dialog_notify_title_gpo_set);
        final EditText title_clear = (EditText)view.findViewById(R.id.dialog_notify_title_gpo_clear);

        builder.setPositiveButton(R.string.gpi_add_notify, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String eventtag = ((TextItemPair<String>) actiontype.getSelectedItem()).getItem();
                if(eventtag.equals("gpo_set_3_timeout"))
                {
                    endpoint.createRule(createRuleSetClearOnTimeout(c.getId(), 3000, title_clear.getText().toString(), title_set.getText().toString(), title.getText().toString()));
                }
                if(eventtag.equals("gpo_set_6_timeout"))
                {
                    endpoint.createRule(createRuleSetClearOnTimeout(c.getId(), 6000, title_clear.getText().toString(), title_set.getText().toString(), title.getText().toString()));
                }
                if(eventtag.equals("gpo_toggle"))
                {
                    endpoint.createRule(createRuleToggle(c.getId(), title_clear.getText().toString(), title_set.getText().toString(), title.getText().toString()));
                }

                ZapService.getInstance().saveConfig();
                updateAdapter();

                Toast toast = Toast.makeText(getApplicationContext(), R.string.added_new_action, Toast.LENGTH_SHORT);
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
/*
    void createNotifyGpo(final Gpo c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_notify_gpo, null);
        builder.setView(view);

        builder.setTitle(R.string.gpo_create_notify);
        final EditText title = (EditText)view.findViewById(R.id.dialog_notify_title);
        final EditText title_set = (EditText)view.findViewById(R.id.dialog_notify_title_gpo_change);
        final EditText title_clear = (EditText)view.findViewById(R.id.dialog_notify_title_gpo_clear);

        builder.setPositiveButton(R.string.gpi_add_notify, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                endpoint.createRule(EventConfigFactory.createRuleNotifyGpo(c.getId(), title_clear.getText().toString(), title_set.getText().toString(), title.getText().toString()));
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
*/
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Gpo gpo = list.get(info.position);

        switch (item.getItemId()) {
            case R.id.action_gpo_set:
                gpo.doAction(Gpo.State.set);
                return true;
            case R.id.action_gpo_clear:
                gpo.doAction(Gpo.State.clear);
                return true;
            case R.id.action_create_gpo_action:
                createActionGpo(gpo);
                return true;
            /*case R.id.notify_create_gpo_notify:
                createNotifyGpo(gpo);
                return true;*/
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zap_device_gpo);
        Intent intent = getIntent();
        assert(intent.getStringExtra(ZapService.ZAP_DEVICE_ID) != null);

        endpoint = ZapService.getInstance().getZapDeviceOrLast(intent.getStringExtra(ZapService.ZAP_DEVICE_ID));
        assert(endpoint != null);

        final ListView listView = (ListView) findViewById(R.id.listview_devicegpo);
        assert(listView != null);
        registerForContextMenu(listView);

        list = new ArrayList<>(endpoint.getGpos().getGpoList().values());
        Collections.sort(list);
        adapter = new ZapDeviceGpoAdapter(this, list, endpoint);
        listView.setAdapter(adapter);
        for(int i = 0 ; i < list.size(); i++)
        {
            Gpo gpo = list.get(i);
            gpo.addObserver(this);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }
}
