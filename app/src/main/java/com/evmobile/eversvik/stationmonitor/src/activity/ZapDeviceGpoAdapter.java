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
import com.evmobile.eversvik.stationmonitor.src.device.Gpo;

import java.util.ArrayList;

public class ZapDeviceGpoAdapter extends ArrayAdapter<Gpo> {
    private final Context context;
    private final ArrayList<Gpo> values;
    private final ZapDevice device;

    public ZapDeviceGpoAdapter(Context context, ArrayList<Gpo> values, ZapDevice device) {
        super(context, R.layout.content_zap_device_gpi, values);
        this.context = context;
        this.values = values;
        this.device = device;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.content_zap_device_gpo, parent, false);
        TextView id = (TextView) rowView.findViewById(R.id.label_kid);
        TextView state = (TextView) rowView.findViewById(R.id.label_state);

        Gpo gpo = values.get(position);
        id.setText(gpo.getId());
        state.setText(gpo.getState().toString());

        return rowView;
    }
}