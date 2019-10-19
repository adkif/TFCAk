package com.example.xtfc;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

public class Monitoring extends AppCompatActivity {
    private BluetoothService bluetoothService;
    private TextView voltage;
    private TextView current;
    private TextView power ;
    private TextView energy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_mob2);
        bluetoothService = BluetoothService.getInstance();
        bluetoothService.setHandler(mHandler);
        ImageView config = findViewById(R.id.config);
        voltage = findViewById(R.id.textViewTension);
        current = findViewById(R.id.textViewCurrent);
        power = findViewById(R.id.textViewPower);
        energy = findViewById(R.id.textViewEnergie);

        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoandro3 = new Intent(Monitoring.this, Choice.class);
                startActivity(gotoandro3);
            }
        });
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            String readMessage = null;
            String inputLine = "";
            try {
                readMessage = new String((byte[]) msg.obj, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                for(int i = 0; i < readMessage.length(); i++){
                    char c = readMessage.charAt(i);
                    if(c == '\n' || c == ' ') {
                        if(inputLine.startsWith("#")) voltage.setText(Double.valueOf(inputLine.substring(1))+"V");
                        if(inputLine.startsWith("~")) current.setText(Double.valueOf(inputLine.substring(1))+"A");
                        if(inputLine.startsWith("*")) power.setText(Double.valueOf(inputLine.substring(1))+"W");
                        if(inputLine.startsWith("&")) energy.setText(Double.valueOf(inputLine.substring(1))+"Wh");
                        inputLine = "";
                    }
                    inputLine += c;
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    };
}
