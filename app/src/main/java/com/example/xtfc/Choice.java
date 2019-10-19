package com.example.xtfc;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

public class Choice extends AppCompatActivity {
    private BluetoothService bluetoothService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_mob3);
        bluetoothService = BluetoothService.getInstance();
        TextView control = findViewById(R.id.control);
        TextView logout = findViewById(R.id.logout);
        TextView status = findViewById(R.id.status);


        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoandro1 = new Intent(Choice.this, PilotageManuel.class);
                startActivity(gotoandro1);
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoandro2 = new Intent(Choice.this, Monitoring.class);
                startActivity(gotoandro2);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothService.close();
                Intent gotoandro2 = new Intent(Choice.this, MainActivity.class);
                startActivity(gotoandro2);
            }
        });
    }
}
