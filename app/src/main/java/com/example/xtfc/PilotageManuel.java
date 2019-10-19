package com.example.xtfc;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import me.tankery.lib.circularseekbar.CircularSeekBar;


public class PilotageManuel extends AppCompatActivity {
    private BluetoothService bluetoothService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_mob1);
        bluetoothService = BluetoothService.getInstance();
        final TextView textProgressAzimute = findTheViewById(R.id.text_progress_azimute);
        final TextView textProgressAltitude = findTheViewById(R.id.text_progress_altitude);
        CircularSeekBar seekBarAzimute = findViewById(R.id.seek_bar_azimute);
        CircularSeekBar seekBarAltitude = findViewById(R.id.seek_bar_altitude);
        final Switch switchMode = findTheViewById(R.id.switchBtn);
        ImageView config = findViewById(R.id.config);

        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchMode.isChecked()){
                    bluetoothService.sendData("4");
                }
            }
        });

        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoandro3 = new Intent(PilotageManuel.this, Choice.class);
                startActivity(gotoandro3);
            }
        });

        seekBarAzimute.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                if(!switchMode.isChecked()){
                    circularSeekBar.isLockEnabled();
                }else{
                    int message = (int) (progress*3.6);
                    Log.d("Main", String.valueOf(message));
                    textProgressAzimute.setText(String.valueOf(message+"°"));
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                Log.d("Main", "onStopTrackingTouch");
                int position = (int) (seekBar.getProgress()*3.6);
                bluetoothService.sendData(String.valueOf(position));
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                Log.d("Main", "onStartTrackingTouch");
                if(!switchMode.isChecked()){
                    Toast.makeText(getApplicationContext(), "Passer en mode manuel", Toast.LENGTH_SHORT).show();
                }else{
                    bluetoothService.sendData("99");
                }
            }
        });
        seekBarAltitude.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                if(!switchMode.isChecked()){
                    circularSeekBar.isLockEnabled();
                }else{
                    int message = (int) (progress*1.8);
                    Log.d("Main", String.valueOf(message));
                    textProgressAltitude.setText(String.valueOf(message+"°"));
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                Log.d("Main", "onStopTrackingTouch");
                int position = (int) (seekBar.getProgress()*1.8);
                bluetoothService.sendData(String.valueOf(position));
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                Log.d("Main", "onStartTrackingTouch");
                if(!switchMode.isChecked()){
                    Toast.makeText(getApplicationContext(), "Passer en mode manuel", Toast.LENGTH_SHORT).show();
                }else{
                    bluetoothService.sendData("100");
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private <T> T findTheViewById(@IdRes int id) {
        return (T) super.findViewById(id);
    }
}
