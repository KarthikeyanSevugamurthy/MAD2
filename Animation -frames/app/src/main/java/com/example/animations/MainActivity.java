package com.example.animations;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    private AnimationDrawable isAnimation;
    private Button btn;
    private ImageView img;
    private boolean isStart = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);
        btn = findViewById(R.id.btn);

        img.setImageResource(R.drawable.animation_item);


        isAnimation = (AnimationDrawable)img.getDrawable();

        btn.setBackgroundColor(Color.GREEN);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view)
            {

                if (!isStart) {

                    isAnimation.start();

                    btn.setText("stop");

                    isStart = true;

                    btn.setBackgroundColor(Color.RED);
                }
                else {
                    isAnimation.stop();

                    btn.setText("Start");

                    isStart = false;

                    btn.setBackgroundColor(Color.GREEN);
                }
            }
        });
    }
}