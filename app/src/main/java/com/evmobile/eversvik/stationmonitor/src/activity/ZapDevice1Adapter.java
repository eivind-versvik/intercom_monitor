package com.evmobile.eversvik.stationmonitor.src.activity;

/**
 * Created by eversvik on 12.02.2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.evmobile.eversvik.stationmonitor.R;

import java.util.ArrayList;

public class ZapDevice1Adapter extends ArrayAdapter<ZapDevice> {
    private final Context context;
    private final ArrayList<ZapDevice> values;

    public ZapDevice1Adapter(Context context, ArrayList<ZapDevice> values) {
        super(context, R.layout.content_zap_device_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.content_zap_device_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label_display);
        TextView textView2 = (TextView) rowView.findViewById(R.id.label_ipaddress);
        ZapDevice endp = values.get(position);
        textView.setText(endp.id);
        textView2.setText(endp.ipaddress);

        ImageView img = (ImageView) rowView.findViewById(R.id.image_tcpstate);
        if(endp.tcpStatus.equals("connecting") || endp.tcpStatus.equals("disconnected"))
            img.setImageResource(R.drawable.ic_block_24dp);
        else
            img.setImageResource(R.drawable.ic_done_24dp);


        return rowView;
    }
}