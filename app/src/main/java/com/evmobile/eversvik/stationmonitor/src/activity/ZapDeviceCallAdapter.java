package com.evmobile.eversvik.stationmonitor.src.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.evmobile.eversvik.stationmonitor.R;
import com.evmobile.eversvik.stationmonitor.src.device.Call;

import java.util.ArrayList;

/**
 * Created by eversvik on 13.02.2016.
 */
public class ZapDeviceCallAdapter extends ArrayAdapter<Call> {
    private final Context context;
    private final ArrayList<Call> values;

    public ZapDeviceCallAdapter(Context context, ArrayList<Call> values) {
        super(context, R.layout.content_zap_device_call, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.content_zap_device_call, parent, false);
        TextView usertextView = (TextView) rowView.findViewById(R.id.label_username);
        TextView displaytextView = (TextView) rowView.findViewById(R.id.label_display);

        Call call = values.get(position);
        usertextView.setText(call.getUsername());
        displaytextView.setText(call.getDisplay());

        ImageView img = (ImageView) rowView.findViewById(R.id.image_callstate);

        if(call.getState().equals("alerting") || call.getState().equals("trying"))
            img.setImageResource(R.drawable.ic_ring_volume_24dp_yellow);
        else if(call.getState().equals("call_ended"))
            img.setImageResource(R.drawable.ic_call_end_24dp);
        else
            img.setImageResource(R.drawable.ic_call_24dp_geen);

        return rowView;
    }
}