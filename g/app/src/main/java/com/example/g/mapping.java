package com.example.g;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class mapping extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private ProgressDialog progressDialog;
    private static final String CHANNEL_ID = "user_auth_notifications";
    private static final int NOTIFICATION_ID = 1;

    Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        EditText searchEditText = findViewById(R.id.searchEditText);
        createNotificationChannel();
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = searchEditText.getText().toString().trim();
                if (!searchString.isEmpty()) {
                    Geocoder geocoder = new Geocoder(mapping.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(searchString, 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                            googleMap.clear(); // Clear previous markers
                            googleMap.addMarker(new MarkerOptions().position(location).title(searchString));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                            showNotification(" Successful", "Address Fetched!");

                        } else {
                            Toast.makeText(mapping.this, "Location not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mapping.this, "Please enter an address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap mapping) {
        googleMap = mapping;

        // Initialize default location (e.g., your user's location)
        LatLng defaultLocation = new LatLng(9.9252, 78.1198); // Example coordinates (Mountain View, CA)

        // Add marker to default location
        googleMap.addMarker(new MarkerOptions().position(defaultLocation).title("Thiagrajar college of enginering"));

        // Move camera to default location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));

        // Enable user interaction with the map
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Reverse geocoding to fetch complete address
        Geocoder geocoder = new Geocoder(mapping.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(defaultLocation.latitude, defaultLocation.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String completeAddress = address.getAddressLine(0); // Get the first line of the address
                Log.d("ReverseGeocode", "Complete Address: " + completeAddress); // Add this line for logging
                Toast.makeText(mapping.this, "Complete Address: " + completeAddress, Toast.LENGTH_LONG).show();
                showNotification(" Successful", completeAddress+"Address Fetched!");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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