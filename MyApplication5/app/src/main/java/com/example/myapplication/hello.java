
package com.example.myapplication;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class hello extends AppCompatActivity {

    private TimePicker timePicker;
    private Button setNotificationBtn;
    private static final String CHANNEL_ID = "user_auth_notifications";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time);

        timePicker = findViewById(R.id.timePicker);
        setNotificationBtn = findViewById(R.id.setNotificationBtn);
        createNotificationChannel();
        setNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNotification();
            }
        });
    }

    private void setNotification() {
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

// Convert hour and minute to string format
        String hourString = String.valueOf(hour);
        String minuteString = String.valueOf(minute);

// Store the string values in variables
        String timeString = hourString + ":" + minuteString;

// Example of storing the timeString in a variable for later use
// You can store it in SharedPreferences, a database, or any other suitable storage mechanism
        String storedTime = timeString;

// Example of displaying the stored time
        Toast.makeText(hello.this, "Stored Time: " + storedTime, Toast.LENGTH_SHORT).show();
        showNotification("Remainder notification", storedTime);
        Intent media = new Intent(hello.this, MainActivity.class);
        startActivity(media);


    }
    private void showNotification(String title, String message) {
        Intent dismissIntent = new Intent(this, NotificationDismissReceiver.class);
        dismissIntent.putExtra("notification_id", NOTIFICATION_ID);
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_arrow_back_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.baseline_arrow_back_24, "Dismiss", dismissPendingIntent); // Add dismiss action

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling ActivityCompat#requestPermissions
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "User Authentication Notifications";
            String description = "Notifications for user authentication status";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}


