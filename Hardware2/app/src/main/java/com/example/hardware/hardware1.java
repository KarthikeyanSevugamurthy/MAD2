package com.example.hardware;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class hardware1 extends AppCompatActivity
{

    private Button Bt, wi, cam, sensorr;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware1);


        Bt = findViewById(R.id.bluetooth);
        wi = findViewById(R.id.wifibutton);
        cam = findViewById(R.id.camera_button);
        sensorr = findViewById(R.id.sensorButton);

        Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(hardware1.this, "Bluetooth clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(hardware1.this , Bluetooth.class));
            }
        });

        wi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(hardware1.this, "Wifi clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(hardware1.this , wifi.class));
            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(hardware1.this, "Camera clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(hardware1.this , camera.class));
            }
        });

        sensorr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(hardware1.this, "Sensor clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(hardware1.this , sensor.class));
            }
        });
    }

}
