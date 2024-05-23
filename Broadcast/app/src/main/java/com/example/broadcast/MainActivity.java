package com.example.broadcast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.IntentFilter;

public class MainActivity extends AppCompatActivity {

    private AirplaneModeReceiver airplaneModeReceiver;
    private BatteryLevelReceiver batteryLevelReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register dynamic broadcast receivers
        registerAirplaneModeReceiver();
        registerBatteryLevelReceiver();
        Button showDialogButton = findViewById(R.id.showDialogButton);

        // Set click listener for the button
        showDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the AlertDialog
                showAlertDialog();
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This is an AlertDialog")
                .setTitle("Alert")
                .setPositiveButton("OK", null); // Null listener for dismissing dialog only

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void registerAirplaneModeReceiver() {
        airplaneModeReceiver = new AirplaneModeReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);

        registerReceiver(airplaneModeReceiver, filter);

    }

    private void registerBatteryLevelReceiver() {
        batteryLevelReceiver = new BatteryLevelReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister dynamic broadcast receivers
        unregisterReceiver(airplaneModeReceiver);
        unregisterReceiver(batteryLevelReceiver);
    }

}
