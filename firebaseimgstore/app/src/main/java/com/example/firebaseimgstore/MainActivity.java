package com.example.firebaseimgstore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    private StorageReference storageReference;
    private LinearProgressIndicator progress;
    private ImageView img;
    private Uri image;
    private Button select, upload;

    // Initialize the ActivityResultLauncher for picking an image
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    image = result.getData().getData();
                    img.setImageURI(image);
                    upload.setVisibility(View.VISIBLE); // Make the upload button visible
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        img = findViewById(R.id.imageView);
        select = findViewById(R.id.button);
        upload = findViewById(R.id.button2);
        progress = findViewById(R.id.progress);

        // Initialize Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().getReference();

        // Set onClick listener for the select button
        select.setOnClickListener(v -> openGallery());

        // Set onClick listener for the upload button
        upload.setOnClickListener(v -> uploadImageToFirebase());

        // Initially hide the upload button
        upload.setVisibility(View.GONE);
    }

    // Method to open the gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }

    // Method to upload image to Firebase Storage
    private void uploadImageToFirebase() {
        if (image != null) {
            // Show progress bar
            progress.setVisibility(View.VISIBLE);

            // Create a reference to "images/filename.jpg"
            StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");

            // Upload the file to Firebase Storage
            UploadTask uploadTask = imageRef.putFile(image);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Handle successful upload
                progress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                // Handle failed upload
                progress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(snapshot -> {
                // Handle progress update
                double progressPercentage = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progress.setProgress((int) progressPercentage);
            });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
}