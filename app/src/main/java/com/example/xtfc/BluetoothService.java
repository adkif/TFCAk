package com.example.xtfc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.*;
import android.util.Log;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class BluetoothService {
    private static BluetoothService bluetoothService;
    private static final String TAG = "BluetoothService";
    private String key;
    private HashMap<String, String> mapList;
    // handler that gets info from Bluetooth service
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter() ;
    private ConnectedThread clientThread;
    private final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String readMessage = null;
            try {
                readMessage = new String((byte[]) msg.obj, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    // Defines several constants used when transmitting messages between the
    // service and the UI.
    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
    }

    private BluetoothService() {
    }

    public static BluetoothService getInstance(){
        if(bluetoothService == null){
            bluetoothService = new BluetoothService();
        }
        return  bluetoothService;
    }

    public void setHandler(Handler mhandler){
        handler = mhandler;
    }


    public void enableBluetooth(){
        if (!bluetoothAdapter.isEnabled()){
            bluetoothAdapter.enable();
        }
    }

    public ArrayList<String> listPairedDevice(){
        enableBluetooth();
        Set<BluetoothDevice> list = bluetoothAdapter.getBondedDevices();
        mapList= new HashMap<>();
        for(BluetoothDevice device : list){
            String name = device.getName();
            String adress = device.getAddress();
            mapList.put(name, adress);
        }
        return new ArrayList<String>(mapList.keySet());
    }

    public boolean connection (String nameDevice){
        String hardwareAdress = mapList.get(nameDevice);
        BluetoothSocket mSocket = null;
        boolean isConnected = false;

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(hardwareAdress);
        try{
            mSocket = device.createRfcommSocketToServiceRecord(mUUID);
            mSocket.connect();
            isConnected = true;
        }catch (Exception e){ }
        clientThread = new ConnectedThread(mSocket);
        clientThread.start();
        return isConnected;
    }

    public void sendData(String data){
        if(clientThread.mmSocket.isConnected()){
            clientThread.write(data.getBytes());
        }
    }

    public void close(){
        if(clientThread.mmSocket.isConnected()){
            clientThread.cancel();
        }
    }

    public void reconnection(){
        this.connection(key);
    }

    public void setKey(String key) {
        this.key = key;
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = handler.obtainMessage(
                            MessageConstants.MESSAGE_READ, numBytes, -1,
                            mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                mmOutStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

}
