package com.example.firebaseauth;



import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    TextView t,text,text1;
    ImageView i;
    Button textView;
    FirebaseAuth mAuth;
    private GoogleSignInClient client;
    private static final String CHANNEL_ID = "user_auth_notifications";
    private static final int NOTIFICATION_ID = 1;
    @SuppressLint({"MissingInflatedId", "MissingSuperCall"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = (TextView) findViewById(R.id.t2);
        i = findViewById(R.id.logo);
        t.setText("Login");
        text = findViewById(R.id.textview20);
//        text1=findViewById(R.id.t3);

        ProgressDialog progressDialog = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), register.class);
                startActivity(intent);
            }
        });

        createNotificationChannel();
        textView = findViewById(R.id.button2);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = client.getSignInIntent();
                startActivityForResult(i, 1234);
            }
        });

        text1 = findViewById(R.id.t3);
        if (text1 != null) {
            text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent forgetintent = new Intent(MainActivity.this, forget.class);
                    startActivity(forgetintent);
                }
            });
        } else {
            Log.e("MainActivity", "Button text1 is null");
        }


        // Assuming you have a button with an ID "myButton" in your layout

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                String mail = ((EditText) findViewById(R.id.mail)).getText().toString();
                if (TextUtils.isEmpty(mail)) {
                    Toast.makeText(MainActivity.this, "Enter mail", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "Enter pass", Toast.LENGTH_LONG).show();
                    return;
                }
                progressDialog.setMessage("Logging in Please Wait...");
                progressDialog.show();
                Toast.makeText(MainActivity.this, mail + " " + password, Toast.LENGTH_LONG).show();
                mAuth.signInWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplicationContext(), "Login Succesful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), first.class);
                                    showNotification("Login Successful", "Welcome back!");
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        Button signInButton = findViewById(R.id.button2);
// Set OnClickListener for the Google Sign-In button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // signIn(); // Call the signIn() method to start Google Sign-In flow
            }
        });

        ImageView logoImageView = findViewById(R.id.logo);

        // Set OnClickListener for the ImageView
        logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a Toast message when the ImageView is clicked
                showToast("ImageView Clicked!");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }






    private void showNotification(String title, String message) {
        Intent dismissIntent = new Intent(this, NotificationDismissReceiver.class);
        dismissIntent.putExtra("notification_id", NOTIFICATION_ID);
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_download_done_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.baseline_download_done_24, "Dismiss", dismissPendingIntent); // Add dismiss action

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

