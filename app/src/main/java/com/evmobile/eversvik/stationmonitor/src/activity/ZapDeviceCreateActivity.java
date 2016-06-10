package com.evmobile.eversvik.stationmonitor.src.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evmobile.eversvik.stationmonitor.R;

public class ZapDeviceCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zap_device_create);

        final EditText idt = (EditText)findViewById(R.id.edit_id);
        final EditText ipaddrt = (EditText)findViewById(R.id.edit_ip_address);
        final Button but = (Button)findViewById(R.id.button_add_device);
        assert(but != null);
        assert(ipaddrt != null);
        assert(idt != null);

        but.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(ipaddrt.getText().toString().length() > 5 && idt.getText().toString().length() > 1)
                {
                    ZapService.getInstance().createNewZapDevice(ipaddrt.getText().toString(), idt.getText().toString());
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.add_new_device, Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            }
        });

    }
}
