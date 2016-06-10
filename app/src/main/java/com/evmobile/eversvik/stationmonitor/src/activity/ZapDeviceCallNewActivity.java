package com.evmobile.eversvik.stationmonitor.src.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evmobile.eversvik.stationmonitor.R;

public class ZapDeviceCallNewActivity extends AppCompatActivity {

    private final String TAG = "CallActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zap_device_new_call);

        Intent intent = getIntent();
        final String zapid = intent.getStringExtra(ZapService.ZAP_DEVICE_ID);
        assert(zapid != null);

        final Button startCall = (Button) findViewById(R.id.new_call_create);
        assert(startCall != null);

        final EditText username = (EditText) findViewById(R.id.new_call_username);
        assert(username != null);

        startCall.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ZapService.getInstance() == null)
                    Log.d(TAG, "error service null");
                ZapDevice e = ZapService.getInstance().getZapEndpoint(zapid);
                e.getCalling().setupCall(username.getText().toString());

                Intent intent = new Intent(ZapDeviceCallNewActivity.this, ZapDeviceCallActivity.class);
                intent.putExtra(ZapService.ZAP_DEVICE_ID, zapid);
                startActivity(intent);
                Toast toast = Toast.makeText(getApplicationContext(), R.string.create_call, Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        });
    }
}
