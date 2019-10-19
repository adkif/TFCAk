package com.example.xtfc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.*;

public class AppConnecte extends AppCompatActivity {
    private BluetoothService bluetoothService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_mob5);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bluetoothService = BluetoothService.getInstance();
        bluetoothService.setHandler(mHandler);
        Button returnBtn = findViewById(R.id.returnBtn);
        final ListView listPaired = findViewById(R.id.listPaired);
        ArrayList<String> listDevice = bluetoothService.listPairedDevice();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listDevice);
        listPaired.setAdapter(adapter);
        listPaired.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Connexion...", Toast.LENGTH_SHORT).show();
                String key = (String) listPaired.getItemAtPosition(position);
                bluetoothService.setKey(key);
                Toast.makeText(getApplicationContext(), "Connecting to "+key, Toast.LENGTH_SHORT).show();
                boolean connection = bluetoothService.connection(key);
                if(connection){
                    Intent gotoAndroid3 = new Intent(AppConnecte.this, Choice.class);
                    startActivity(gotoAndroid3);
                }else{
                    Toast.makeText(getApplicationContext(), "Connexion impossible", Toast.LENGTH_SHORT).show();
                }
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(AppConnecte.this, MainActivity.class);
                startActivity(mIntent);
            }
        });

    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}
