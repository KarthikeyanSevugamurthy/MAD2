package com.example.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forget extends AppCompatActivity {
    EditText email;
    Button bt;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forward);

        email=findViewById(R.id.mail);
        bt = findViewById(R.id.button2);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = email.getText().toString().trim();
                if (TextUtils.isEmpty(e)) {
                    Toast.makeText(forget.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(e).matches())
                {
                    Toast.makeText(forget.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
     resetPassword(e);
                }
            }
        });

    }
    private void resetPassword(String e) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.sendPasswordResetEmail(e)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(forget.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(forget.this, MainActivity.class);

                            // Start the second activity
                            startActivity(intent);
                        } else {
                            Toast.makeText(forget.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
