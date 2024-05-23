package com.example.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;


public class AirplaneModeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
            boolean isAirplaneModeOn = intent.getBooleanExtra("state", false);
            if (isAirplaneModeOn) {
                Toast.makeText(context, "Airplane Mode ON", Toast.LENGTH_SHORT).show();
               // showMessage1(context,"success","aeroplane mode on");
            } else {
                Toast.makeText(context, "Airplane Mode OFF", Toast.LENGTH_SHORT).show();
               // showMessage1(context,"success","aeroplane mode off");
            }
        }
    }
   /* private void showMessage1(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setCancelable(true)
                .show();
    }*/
}