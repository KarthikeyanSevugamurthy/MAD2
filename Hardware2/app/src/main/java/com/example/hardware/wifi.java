package com.example.hardware;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class wifi extends AppCompatActivity {
    ListView listView;
    Button button;
    WifiManager wifiManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        listView = findViewById(R.id.wifilist);
        button = findViewById(R.id.showWifi);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWifiDevices();
            }
        });
    }

    private void showWifiDevices() {
        // Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Error","Program Returned");
            return;
        }

        List<ScanResult> wifi = wifiManager.getScanResults();
        if(wifi.size() > 0){
            String[] devices = new String[wifi.size()];
            int index = 0;
            for(;index<wifi.size();index++){
                String details= wifi.get(index).toString();
                Pattern pattern = Pattern.compile("SSID: (.*?),");
                // Match the pattern against the input string
                Matcher matcher = pattern.matcher(details);
                // Check if the pattern is found
                if (matcher.find()) {
                    String ssid = matcher.group(1); // Extract the SSID
                    devices[index] = ssid;
                }

                Log.d("Wifi", "Device Name: " + devices[index]);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, devices);
            listView.setAdapter(arrayAdapter);
        }
    }
}


