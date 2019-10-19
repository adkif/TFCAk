package com.example.xtfc;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothService bluetoothService;
    private ImageView state;
    private TextView etat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bluetoothService = BluetoothService.getInstance();
        Button connect = findViewById(R.id.connect);
        ImageView activer = findViewById(R.id.buttonActiver);
        state = findViewById(R.id.vue);
        etat = findViewById(R.id.etat);


        activer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothAdapter == null){
                    Toast.makeText(getApplicationContext(), "Bluetooth non supporter", Toast.LENGTH_SHORT).show();
                }else{
                    if(bluetoothAdapter.isEnabled()){
                        Toast.makeText(getApplicationContext(), "Le bluetooth est deja active", Toast.LENGTH_SHORT).show();
                        etat.setText("Bluetooth actif");
                    }else {
                        bluetoothService.enableBluetooth();
                        etat.setText("Bluetooth actif");
                        state.setImageResource(R.drawable.on);
                    }
                }
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothAdapter == null){
                    Toast.makeText(getApplicationContext(), "Bluetooth non supporter", Toast.LENGTH_SHORT).show();
                }else{
                    if(!bluetoothAdapter.isEnabled()){
                        Toast.makeText(getApplicationContext(), "Activer d'abord le bluetooth", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent gotoAndro5 = new Intent(MainActivity.this, AppConnecte.class);
                        startActivity(gotoAndro5);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "Bluetooth non supporter", Toast.LENGTH_SHORT).show();
        }else{
            if(bluetoothAdapter.isEnabled()){
                Toast.makeText(getApplicationContext(), "Le bluetooth est deja active", Toast.LENGTH_SHORT).show();
                state.setImageResource(R.drawable.on);
                etat.setText("Bluetooth actif");
            }else {
                etat.setText("Activer le bluetooth sur cet\nappareil");
                state.setImageResource(R.drawable.off);
            }
        }
    }
}
