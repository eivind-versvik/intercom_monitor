package com.evmobile.eversvik.stationmonitor.src.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evmobile.eversvik.stationmonitor.R;
import com.evmobile.eversvik.stationmonitor.src.device.Gpi;

import java.util.ArrayList;

/**
 * Created by eversvik on 13.05.2016.
 */
public class ZapDeviceGpiAdapter extends ArrayAdapter<Gpi> {
    private final Context context;
    private final ArrayList<Gpi> values;
    private final ZapDevice device;

    public ZapDeviceGpiAdapter(Context context, ArrayList<Gpi> values, ZapDevice device) {
        super(context, R.layout.content_zap_device_gpi, values);
        this.context = context;
        this.values = values;
        this.device = device;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.content_zap_device_gpi, parent, false);
        TextView id = (TextView) rowView.findViewById(R.id.label_gpi_kid);
        TextView state = (TextView) rowView.findViewById(R.id.label_gpi_state);

        Gpi gpi = values.get(position);
        id.setText(gpi.getId());
        state.setText(" - " + gpi.getState().toString());

        return rowView;
    }
}