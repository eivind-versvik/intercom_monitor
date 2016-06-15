package com.evmobile.eversvik.stationmonitor.src.activity;

/**
 * Created by eversvik on 18.02.2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evmobile.eversvik.stationmonitor.R;
import com.evmobile.eversvik.stationmonitor.src.event.EventRule;
import com.evmobile.eversvik.stationmonitor.src.event.EventDisplay;

import java.util.ArrayList;

public class ZapDeviceActionAdapter extends ArrayAdapter<EventRule> {
    private final Context context;
    private final ArrayList<EventRule> values;

    public ZapDeviceActionAdapter(Context context, ArrayList<EventRule> values) {
        super(context, R.layout.content_zap_device_gpi, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.content_zap_device_action, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.label_title);

        TextView state = (TextView) convertView.findViewById(R.id.label_state);
        EventRule item = values.get(position);
        title.setText(item.getTitle());

        EventDisplay display = item.getDisplay();
        convertView.setBackgroundColor(display.getColor());

        title.setText(item.getTitle());
        state.setText(display.getText());

        return convertView;
    }
}