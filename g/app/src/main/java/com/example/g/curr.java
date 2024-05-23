package com.example.g;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class curr extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView locationTextView;
    private EditText latitudeEditText, longitudeEditText;
    private LinearLayout latitudeLayout, longitudeLayout;
    private Button saveLocationButton;
    private ProgressDialog progressDialog;
    private static final String CHANNEL_ID = "user_auth_notifications";
    private static final int NOTIFICATION_ID = 1;


    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curr);

        locationTextView = findViewById(R.id.locationTextView);
        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        latitudeLayout = findViewById(R.id.latitudeLayout);
        longitudeLayout = findViewById(R.id.longitudeLayout);
        saveLocationButton = findViewById(R.id.saveLocationButton);

        databaseHelper = new DatabaseHelper(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        createNotificationChannel();

        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        // Register for location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        // Add click listeners to the LinearLayout containers
        latitudeLayout.setOnClickListener(view -> {
            // Handle click event, if needed
        });

        longitudeLayout.setOnClickListener(view -> {
            // Handle click event, if needed
        });

        // Add click listener to the save location button
        saveLocationButton.setOnClickListener(view -> {
            String locationName = locationTextView.getText().toString();
            String latitudeStr = latitudeEditText.getText().toString();
            String longitudeStr = longitudeEditText.getText().toString();

            if (!latitudeStr.isEmpty() && !longitudeStr.isEmpty()) {
                double latitude = Double.parseDouble(latitudeStr);
                double longitude = Double.parseDouble(longitudeStr);
                databaseHelper.addLocation(locationName, latitude, longitude);
               /* progressDialog.setMessage("Registering Please Wait...");
                progressDialog.show();

                */
                showNotification("Successful", "You Current Location Succefully  Goted");
                Toast.makeText(curr.this, "Location saved to database", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(curr.this, "Location data is missing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start receiving location updates
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


                }
            } else {
                // Permission denied, show a message or handle accordingly


                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            // Handle location change
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Reverse geocoding to get the location name
            Geocoder geocoder = new Geocoder(curr.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    String locationName = addresses.get(0).getAddressLine(0);
                    locationTextView.setText(locationName);
                    latitudeEditText.setText(String.valueOf(latitude));
                    longitudeEditText.setText(String.valueOf(longitude));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            // Handle provider disabled
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            // Handle provider enabled
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Handle status change
        }
    }
    private void showNotification(String title, String message) {
        Intent dismissIntent = new Intent(this, NotificationDismissReceiver.class);
        dismissIntent.putExtra("notification_id", NOTIFICATION_ID);
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_1x_mobiledata_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.baseline_1x_mobiledata_24, "Dismiss", dismissPendingIntent); // Add dismiss action

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

