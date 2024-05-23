package com.example.hardware;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Bluetooth extends AppCompatActivity {
    ListView listView;
    Button button;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        listView = findViewById(R.id.bluetoothlist);
        button = findViewById(R.id.showBluetooth);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBondedDevices();
            }
        });
    }

    private void showBondedDevices() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Bluetooth", "Bluetooth permission not granted.");
            return;
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if (bondedDevices.size() > 0) {
            List<String> deviceNames = new ArrayList<>();
            for (BluetoothDevice device : bondedDevices) {
                String deviceName = device.getName();
                deviceNames.add(deviceName);
                Log.d("Bluetooth", "Device Name: " + deviceName);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceNames);
            listView.setAdapter(arrayAdapter);
        } else {
            Toast.makeText(this, "No bonded Bluetooth devices found.", Toast.LENGTH_SHORT).show();
        }
    }
}
