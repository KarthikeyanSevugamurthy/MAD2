package com.example.g;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Buttons
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);

        // Set OnClickListener for Button 1
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, curr.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for Button 2
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, mapping.class);
                startActivity(intent);
            }
        });
    }
}
