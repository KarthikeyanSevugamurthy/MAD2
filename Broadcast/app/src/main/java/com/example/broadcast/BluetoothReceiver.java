package com.example.broadcast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
            int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
            switch (state) {
                case android.bluetooth.BluetoothAdapter.STATE_OFF:
                    Toast.makeText(context, "Bluetooth OFF", Toast.LENGTH_SHORT).show();
                    showAlert(context, "Bluetooth is turned OFF");
                    break;
                case android.bluetooth.BluetoothAdapter.STATE_ON:
                    Toast.makeText(context, "Bluetooth ON", Toast.LENGTH_SHORT).show();
                    showAlert(context, "Bluetooth is turned ON");
                    break;
            }
        }
    }

    private void showAlert(Context context, String message) {
        // Ensure the context can show dialogs
        if (!(context instanceof android.app.Activity)) {
            // If it's not an Activity context, we can't show a dialog
            return;
        }

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(message)
                        .setTitle("Bluetooth State Changed")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

}
